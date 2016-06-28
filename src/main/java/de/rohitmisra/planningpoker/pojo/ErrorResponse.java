package de.rohitmisra.planningpoker.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse implements IGenericResponse{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("message")
	String message;

	@JsonProperty("code")
	Integer code;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public ErrorResponse(String message, Integer code) {
		super();
		this.message = message;
		this.code = code;
	}
	
	
}
