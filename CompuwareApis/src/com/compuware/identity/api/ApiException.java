package com.compuware.identity.api;

public class ApiException extends Exception {
	private static final long serialVersionUID = 1L;

	private int status;
	
	public ApiException(int status, String message) {
	    super(message);
	    this.status = status;
	}
	
	public int getStatus () {
		return this.status;
	}

}
