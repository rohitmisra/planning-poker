package de.rohitmisra.planningpoker.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Conflict {
	@JsonProperty("firstUser")
	String firstUser;
	@JsonProperty("secondUser")
	String secondUser;
	
	
	public String getSecondUser() {
		return secondUser;
	}
	public void setSecondUser(String secondUser) {
		this.secondUser = secondUser;
	}
	public String getFirstUser() {
		return firstUser;
	}
	public void setFirstUser(String firstUser) {
		this.firstUser = firstUser;
	}


	public Conflict(String firstUser, String secondUser) {
		super();
		this.firstUser = firstUser;
		this.secondUser = secondUser;
	}
	
	
}
