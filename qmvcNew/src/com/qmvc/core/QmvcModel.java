package com.qmvc.core;

import java.sql.Connection;

/**
 * 鐢ㄦ埛鐨刴odel瑕佺辜鎵胯繖涓被锛?
 * 杩欓噷鐨勫仛娉曟槸鍘绘帀浜哾ao灞傦紝鐩存帴灏嗗鍒犳敼鏌ュ唴缃埌Model涓?
 * */
import java.util.HashMap;
import java.util.Map;

import com.qmvc.db.Db;

public class QmvcModel {

	// 琛ㄤ腑鐨勫睘鎬ч兘浠ラ敭鍊煎鐨勫舰寮忓瓨鏀惧湪HashMap閲岄潰
	private Map<String, Object> attrs = new HashMap<String, Object>();

	// 鐢ㄦ埛new鍑轰竴涓猰odel鏃讹紝杩欎釜model瀵瑰簲鐨勮〃鍚嶅氨浼氳嚜鍔ㄥ～鍏呭ソ銆傚叾瀹炴槸浠庢牴鎹綋鍓峮ew鍑烘潵鐨勭被鐨勫璞′粠鍏ㄥ眬甯搁噺涓煡鍒扮殑鍚嶅瓧銆?
	private String tableName = QmvcConfig.CONSTANT.getTable().getTable(
			this.getClass());

	/**
	 * 淇濆瓨鎿嶄綔锛?
	 * 
	 * @return
	 */
	public int save() {
		// 鍙互鐪嬪埌锛屽洜鐐簅rm浜嗭紝鎵?互鐢ㄦ埛涓嶅啀鐩存帴浠庤繛鎺ユ睜涓嬁鍙栬繛鎺ャ?鐢ㄦ埛鍙互鐩存帴鍦ㄥ璞′笂浣跨敤澧炲垹鏀规煡锛屾鏋跺府鐢ㄦ埛鎷胯繛鎺ワ紝
		// 鍙兘鏄负浜嗗仛浜嬪姟锛屾墍浠ヤ娇鐢ㄤ簡threadlocal鏉ヤ繚璇佷竴涓嚎绋嬶紙涓?釜璇锋眰锛変腑鐨勫悇绫绘搷浣滄嬁鍒扮殑閮芥槸鍚屼竴涓繛鎺ャ?
		int i = save(QmvcConfig.pool.getConnection());
		return i;
	}

	
	
	/**
	 * new一个对象，设置他的id，然后就可以调用这个方法，然后就会将model中填充起来。
	 * 
	 * 
	 * */
	
	public void queryIdForObject() {
		Db.queryIdforObject(tableName, this);
	}

	/**
	 * 淇濆瓨鎿嶄綔
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

	public void clearAttr() {
		this.attrs.clear();
	}

	/**
	 * 
	 * @param 瀛楁
	 *            鍚嶇О锛屼笌鏁版嵁琛ㄨ瀹屽叏涓?嚧銆?
	 * @param 瀛楁
	 *            鍊?
	 * */
	public void set(String attr, Object value) {
		attrs.put(attr, value);
	}

	public Object get(String attr) {
		return attrs.get(attr);
	}
}
