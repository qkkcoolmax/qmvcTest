package com.qmvc.core;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qmvc.annotation.Aop;
import com.qmvc.annotation.AutoWired;
import com.qmvc.annotation.TransactionMethod;
import com.qmvc.kit.PrintKit;

/**
 * ����action
 * 
 * @author Administrator
 * 
 */
public class ActionHandle {

	/**
	 * ���� action
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
			 * handler����ͨ��action�����������������action�Č������Լ��������� Ȼ����ִ���䷽���� *
			 * */
			// Ŀ����ÿ������Ҫ����һ���µ�controller������
			Object obj = controller.newInstance();
			// ����ע������,��ʵ���ǻ�ȡ����Ӧ�ķ�����Ȼ�������һ�£������õķ����������������ķ��������ԾͰѲ������õ�������ȥ�ˡ�����������������������������������������������������������������
			controller.getMethod("setRequest", HttpServletRequest.class)
					.invoke(obj, req);
			controller.getMethod("setResponse", HttpServletResponse.class)
					.invoke(obj, resp);
			//ע����service����
			Method[] methods = controller.getMethods();
			for(Method mt :methods){
				if(mt.getAnnotation(AutoWired.class)!=null){
					Class[] classes = mt.getParameterTypes();
					Class inte = classes[0];
					List<Class>	 list =QmvcConfig.CONSTANT.getServClassList();
					for(Class cla:list){
						if(cla.getInterfaces()[0]==inte){
							 
							
							
							
							
						}
						
					}
				
				}
				
				
			}
			
			
			// ����������������������������������������������������������������������
			Method method = controller.getMethod(methodName, null);

			/**
			 * ��������aop����������
			 * */
			// �鿴���з����Ƿ񱻼�����Aop�@���]�⡣
			// ���û�����ע�⽫�᷵�ؿա�
			// ����ͻ���˸÷����ϵġ�ע�����
			Aop aop = method.getAnnotation(Aop.class);//

			TransactionMethod tm = method
					.getAnnotation(TransactionMethod.class);

			if (aop != null) {
				InterceptorInterface interceptor = (InterceptorInterface) QmvcConfig.beanfactory
						.getSimpleBean(aop.interceptor());// �����ǻ����ע���ֵ���е�ֵ.���ֵ��һ��class���Ñ��Լ������Լ��Ĕr������
				// �������ǿ���ֱ�������class��������û����������ʵ����Ȼ���ó����ͽ�ס��
				// ע�⣬����ʹ��bean����������ҪҪ��bean
				try {
					interceptor.before();

					// ��controller�ķ���Ҳʵ������֧�֡�
					if (tm != null) {
						Connection con = QmvcConfig.pool.getConnection();
						con.setAutoCommit(false);
						try {
							method.invoke(obj, null);
						} catch (RuntimeException e) {
							con.rollback();
						} finally {
							con.close();
							QmvcConfig.pool.clearConnection();
						}
					}

					interceptor.after();
				} catch (Exception e) {
					interceptor.exception();
					throw new RuntimeException(e);
				}
			} else {
				/**
				 * tm��һ������ʽ������ע�⡣�������η�����
				 * */
				if (tm != null) {
					Connection con = QmvcConfig.pool.getConnection();
					con.setAutoCommit(false);
					try {
						method.invoke(obj, null);
						con.commit();
					} catch (RuntimeException e) {
						con.rollback();
					} finally {
						// �����ӹ黹�̳߳�
						con.close();
						// ���threadlocal�е�connection���´�Ҫ������ȡ��
						QmvcConfig.pool.clearConnection();
					}
				}

			}

		} catch (Exception e) {

			System.out.println("����������");
			throw new RuntimeException(e);

		}

	}
}
