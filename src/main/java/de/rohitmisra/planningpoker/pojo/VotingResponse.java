package de.rohitmisra.planningpoker.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VotingResponse implements IGenericResponse{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("average")
	Double average;

	public VotingResponse(Double average) {
		super();
		this.average = average;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}
}
