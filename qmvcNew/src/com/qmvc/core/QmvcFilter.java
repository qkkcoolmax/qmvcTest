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
		System.out.println("qmvc框架销毁");

	}

	/**
	 * 
	 * 从过滤器本身的规范来说，应该是所有请求都是这一个过滤器对象来处理。 到此，用户的路径到action已经配置完全，放在全局常量中。
	 * 
	 * 这里从请求中解析出来要請求的uri，從uri中將用來處理該請求的類名和方法名也分别解析出來。
	 * 
	 * 再根据解析出来的类名从全局map中拿到相应action的类对象。
	 * 如果不存在，就拋出錯誤。如果存在，將要請求的方法，action類對象，request對象和response對象傳入handler中。
	 * 
	 * 注意handler定义在框架config中，作为私有实例域持有。用get可以拿到。
	 * 
	 * 
	 * */

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		// 对请求和响应设置合适的编码。
		request.setCharacterEncoding(config.CONSTANT.getEncoding());
		// 這個設置控制了待會从请求体解析参数出来时的编码、
		response.setCharacterEncoding(config.CONSTANT.getEncoding());
		response.setContentType("text/html;" + config.CONSTANT.getEncoding());
		// 这个控制了待会返回输出流时，响应体中的编码方式。

		String uri = req.getRequestURI();
		String context = req.getContextPath();

		String result[] = UrlKit.getUriTail(uri, context);

		System.out.println(result[0] + ";" + result[1]);
		// 根据路由信息从全局配置map中拿到对应的类对象。
		Class<?> controller = config.CONSTANT.getRoute(result[0]);

		PrintKit.printAccess(uri, controller, result[1], req);
		// 先简单的对静态资源放行一下，这里将所有的静态资源都放行了，有待改进。
		// 注意即使這樣，web-inf下的內容也不能直接通過静态资源的方式訪問，应该是tomcat自身的限制。

		if (uri.indexOf(".") > 0) {
			chain.doFilter(request, response);
			// 注意对tomcat所有请求都会找servlet，找不到就会去到默认的servlet。就是处理静态资源那个。
			// 所以這裡放行过后，发现匹配不上任何servlet就会由默认的servlet处理这个请求。
		} else {

			if (controller == null) {
				throw new RuntimeException("该controller不存在");
			} else {
				// 在这里也可以考虑使用aop注解来实现对action访问的拦截。
				// 也就是說在action类上使用了Aop注解，要使用 该类处理的所有请求都会先通过该拦截器。
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
	 * web服务器会首先回调用戶配置的filter的这个方法，這個方法中先要从配置文件中取得用户的config类名。
	 * 用户config繼承了框架的config 反射调用用戶config的init.
	 * init方法定义在框架父config中，方法调用了两个用户复写的两个设置方法，分别将用户要设置的参数真正的存进了一个全局常量中
	 * 
	 * 注意全局常量一个是map，用户存储用户的路徑和实际action類名的映射。
	 * 
	 * 到此，初始化结束。
	 * 
	 * 
	 * 
	 * */
	public void init(FilterConfig param) throws ServletException {
		System.out.println("框架启动");
		String paramName = param.getInitParameter("initParm");
		System.out.println(this.getClass().getClassLoader());
		System.out.println("paramName :" + paramName);
		try {
			config = (QmvcConfig) Class.forName(paramName).newInstance();// 獲得了一个jvnconfig對象。放到了当前对象的域中。
			config.init();// 初始化
		} catch (Exception e) {

		}
	}
}
