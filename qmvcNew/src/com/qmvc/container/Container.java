package com.qmvc.container;

import java.util.HashMap;
import java.util.Map;

public class Container {

	private final static Map<String, Object> contextMap = new HashMap<String, Object>();

	public Container() {
		// TODO Auto-generated constructor stub
	}

	public synchronized void putObject(String className, Object object) {
		contextMap.put(className, object);
	}

	public synchronized Object getObject(String className) {
		return contextMap.get(className);
	}

}
