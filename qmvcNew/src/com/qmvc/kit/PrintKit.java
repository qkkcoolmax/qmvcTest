package com.qmvc.kit;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * ��ӡ������
 * 
 * */
public class PrintKit {

	// private final static Logger log = Logger.getLogger("routeLog");
	// ��ʱ�Ȳ�����־��ֱ��ʹ�ñ�׼�������

	public static void printAccess(String uri, Class controller, String method,
			HttpServletRequest req) {
		String str_controller = String.valueOf(controller);
		if (controller != null && str_controller.trim().startsWith("class")) {
			str_controller = str_controller.trim().substring(6,
					str_controller.length());
		}

		System.out.println();
		System.out
				.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
		System.out.println("Frame : qmvc(since 2015)");
		System.out.println("Access Uri :" + uri);
		System.out.println("Controller : " + str_controller);
		System.out.println("Method : " + method);
		// System.out.println("Param :" +ServletKit.getParma(req));
		System.out
				.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
		System.out.println();
	}
}
