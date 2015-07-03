package com.qmvc.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qmvc.core.JvnController;
import com.qmvc.core.JvnTable;
import com.qmvc.onlinedoc.Online;

/**
 * jvn框架的信息库
 * 
 * @author Administrator
 * 
 * 
 *         保证封装性，用get和set来暴露。
 */
public class Constant {
	// 路由信息
	private Map<String, Class> routeMap = new HashMap<String, Class>();
	private String encoding = "utf-8";// 默认编码格式
	private List<Online> onlinedocs;
	// 数据库表名字跟类字节码的映射
	private JvnTable table = new JvnTable();
	private List<Class> servClassList = new ArrayList<Class>();


	public List<Class> getServClassList() {
		return servClassList;
	}

	public List<Online> getOnlinedocs() {
		return onlinedocs;
	}

	public void setOnlinedocs(List<Online> onlinedocs) {
		this.onlinedocs = onlinedocs;
	}

	/**
	 * 得到一个Controller
	 * 
	 * @param route
	 * @return
	 */

	public Class getRoute(String route) {
		return routeMap.get(route);
	}

	/**
	 * 设置一个路由
	 * 
	 * @param route
	 * @param clazz
	 * 
	 *            使用一个泛型，来限定要put進來的值必須繼承了框架的JvnController
	 */
	public void setRoute(String route, Class<? extends JvnController> clazz) {
		routeMap.put(route, clazz);
	}

	public Map<String, Class> getRouteMap() {
		return routeMap;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public JvnTable getTable() {
		return table;
	}

	public void setTable(JvnTable table) {
		this.table = table;
	}
	
	public void  addClass(Class clazz){
		this.servClassList.add(clazz);
	}
	
	
}
