package com.qmvc.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qmvc.core.JvnController;
import com.qmvc.core.JvnTable;
import com.qmvc.onlinedoc.Online;

/**
 * jvn��ܵ���Ϣ��
 * 
 * @author Administrator
 * 
 * 
 *         ��֤��װ�ԣ���get��set����¶��
 */
public class Constant {
	// ·����Ϣ
	private Map<String, Class> routeMap = new HashMap<String, Class>();
	private String encoding = "utf-8";// Ĭ�ϱ����ʽ
	private List<Online> onlinedocs;
	// ���ݿ�����ָ����ֽ����ӳ��
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
	 * �õ�һ��Controller
	 * 
	 * @param route
	 * @return
	 */

	public Class getRoute(String route) {
		return routeMap.get(route);
	}

	/**
	 * ����һ��·��
	 * 
	 * @param route
	 * @param clazz
	 * 
	 *            ʹ��һ�����ͣ����޶�Ҫput�M���ֵ����^���˿�ܵ�JvnController
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
