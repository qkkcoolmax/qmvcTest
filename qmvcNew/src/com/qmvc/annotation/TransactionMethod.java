package com.qmvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 方法的声明式事务注解，
 * 使用此注解的方法所在的类必须实现接口。
 *
 * 
 * */

@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionMethod {
	

}
