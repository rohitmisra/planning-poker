package de.rohitmisra.planningpoker.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VoteRequest {
	@JsonProperty("vote")
	private Double vote;
	
	@JsonProperty("user")
	private String user;

	private Long time;
	
	public Double getVote() {
		return vote;
	}
	public void setVote(Double vote) {
		this.vote = vote;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public VoteRequest(Double vote, String user, Long time) {
		super();
		this.vote = vote;
		this.user = user;
		this.time = time;
	}
	
	
}
