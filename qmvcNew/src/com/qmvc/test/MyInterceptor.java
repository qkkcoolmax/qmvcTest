package com.qmvc.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qmvc.core.InterceptorInterface;

/**
 * 
 * ׼ȷ����˵���Ҿ������������aop��ʵ��ԭ��
 * 
 * */

public class MyInterceptor implements InterceptorInterface {

	@Override
	public void before() {
		System.out.println("����ǰִ��");
	}

	@Override
	public void after() {
		System.out.println("�����ִ��");
	}

	@Override
	public void exception() {
		System.out.println("�������Ҳ�ִ��");

	}

	/*
	 * @Override public boolean interAction(HttpServletRequest req,
	 * HttpServletResponse res) { return false; }
	 */

}
