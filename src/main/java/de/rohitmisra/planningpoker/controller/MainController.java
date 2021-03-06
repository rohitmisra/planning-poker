package de.rohitmisra.planningpoker.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;

import de.rohitmisra.planningpoker.pojo.Conflict;
import de.rohitmisra.planningpoker.pojo.VoteRequest;
import de.rohitmisra.planningpoker.pojo.VotingResponse;

@Controller
@RequestMapping(value="/")
public class MainController extends BaseController implements InitializingBean {

	private static final long MESSAGE_QUEUE_PROCESS_FREQ = 500;
    
	private static final long DEFFERED_REQUEST_TIMEOUT = 360000;
	
	private List<String> users = new ArrayList<String>();
	
	private Double total;
	
	private final Map<DeferredResult<ResponseEntity<String>>,VoteRequest> deferredResultMap = new ConcurrentHashMap<DeferredResult<ResponseEntity<String>>, VoteRequest>();

	private List<VoteRequest> votes = new ArrayList<VoteRequest>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("started...");
		total = 0d;
	}
	
	
	@RequestMapping("/vote.json")
    public DeferredResult<ResponseEntity<String>> checkMessages(
    										  @RequestParam(value="user",   required=true)	String	user,
                                              @RequestParam(value="vote",	required=true)	Double	vote,
                                              @RequestParam(value="time",   required=true)	Long    time
                                             ) throws JsonParseException, IOException{
		if(!users.contains(user)){
    		throw new IllegalArgumentException("User doesn't exist");
    	}
		
		if(!allowedValues.contains(vote)){
    		throw new IllegalArgumentException("This value of vote is not allowed");
    	}
    	
    	for(VoteRequest voteRequest : votes){
    		if(voteRequest.getUser().equals(user))
    			throw new IllegalArgumentException(user + " has already Voted");
    	}
    	
    	total += vote;
    	
		final DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<ResponseEntity<String>>(DEFFERED_REQUEST_TIMEOUT); 
    	
    	VoteRequest req = new VoteRequest(vote, user, time);
    	votes.add(req);
    	deferredResultMap.put(deferredResult, req);
    	deferredResult.onCompletion(new Runnable() {
			@Override
			public void run() {
				deferredResultMap.remove(deferredResult);
			}
		});
    	deferredResult.onTimeout(new Runnable(){

			@Override
			public void run() {
				deferredResultMap.remove(deferredResult);
				deferredResult.setResult(new ResponseEntity<String>("", HttpStatus.NO_CONTENT));
			}
    		
    	});
    	return deferredResult;
    }
	
	
	
	@RequestMapping(
			value = "/create.json",
			method = { RequestMethod.GET },
			produces = { "application/json;charset=UTF-8" }
			)
	public ResponseEntity<String> createPOST( @RequestParam(value="user",   required=true)	String	user){
		if(users.contains(user)) throw new IllegalArgumentException("User already Exists");
		if(user!=null && !user.equals("")){
			users.add(user);
		}
		return new ResponseEntity<String>("{\"created\":\""+user+"\"}", HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/delete.json",
			method = { RequestMethod.GET },
			produces = { "application/json;charset=UTF-8" }
			)
	public ResponseEntity<String> delete( @RequestParam(value="user",   required=true)	String	user){
		if(!users.contains(user)) throw new IllegalArgumentException("User already Exists");
		if(user!=null && !user.equals("")){
			users.remove(user);
		}
		return new ResponseEntity<String>("{\"deleted\":\""+user+"\"}", HttpStatus.OK);
	}
	
	
	
    @SuppressWarnings("unchecked")
	@Scheduled(fixedRate = MESSAGE_QUEUE_PROCESS_FREQ)
    public void processMessageQueues() throws JsonProcessingException, IOException {
    	if (!this.deferredResultMap.isEmpty() && this.deferredResultMap.size()==users.size()) {
    		
    		Map<String, Object> votingResult = evaluate(votes);
    		
    		VotingResponse result;
			if(votingResult.get("result").equals("C")){
    			result = new VotingResponse(total/users.size(), "There are conflicts", (List<VoteRequest>) votingResult.get("votes"), "C", (List<Conflict>) votingResult.get("conflicts"));
    		}else{
    			result = new VotingResponse(total/users.size(), "We have a result", (List<VoteRequest>) votingResult.get("votes"), "S", null);
    		}
    		
    		
    		for(Entry<DeferredResult<ResponseEntity<String>>, VoteRequest> entry : this.deferredResultMap.entrySet()) {
		    	DeferredResult<ResponseEntity<String>> deferredResult = entry.getKey();
		    	deferredResult.setResult(new ResponseEntity<String>(serializeToJson(result, ""), HttpStatus.OK));
    		}
    		this.deferredResultMap.clear();
    		votes.clear();
    	}
    }
    
}
