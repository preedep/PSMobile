/**
 * 
 */
package com.epro.psmobile.remote.api;

import java.io.IOException;

/**
 * @author thrm0006
 *
 */
public class ServiceMobileException extends IOException {

	private int errorCode;
	private String responseBody;
	
	
	/**
	 * 
	 */
	public ServiceMobileException() {
		// TODO Auto-generated constructor stub
	}

	public ServiceMobileException(int errorCode){
		this.errorCode = errorCode;
	}
	
	public ServiceMobileException(int errorCode, String responseBody){
		this.errorCode = errorCode;
		this.responseBody = responseBody;
	}
	/**
	 * @param detailMessage
	 */
	public ServiceMobileException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ServiceMobileException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceMobileException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

}
