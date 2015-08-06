package com.qmvc.exception;

public class TooMuchParamException extends RuntimeException {

	public TooMuchParamException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public TooMuchParamException(String msg) {
		super(msg);
	}

}
