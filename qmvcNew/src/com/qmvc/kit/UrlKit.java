package com.qmvc.kit;

public class UrlKit {

	/**
	 * ����һ�����飬��һ����ʾ ����controller ·�� ���ڶ�����ʾ controllers�ķ���
	 * 
	 * @param uri
	 * @param contextPath
	 * @return
	 */
	
	
	/**
	 * 
	 * ע��������жϣ�uriΪ:/Ӧ����/ROUTE/������
	 *  
	 *   ��/qmvc/haha/dfsa/add ��routeΪ/haha/dfsa/   ������Ϊadd
  ��	 *   /qmvc/haha/dfsa/add/   route Ϊhaha/dfsa/add/,�������ͻ���Ĭ�ϵ�index
	 * 
	 * 
	 * */
	public static String[] getUriTail(String uri, String contextPath) {
		String result[] = { "", "" };
		String s = uri.substring(contextPath.length(), uri.length());

		if (s.length() > 0) {
			if (s.endsWith("/")) {
				result[0] = s;
				result[1] = "index";
			} else {
				String ss[] = s.split("/");
				if (ss.length > 2) {
					result[0] = s.substring(0,
							s.length() - ss[ss.length - 1].length());
					result[1] = ss[ss.length - 1];
				} else if (ss.length == 2) {
					result[0] = "/" + ss[1];
					result[1] = "index";
				}
			}
		}

		if (result[0].length() > 1 && result[0].endsWith("/")) {
			result[0] = result[0].substring(0, result[0].length() - 1);
		}

		return result;
	}

	public static void main(String[] args) {
		String uri = "";
		System.out.println(uri.split("/").length);
	}

}
