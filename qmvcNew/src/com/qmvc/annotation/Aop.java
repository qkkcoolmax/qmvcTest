package com.qmvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 拦截器注解。使用該註解搭配用戶實現的攔截器接口类就可以使用框架提供的拦截器功能。
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Aop {

	Class interceptor();// 类似于键值对一样使用。这里定义了，那边就会多一个方法用户或者这里面设置的参数值
	//这里表示使用该注解就必须给出interceptor这个键，且改建的值应该是一个类对象，class对象。

	// String name();
}
