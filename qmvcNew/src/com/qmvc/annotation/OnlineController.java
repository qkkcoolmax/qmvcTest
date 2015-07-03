package com.qmvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**|
 * 在线文档Controller
 * 用于标记Controller，可以提供controller的功能�?
 * @author Administrator
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlineController {

	String memo();
}
