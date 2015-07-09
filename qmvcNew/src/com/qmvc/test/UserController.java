package com.qmvc.test;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qmvc.annotation.Aop;
import com.qmvc.annotation.Controller;
import com.qmvc.annotation.OnlineController;
import com.qmvc.annotation.OnlineMethod;
import com.qmvc.core.QmvcController;
import com.qmvc.onlinedoc.Online;

@OnlineController(memo = "测试用的controller")
@Controller(space = "/test")
public class UserController extends QmvcController {

	public void echo() {
		// 注意父类的私有属性，子类继承了但是被隐藏了，但是通过调用父类继承过来的的方法还是可以得到的。
		// HttpServletRequest request = getRequest();
		/* HttpServletResponse response = getResponse(); */
		String username = getRequest().getParameter("username");
		// 注意如果没有这一个参数,tomcat的request对象被设计为返回一个字符串null。而不是對象null。
		// response.getWriter().write("你好," + username);
		renderString(username + ",欢迎进入qmvc，你将会有无与伦比的技术体验");

	}

	@OnlineMethod(memo = "返回一个测试页面", method = "get", param = "没有参数")
	public void reHtml() {
		renderHtmL("/WEB-INF/test.html");
	}

	@OnlineMethod(memo = "重定向到百度", method = "get", param = "没有参数")
	public void rePoint() {
		redirect("www.baidu.com");
	}

	public void useBean() {
		// 注意只有在框架使用者在这里真正調用这个方法时，才会执行那一套反射注入的过程。最後通过反射拿到的一个對象。
		User user = (User) getObject(User.class);
		System.out.println(user);
		// 注意null对象转换成字符串以后会成为“null”，如果直接對其调用方法，仍然会报空指针，
	}

	public void upload() {
		// 转发到静态资源。
		renderHtmL("/WEB-INF/uploadFile.html");

	}

	public void receiceFile() {
		String[] filenames = { "test2" };
		getFiles("C:/Users/qkkcoolmax/Desktop/", filenames, 4 * 1024 * 1024);
		System.out.println(getFormParm("name"));
		renderString("上传成功");
	}

	public void onlinedoc() {
		List<Online> tmp = getDocs();
		System.out.println(tmp.size());
		setAttrbute("onlineList", tmp);
		renderJsp("/WEB-INF/index.jsp");
	}

	
	public void regMember() {
		String username = getPar("username");
		String age = getPar("age");
		String income = getPar("income");

		Member member = new Member();
		//字段名可以少，少了的话，sav就只会save出现的字段，字段名如果多了的话，会报错。不能多。
		member.set("name", username);
		member.set("age", age);
		member.set("income", income);
		member.save();
	}

}
