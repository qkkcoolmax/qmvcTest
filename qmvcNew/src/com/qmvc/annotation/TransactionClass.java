package com.qmvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 类的声明式事务注解，
 * 使用此注解的类必须实现接口。
 * 类中全部方法将开启事务。
 *
 * 
 * */

@Retention(RetentionPolicy.RUNTIME)

public @interface TransactionClass {

	
}
