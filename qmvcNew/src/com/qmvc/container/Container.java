package com.qmvc.container;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Container {

	/**
	 * 用于存放所有单代理Service的容器。
	 * 
	 * */
	private final static Map<Class, Object> contextMap = new ConcurrentHashMap<Class, Object>();

	public Container() {
		// TODO Auto-generated constructor stub
	}

	public static void putObject(Class clazz, Object object) {
		contextMap.put(clazz, object);
	}

	public static Object getObject(Class clazz) {
		return contextMap.get(clazz);
	}

}
