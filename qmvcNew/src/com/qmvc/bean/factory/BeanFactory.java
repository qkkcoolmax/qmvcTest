package com.qmvc.bean.factory;

public class BeanFactory {

	public Object getSimpleBean(Class clazz) {
		Object obj = null;
		try {
			obj = clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return obj;
	}
}
