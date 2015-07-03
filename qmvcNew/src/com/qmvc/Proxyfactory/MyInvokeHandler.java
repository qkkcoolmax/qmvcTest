package com.qmvc.Proxyfactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.qmvc.annotation.Aop;
import com.qmvc.annotation.TransactionClass;
import com.qmvc.annotation.TransactionMethod;
import com.qmvc.core.InterceptorInterface;
import com.qmvc.core.JvnConfig;

/**
 * 
 * service类的代理handler，用于注入action
 * 
 * */
public class MyInvokeHandler implements InvocationHandler {

	private Object object;
	private Aop aop=null;
	private boolean tran = false;
	public MyInvokeHandler(Object ob) {
		this.object = ob;
		if(ob.getClass().getAnnotation(TransactionClass.class)!=null){
			this.tran=true;
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object reob = null;
		Annotation[] anos = method.getAnnotations();
		for(Annotation ano :anos){
			if(ano.getClass()==Aop.class){
				aop = (Aop) ano;
			}
			if(ano.getClass()==TransactionMethod.class){
				tran=true;
			}
			
			if(aop==null&&tran==false){
				reob = method.invoke(object, args);
			}else if(aop!=null&&tran==false){
				InterceptorInterface interceptor = (InterceptorInterface) JvnConfig.beanfactory
						.getSimpleBean(aop.interceptor());
				try{
				interceptor.before();
				reob = method.invoke(object, args);
				interceptor.after();
				}catch(Exception e){
					interceptor.exception();
					throw new RuntimeException(e);
				}
				}else if(aop==null&&tran==true){
					
				Connection con = JvnConfig.pool.getConnection();
				con.setAutoCommit(false);
				try {
					reob = method.invoke(object, args);
					con.commit();
				} catch (RuntimeException e) {
					con.rollback();
					throw e;
				} finally {
					//将连接归还线程池
					con.close();
					//清空threadlocal中的connection，下次要用重新取。
					JvnConfig.pool.clearConnection();
				}
				
			}else{
				InterceptorInterface interceptor = (InterceptorInterface) JvnConfig.beanfactory
						.getSimpleBean(aop.interceptor());
				interceptor.before();
				Connection con = JvnConfig.pool.getConnection();
				con.setAutoCommit(false);
				try {
					reob = method.invoke(object, args);
					interceptor.after();
				} catch (Exception e) {
					con.rollback();
					interceptor.exception();
					throw e;
				} finally {
					con.close();
					JvnConfig.pool.clearConnection();
				}
				
			}
			
		}
		return reob;
	}

}
