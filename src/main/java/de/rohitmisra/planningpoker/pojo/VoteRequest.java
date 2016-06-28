package de.rohitmisra.planningpoker.pojo;

public class VoteRequest {
	Double vote;
	String user;
	Long time;
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
