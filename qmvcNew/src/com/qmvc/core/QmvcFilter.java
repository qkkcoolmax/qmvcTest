package com.qmvc.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qmvc.annotation.Aop;
import com.qmvc.kit.PrintKit;
import com.qmvc.kit.UrlKit;
import com.qmvc.test.MyConfig;

/**
 * 
 * 
 * @author Administrator
 * 
 */
public class QmvcFilter implements Filter {
	QmvcConfig config;

	public void destroy() {
		System.out.println("qmvc�������");

	}

	/**
	 * 
	 * �ӹ���������Ĺ淶��˵��Ӧ����������������һ������������������ ���ˣ��û���·����action�Ѿ�������ȫ������ȫ�ֳ����С�
	 * 
	 * ����������н�������ҪՈ���uri����uri�Ќ��Á�̎��ԓՈ�������ͷ�����Ҳ�ֱ��������
	 * 
	 * �ٸ��ݽ���������������ȫ��map���õ���Ӧaction�������
	 * ��������ڣ��͒����e�`��������ڣ���ҪՈ��ķ�����action���request�����response�������handler�С�
	 * 
	 * ע��handler�����ڿ��config�У���Ϊ˽��ʵ������С���get�����õ���
	 * 
	 * 
	 * */

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		// ���������Ӧ���ú��ʵı��롣
		request.setCharacterEncoding(config.CONSTANT.getEncoding());
		// �@���O�ÿ����˴����������������������ʱ�ı��롢
		response.setCharacterEncoding(config.CONSTANT.getEncoding());
		response.setContentType("text/html;" + config.CONSTANT.getEncoding());
		// ��������˴��᷵�������ʱ����Ӧ���еı��뷽ʽ��

		String uri = req.getRequestURI();
		String context = req.getContextPath();

		String result[] = UrlKit.getUriTail(uri, context);

		System.out.println(result[0] + ";" + result[1]);
		// ����·����Ϣ��ȫ������map���õ���Ӧ�������
		Class<?> controller = config.CONSTANT.getRoute(result[0]);

		PrintKit.printAccess(uri, controller, result[1], req);
		// �ȼ򵥵ĶԾ�̬��Դ����һ�£����ｫ���еľ�̬��Դ�������ˣ��д��Ľ���
		// ע�⼴ʹ�@�ӣ�web-inf�µă���Ҳ����ֱ��ͨ�^��̬��Դ�ķ�ʽ�L����Ӧ����tomcat��������ơ�

		if (uri.indexOf(".") > 0) {
			chain.doFilter(request, response);
			// ע���tomcat�������󶼻���servlet���Ҳ����ͻ�ȥ��Ĭ�ϵ�servlet�����Ǵ���̬��Դ�Ǹ���
			// �����@�e���й��󣬷���ƥ�䲻���κ�servlet�ͻ���Ĭ�ϵ�servlet�����������
		} else {

			if (controller == null) {
				throw new RuntimeException("��controller������");
			} else {
				// ������Ҳ���Կ���ʹ��aopע����ʵ�ֶ�action���ʵ����ء�
				// Ҳ�����f��action����ʹ����Aopע�⣬Ҫʹ�� ���ദ����������󶼻���ͨ������������
				// ......................................................
				/*
				 * boolean auth = true; Aop aop = null; if ((aop = (Aop)
				 * controller.getAnnotation(Aop.class)) != null) { auth =
				 * ((InterceptorInterface) JvnConfig.beanfactory
				 * .getSimpleBean(aop.interceptor())).interAction(req, resp); }
				 * 
				 * if (auth) {
				 */
				config.getHandel().handel(result[1], controller, req, resp);
				// }
				// ..........................................................
			}
		}
	}

	/**
	 * web�����������Ȼص��Ñ����õ�filter������������@����������Ҫ�������ļ���ȡ���û���config������
	 * �û�config�^���˿�ܵ�config ��������Ñ�config��init.
	 * init���������ڿ�ܸ�config�У����������������û���д���������÷������ֱ��û�Ҫ���õĲ��������Ĵ����һ��ȫ�ֳ�����
	 * 
	 * ע��ȫ�ֳ���һ����map���û��洢�û���·����ʵ��action�����ӳ�䡣
	 * 
	 * ���ˣ���ʼ��������
	 * 
	 * 
	 * 
	 * */
	public void init(FilterConfig param) throws ServletException {
		System.out.println("�������");
		String paramName = param.getInitParameter("initParm");
		System.out.println(this.getClass().getClassLoader());
		System.out.println("paramName :" + paramName);
		try {
			config = (QmvcConfig) Class.forName(paramName).newInstance();// �@����һ��jvnconfig���󡣷ŵ��˵�ǰ��������С�
			config.init();// ��ʼ��
		} catch (Exception e) {

		}
	}
}
