package com.qmvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ������ע�⡣ʹ��ԓ�]������Ñ􌍬F�Ĕr�����ӿ���Ϳ���ʹ�ÿ���ṩ�����������ܡ�
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Aop {

	Class interceptor();// �����ڼ�ֵ��һ��ʹ�á����ﶨ���ˣ��Ǳ߾ͻ��һ�������û��������������õĲ���ֵ
	//�����ʾʹ�ø�ע��ͱ������interceptor��������ҸĽ���ֵӦ����һ�������class����

	// String name();
}
