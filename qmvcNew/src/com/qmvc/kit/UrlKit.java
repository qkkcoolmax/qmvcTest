package com.qmvc.kit;

public class UrlKit {

	/**
	 * 返回一个数组，第一个表示 属于controller 路由 ，第二个表示 controllers的方法
	 * 
	 * @param uri
	 * @param contextPath
	 * @return
	 */
	
	
	/**
	 * 
	 * 注意这里会判断，uri为:/应用名/ROUTE/方法名
	 *  
	 *   如/qmvc/haha/dfsa/add ，route为/haha/dfsa/   方法名为add
  ，	 *   /qmvc/haha/dfsa/add/   route 为haha/dfsa/add/,方法名就会是默认的index
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
