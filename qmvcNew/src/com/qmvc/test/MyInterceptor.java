package com.qmvc.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qmvc.core.InterceptorInterface;

/**
 * 
 * 准确的来说，我觉得这个更像是aop的实现原理。
 * 
 * */

public class MyInterceptor implements InterceptorInterface {

	@Override
	public void before() {
		System.out.println("我提前执行");
	}

	@Override
	public void after() {
		System.out.println("我最后执行");
	}

	@Override
	public void exception() {
		System.out.println("出错了我才执行");

	}

	/*
	 * @Override public boolean interAction(HttpServletRequest req,
	 * HttpServletResponse res) { return false; }
	 */

}
