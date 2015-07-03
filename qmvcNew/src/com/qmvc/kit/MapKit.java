package com.qmvc.kit;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.qmvc.core.JvnModel;
import com.qmvc.test.User;

public class MapKit {
	public static final SimpleDateFormat sdfDateTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat sdfDate = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * 把map转换成对应的Object
	 * 
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static Object map2Object(Map map, Class clazz) {
		try {
			Object object = clazz.newInstance();
			Map<String, String> methodMap = getMethods(clazz);
			for (Object obj : map.keySet()) {
				try {
					String methodName = "set"
							+ StrKit.firstCharToUpperCase(String.valueOf(obj));
					String simpleName = methodMap.get(methodName);

					Method method = null;
					if (simpleName.equals("String")) {

						method = clazz.getMethod(methodName, String.class);
						method.invoke(object, String.valueOf(map.get(obj)));
					}
					if (simpleName.equalsIgnoreCase("Double")) {

						method = clazz.getMethod(methodName, Double.class);
						method.invoke(object, Double.valueOf(map.get(obj) + ""));
					}
					if (simpleName.equalsIgnoreCase("double")) {

						method = clazz.getMethod(methodName, double.class);
						method.invoke(object, Double.valueOf(map.get(obj) + ""));
					}
					if (simpleName.equalsIgnoreCase("Long")) {
						method = clazz.getMethod(methodName, Long.class);
						method.invoke(object, Long.valueOf(map.get(obj) + ""));
					}
					if (simpleName.equalsIgnoreCase("long")) {
						method = clazz.getMethod(methodName, long.class);
						method.invoke(object, Long.valueOf(map.get(obj) + ""));
					}

					if (simpleName.equals("Integer")) {
						method = clazz.getMethod(methodName, Integer.class);
						method.invoke(object,
								Integer.valueOf(map.get(obj) + ""));
					}
					if (simpleName.equals("int")) {
						method = clazz.getMethod(methodName, int.class);
						method.invoke(object,
								Integer.valueOf(map.get(obj) + ""));
					}
					if (simpleName.equals("boolean")) {
						method = clazz.getMethod(methodName, boolean.class);
						method.invoke(object,
								Boolean.valueOf(map.get(obj) + ""));
					}
					if (simpleName.equals("Boolean")) {
						method = clazz.getMethod(methodName, Boolean.class);
						method.invoke(object,
								Boolean.valueOf(map.get(obj) + ""));
					}
					if (simpleName.equals("Date")) {
						method = clazz.getMethod(methodName, Date.class);
						try {
							Date date = sdfDateTime.parse(String.valueOf(map
									.get(obj)));
							method.invoke(object, date);
						} catch (Exception e) {
							try {
								Date date = sdfDate.parse(String.valueOf(map
										.get(obj)));
								method.invoke(object, date);
							} catch (Exception ee) {

							}
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			return object;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把map转换成对应的Object
	 * 
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static Object map2Model(Map map, Class<? extends JvnModel> clazz) {
		try {
			Object object = clazz.newInstance();
			Method m = clazz.getMethod("set", String.class, Object.class);
			for (Object obj : map.keySet()) {
				m.invoke(object, String.valueOf(obj), map.get(obj));
			}
			return object;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回该类�?set方法名称跟参数的simpleName
	 * 
	 * @param clazz
	 * @return
	 */
	public static Map getMethods(Class clazz) {
		Map<String, String> map = new HashMap<String, String>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {

			String methodName = method.getName();
			if (methodName.startsWith("set")) {
				Class[] paramClass = method.getParameterTypes();
				String paramName = paramClass[0].getSimpleName();
				map.put(methodName, paramName);
			}

		}
		return map;
	}

	public static Map<String, String> toStringMap(Map<String, Object> oMap) {
		Map<String, String> map = new HashMap<String, String>();
		for (String key : oMap.keySet()) {
			Object obj = oMap.get(key);
			// 这里是如果传进来的是date类的参数，那么我们手动给他转成字符串然后放进去�?
			if (obj instanceof Date) {
				try {
					obj = sdfDateTime.format(obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			map.put(key, String.valueOf(obj));
		}

		return map;
	}

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "啊蛋");
		map.put("age", 29);
		map.put("school", "清华大学");
		map.put("birthday", "2014-9-12");

		User user = (User) map2Object(map, User.class);
		// user.print();
	}
}
