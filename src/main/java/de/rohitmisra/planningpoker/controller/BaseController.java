package de.rohitmisra.planningpoker.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.MissingServletRequestParameterException;

import de.rohitmisra.planningpoker.pojo.Conflict;
import de.rohitmisra.planningpoker.pojo.ErrorResponse;
import de.rohitmisra.planningpoker.pojo.IGenericResponse;
import de.rohitmisra.planningpoker.pojo.VoteRequest;

public class BaseController {
    public static String serializeToJson(final IGenericResponse serviceResponse, final String callback) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        ByteArrayOutputStream jsonOutputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(jsonOutputStream, serviceResponse);
        if (callback != null && callback.trim().length() > 0) {
            return callback.concat("(").concat(jsonOutputStream.toString("UTF-8")).concat(")");
        }
        return jsonOutputStream.toString("UTF-8");
    }
    
	protected static List<Double> allowedValues = new ArrayList<Double>(Arrays.asList(-1d,0d,0.5d,1d,2d,3d,5d,8d,13d,20d,40d,80d,100d));
    
    public static Double maxDiff(Double[] A){
    	
        Integer N = A.length;
        if (N < 1) return 0d;

        Double max = -2d;
        Double result = 0d;

        for(int i = N-1; i >= 0; --i)
        {
            if(A[i] > max)
                max = A[i];

            Double tmpResult = max - A[i];        
            if(tmpResult > result)
                result = tmpResult;
        }

        return result;
    }
    
    public static Map<String, Object> evaluate(List<VoteRequest> votes){
    	Map<String, Object> votingResult = new HashMap<String, Object>();
    	
    	for (int i = 0; i < votes.size(); i++) {
    		double vote1 = (double) allowedValues.indexOf(votes.get(i).getVote())+1;
    	    for (int k = i + 1; k < votes.size(); k++) {
    	        if (votes.get(i) != votes.get(k)) {
    	        	double vote2 = (double) allowedValues.indexOf(votes.get(k).getVote())+1;
					if(Math.abs(vote1-vote2)>2){
						votingResult.put("result", "C");
						if(!votingResult.containsKey("conflicts")){
							votingResult.put("conflicts", new ArrayList<Conflict>());
						}
						((ArrayList)votingResult.get("conflicts")).add(new Conflict(votes.get(i).getUser() + ", "+votes.get(i).getVote(),votes.get(k).getUser() + ", "+votes.get(k).getVote()));
					}else{
						if(!votingResult.containsKey("result"))
							votingResult.put("result", "S");
					}
    	        }
    	    }
    	}
    	
    	votingResult.put("votes", votes);
    	return votingResult;
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> missingParameterError(MissingServletRequestParameterException ex, HttpServletRequest request)
    {
        final ErrorResponse r = new ErrorResponse(ex.getLocalizedMessage(), 400);
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        String responseBody = "";
        try {
            String callback = request.getParameter("callback");
            if (callback != null && callback.trim().length() > 0) {
                statusCode = HttpStatus.OK; // in case of callback the returning status is 200 and inside the error code is set
            }
            responseBody = serializeToJson(r, callback);
        }
        catch (IOException e) {
            // if we end up in a serialization error here, the service is basically dead
            responseBody = statusCode.getReasonPhrase();
        }
        return new ResponseEntity<String>(responseBody, statusCode);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> missingParameterError(IllegalArgumentException ex, HttpServletRequest request)
    {
        final ErrorResponse r = new ErrorResponse(ex.getLocalizedMessage(), 400);
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        String responseBody = "";
        try {
            String callback = request.getParameter("callback");
            if (callback != null && callback.trim().length() > 0) {
                statusCode = HttpStatus.OK; // in case of callback the returning status is 200 and inside the error code is set
            }
            responseBody = serializeToJson(r, callback);
        }
        catch (IOException e) {
            // if we end up in a serialization error here, the service is basically dead
            responseBody = statusCode.getReasonPhrase();
        }
        return new ResponseEntity<String>(responseBody, statusCode);
    }
}
