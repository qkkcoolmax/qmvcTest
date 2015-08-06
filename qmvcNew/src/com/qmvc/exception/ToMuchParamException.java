package com.qmvc.exception;

public class ToMuchParamException extends RuntimeException {

	public ToMuchParamException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
