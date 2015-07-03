package com.qmvc.Proxyfactory;

import java.lang.reflect.Proxy;

public class ProxyFactory {

	public ProxyFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static Object createProxy(Object ob) {
		MyInvokeHandler handler = new MyInvokeHandler(ob);
		return Proxy.newProxyInstance(ob.getClass().getClassLoader(), ob
				.getClass().getInterfaces(), handler);
	}
}
