package com.qmvc.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库表配置�?
 * 代表用戶用户应用中的全部�?
 * 
 * 
 * 
 * @author everxs
 *
 */
public class JvnTable {

	//装载数据库表
	private Map<Class,String> tableMap = new HashMap<Class,String>();


	
	public Map<Class,String> getTableMap() {
		return tableMap;
	}



	public void setTableMap(Map<Class,String> tableMap) {
		this.tableMap = tableMap;
	}



	public void setTable(Class tableClass,String tableName){
		tableMap.put(tableClass,tableName);
	}
	
	public String getTable(Class tableClass){
		return tableMap.get(tableClass);
	}
	
	
	
	
	
	
}
