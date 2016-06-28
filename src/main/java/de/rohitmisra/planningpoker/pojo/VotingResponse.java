package de.rohitmisra.planningpoker.pojo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VotingResponse implements IGenericResponse{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("average")
	Double average;

	@JsonProperty("result")
	String result;
	
	@JsonProperty("votes")
	List<VoteRequest> votes = new ArrayList<VoteRequest>();

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<VoteRequest> getVotes() {
		return votes;
	}

	public void setVotes(List<VoteRequest> votes) {
		this.votes = votes;
	}
	
	public void addVote(VoteRequest vote) {
		this.votes.add(vote);
	}

	public VotingResponse(Double average, String result, List<VoteRequest> votes) {
		super();
		this.average = average;
		this.result = result;
		this.votes = votes;
	}

	
	
	

}
