package com.qmvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 
 * 用于注入service的注解,修饰set方法。
 * */

@Retention(RetentionPolicy.RUNTIME)
public @interface AutoWired {

	
}
