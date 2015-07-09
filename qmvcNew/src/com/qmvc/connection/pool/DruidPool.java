package com.qmvc.connection.pool;

import java.sql.Connection;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidConnectionHolder;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

/**
 * druid杩炴帴姹狅紝妗嗘灦鍏х疆鏄敮鎸侊拷?鍊嬭繛鎺ユ睜鐨勶拷?杩欓噷瀵归樋閲屽反宸寸殑杩欎釜杩炴帴姹犺繘琛屼竴涓畝鍗曠殑灏佽锟�
 * 
 * 
 *
 * 
 */
public class DruidPool implements Pool {

	private static DruidDataSource dataSource;
	// 浣跨敤threadlocal鐨勭洰鐨勫簲璇ユ槸涓轰簡浠ュ悗瀹炵幇浜嬪姟鍋氬噯澶囷拷?
	private final static ThreadLocal<Connection> connections = new ThreadLocal<Connection>();

	/**
	 * 鍒涘缓濂絛ruid
	 * 
	 * @param jdbcConfig
	 */
	public DruidPool(JdbcConfig jdbcConfig) {
		dataSource = new DruidDataSource();
		dataSource.setDriverClassName(jdbcConfig.getDriver());
		dataSource.setUsername(jdbcConfig.getUser());
		dataSource.setPassword(jdbcConfig.getPassword());
		dataSource.setUrl(jdbcConfig.getJdbcUrl());
		dataSource.setPoolPreparedStatements(true);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
		dataSource.setInitialSize(jdbcConfig.getInitialSize());
		dataSource.setMinIdle(1);
		dataSource.setMaxActive(jdbcConfig.getMaxActive());
		dataSource.setMaxWait(jdbcConfig.getMaxWait());
		// 鍚敤鐩戞帶缁熻鍔熻兘
		try {
			dataSource.setFilters("stat");
		} catch (SQLException e) {

			e.printStackTrace();
		}// for mysql
		dataSource.setPoolPreparedStatements(false);
	}

	public DruidPool() {
	}

	/**
	 * 鑾峰彇锟�锟斤拷杩炴帴
	 * 
	 * 浠庤繛鎺ユ睜涓嬁鍙栦竴涓繛鎺ワ紝杩欓噷瀵硅繛鎺ユ睜鍋氫簡锟�锟斤拷灏佽锛屽皢鎷垮埌鐨勮繛鎺ユ睜鍚屾椂鏀惧叆浜嗗綋鍓嶇嚎绋嬬殑淇濋櫓绠变腑锟�
	 * 淇濊瘉浜嗕竴涓嚎绋嬪鏋滈渶瑕佸娆¤幏寰梒onnetion鐨勮瘽锛屾案杩滀娇鐢ㄧ殑鏄悓锟�锟斤拷鎺ワ紝涓轰簨鍔″仛鍑嗗锟�
	 * 
	 * 鐐轰粈楹煎姞鍚屾锟�
	 * 
	 * @return
	 */
	public synchronized Connection getConnection() {

		Connection conn = connections.get();

		System.out.println("杩涘叆 锟� + conn");

		try {
			// 閫欒！鍒ゆ柗锛屽鏋滃綋鍓嶇嚎绋嬬锟�锟斤拷鎴栧彇杩炴帴鎴栵拷?涔嬪墠閭ｄ釜杩炴帴宸茬粡琚叧闂簡锛屾病鍔炴硶鍙ソ閲嶆柊鑾峰彇杩炴帴锟�

			if (conn == null || conn.isClosed()) {
				conn = dataSource.getConnection();
				System.out.println("null 鍚庯細" + conn);
				connections.set(conn);
			}

		} catch (Exception e) {

			throw new RuntimeException(e);
		}
		return conn;
	}

	/**
	 * 
	 * 鏄笉鏄湪姣忔浜よ繕绾跨▼鏃惰皟鐢ㄨ繖涓柟娉曡绾跨▼涔熷緸threadlocal涓竻闄わ拷?
	 * 
	 * */
	public synchronized void clearConnection() {
		connections.set(null);
	}
}
