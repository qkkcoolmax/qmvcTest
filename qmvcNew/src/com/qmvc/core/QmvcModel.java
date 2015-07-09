package com.qmvc.core;

import java.sql.Connection;

/**
 * 用户的model要繼承这个类�?
 * 这里的做法是去掉了dao层，直接将增删改查内置到Model中�?
 * */
import java.util.HashMap;
import java.util.Map;

import com.qmvc.db.Db;

public class QmvcModel {

	// 表中的属性都以键值对的形式存放在HashMap里面
	private Map<String, Object> attrs = new HashMap<String, Object>();

	// 用户new出一个model时，这个model对应的表名就会自动填充好。其实是从根据当前new出来的类的对象从全局常量中查到的名字�?
	private String tableName = QmvcConfig.CONSTANT.getTable().getTable(
			this.getClass());

	/**
	 * 保存操作�?
	 * 
	 * @return
	 */
	public int save() {
		// 可以看到，因為orm了，�?��用户不再直接从连接池中拿取连接�?用户可以直接在对象上使用增删改查，框架帮用户拿连接，
		// 可能是为了做事务，所以使用了threadlocal来保证一个线程（�?��请求）中的各类操作拿到的都是同一个连接�?
		int i = save(QmvcConfig.pool.getConnection());
		return i;
	}

	/**
	 * 保存操作
	 * 
	 * @return
	 */
	public int save(Connection conn) {

		int i = Db.save(tableName, this, conn);
		return i;
	}

	public Map<String, Object> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Object> attrs) {
		this.attrs = attrs;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 
	 * @param 字�
	 *            ��名称，与数据表要完全�?���?
	 * @param 字�
	 *            ���?
	 * */
	public void set(String attr, Object value) {
		attrs.put(attr, value);
	}

	public Object get(String attr) {
		return attrs.get(attr);
	}
}
