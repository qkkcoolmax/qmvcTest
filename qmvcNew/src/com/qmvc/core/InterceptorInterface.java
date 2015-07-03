package com.qmvc.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface InterceptorInterface {

	public void before();

	public void after();

	public void exception();

	/*
	 * public boolean interAction(HttpServletRequest req, HttpServletResponse
	 * res);
	 */

}
