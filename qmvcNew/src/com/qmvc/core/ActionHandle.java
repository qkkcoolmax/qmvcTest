package com.qmvc.core;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qmvc.Proxyfactory.ProxyFactory;
import com.qmvc.annotation.Aop;
import com.qmvc.annotation.AutoWired;
import com.qmvc.annotation.TransactionMethod;
import com.qmvc.container.Container;
import com.qmvc.kit.PrintKit;

/**
 * 处理action
 * 
 * @author Administrator
 * 
 */
public class ActionHandle {

	/**
	 * 处理 action
	 * 
	 * @param method
	 * @param controller
	 * @param req
	 * @param resp
	 */
	public void handel(String methodName, Class<?> controller,
			HttpServletRequest req, HttpServletResponse resp) {
		try {
			/**
			 * handler方法通过action类对象反射生成了相應action的實例。以及方法对象 然后反射执行其方法。 *
			 * */
			// 目测是每次请求都要反射一个新的controller来处理。
			Object obj = controller.newInstance();
			// 反射注入属性,其实就是获取到相应的方法象，然后反射调用一下，而调用的方法本身就是设置域的方法，所以就把参数设置到对象中去了。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
			controller.getMethod("setRequest", HttpServletRequest.class)
					.invoke(obj, req);
			controller.getMethod("setResponse", HttpServletResponse.class)
					.invoke(obj, resp);
			// 注入其service的代理，Service为单例，代理还是多例吧。
			Method[] methods = controller.getMethods();
			for (Method mt : methods) {
				if (mt.getAnnotation(AutoWired.class) != null) {
					Class[] classes = mt.getParameterTypes();
					Class inte = classes[0];
					List<Class> list = QmvcConfig.CONSTANT.getServClassList();
					for (Class cla : list) {
						if (cla.getInterfaces()[0] == inte) {
							Object service = Container.getObject(cla);
							if (service == null) {
								service = cla.newInstance();
								Container.putObject(cla, service);
							}
							Object proxy = ProxyFactory.createProxy(service);
							mt.invoke(obj, proxy);
							break;
						}
					}
				}
			} // 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
			Method method = controller.getMethod(methodName, null);
			/**
			 * 拦截器（aop）工作部分
			 * */
			// 查看所有方法是否被加上了Aop這個註解。
			// 如果没有这个注解将会返回空。
			// 否则就获得了该方法上的“注解对象”
			Aop aop = method.getAnnotation(Aop.class);//
			// action就不做事务了吧
			/*
			 * TransactionMethod tm = method
			 * .getAnnotation(TransactionMethod.class);
			 */
			if (aop != null) {
				InterceptorInterface interceptor = (InterceptorInterface) QmvcConfig.beanfactory
						.getSimpleBean(aop.interceptor());// 这里是获得了注解键值对中的值.这个值是一个class（用戶自己寫的自己的攔截器）
				// 所以我们可以直接用这个class反射出来用户拦截器类的实例。然后用超类型接住。
				// 注意，这里使用bean工厂来生成要要的bean
				try {
					interceptor.before();
					method.invoke(obj, null);
					interceptor.after();
				} catch (Exception e) {
					interceptor.exception();
					throw new RuntimeException(e);
				}
			} else {
				method.invoke(obj, null);
			}
		} catch (Exception e) {
			System.out.println("方法不存在");
			throw new RuntimeException(e);

		}

	}
}
