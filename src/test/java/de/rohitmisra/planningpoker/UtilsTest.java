package de.rohitmisra.planningpoker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.rohitmisra.planningpoker.controller.BaseController;
import de.rohitmisra.planningpoker.pojo.VoteRequest;

public class UtilsTest {

	@Test
	public void checkSuccess(){
		List<VoteRequest> votes = new ArrayList<VoteRequest>();
		votes.add(new VoteRequest(13d, "first", 0l));
		votes.add(new VoteRequest(13d, "second", 0l));
		votes.add(new VoteRequest(5d, "third", 0l));
		votes.add(new VoteRequest(8d, "fourth", 0l));
		
		Map<String, Object> votingResult = BaseController.evaluate(votes);
		
		Assert.assertTrue("There should not be a conflict",votingResult.get("result").equals("S"));
	}
	
	
	@Test
	public void checkConflict(){
		List<VoteRequest> votes = new ArrayList<VoteRequest>();
		votes.add(new VoteRequest(13d, "first", 0l));
		votes.add(new VoteRequest(13d, "second", 0l));
		votes.add(new VoteRequest(1d, "third", 0l));
		votes.add(new VoteRequest(3d, "fourth", 0l));
		votes.add(new VoteRequest(8d, "fifth", 0l));
		
		Map<String, Object> votingResult = BaseController.evaluate(votes);
		
		Assert.assertTrue("There should be a conflict",votingResult.get("result").equals("C"));
	}
}
