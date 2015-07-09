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

@OnlineController(memo = "�����õ�controller")
@Controller(space = "/test")
public class UserController extends QmvcController {

	public void echo() {
		// ע�⸸���˽�����ԣ�����̳��˵��Ǳ������ˣ�����ͨ�����ø���̳й����ĵķ������ǿ��Եõ��ġ�
		// HttpServletRequest request = getRequest();
		/* HttpServletResponse response = getResponse(); */
		String username = getRequest().getParameter("username");
		// ע�����û����һ������,tomcat��request�������Ϊ����һ���ַ���null�������ǌ���null��
		// response.getWriter().write("���," + username);
		renderString(username + ",��ӭ����qmvc���㽫���������ױȵļ�������");

	}

	@OnlineMethod(memo = "����һ������ҳ��", method = "get", param = "û�в���")
	public void reHtml() {
		renderHtmL("/WEB-INF/test.html");
	}

	@OnlineMethod(memo = "�ض��򵽰ٶ�", method = "get", param = "û�в���")
	public void rePoint() {
		redirect("www.baidu.com");
	}

	public void useBean() {
		// ע��ֻ���ڿ��ʹ���������������{���������ʱ���Ż�ִ����һ�׷���ע��Ĺ��̡�����ͨ�������õ���һ������
		User user = (User) getObject(User.class);
		System.out.println(user);
		// ע��null����ת�����ַ����Ժ���Ϊ��null�������ֱ�ӌ�����÷�������Ȼ�ᱨ��ָ�룬
	}

	public void upload() {
		// ת������̬��Դ��
		renderHtmL("/WEB-INF/uploadFile.html");

	}

	public void receiceFile() {
		String[] filenames = { "test2" };
		getFiles("C:/Users/qkkcoolmax/Desktop/", filenames, 4 * 1024 * 1024);
		System.out.println(getFormParm("name"));
		renderString("�ϴ��ɹ�");
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
		//�ֶ��������٣����˵Ļ���sav��ֻ��save���ֵ��ֶΣ��ֶ���������˵Ļ����ᱨ�����ܶࡣ
		member.set("name", username);
		member.set("age", age);
		member.set("income", income);
		member.save();
	}

}
