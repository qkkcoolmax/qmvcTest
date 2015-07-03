package com.qmvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 在线文档的方法注�?
 * @author Administrator
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlineMethod {

	String memo();
	String param();
	String method();
}
