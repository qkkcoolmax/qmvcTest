package com.qmvc.connection.pool;

import java.sql.Connection;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidConnectionHolder;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

/**
 * druid连接池，框架內置是支持�?個连接池的�?这里对阿里巴巴的这个连接池进行一个简单的封装�?
 * 
 * 
 * @author everxs
 * 
 */
public class DruidPool implements Pool {

	private static DruidDataSource dataSource;
	// 使用threadlocal的目的应该是为了以后实现事务做准备�?
	private final static ThreadLocal<Connection> connections = new ThreadLocal<Connection>();

	/**
	 * 创建好druid
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
		// 启用监控统计功能
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
	 * 获取�?��连接
	 * 
	 * 从连接池中拿取一个连接，这里对连接池做了�?��封装，将拿到的连接池同时放入了当前线程的保险箱中�?
	 * 保证了一个线程如果需要多次获得connetion的话，永远使用的是同�?��接，为事务做准备�?
	 * 
	 * 為什麼加同步�?
	 * 
	 * @return
	 */
	public synchronized Connection getConnection() {

		Connection conn = connections.get();

		System.out.println("进入 �? + conn");

		try {
			// 這裡判斷，如果当前线程第�?��或取连接或�?之前那个连接已经被关闭了，没办法只好重新获取连接�?

			if (conn == null || conn.isClosed()) {
				conn = dataSource.getConnection();
				System.out.println("null 后：" + conn);
				connections.set(conn);
			}

		} catch (Exception e) {

			throw new RuntimeException(e);
		}
		return conn;
	}

	/**
	 * 
	 * 是不是在每次交还线程时调用这个方法让线程也從threadlocal中清除�?
	 * 
	 * */
	public synchronized void clearConnection() {
		connections.set(null);
	}
}
