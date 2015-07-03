package com.qmvc.connection.pool;

/**
 * jbdc参数
 * 
 * @author Administrator
 * 
 *         提供个开发�?的设置连接池的jdbc配置bean�?
 */
public class JdbcConfig {

	private String driver;
	private String user;
	private String password;
	private String jdbcUrl;

	private int initialSize;
	private int maxActive;
	private long maxWait;

	public JdbcConfig() {

	}

	public JdbcConfig(String driver, String user, String password,
			String jdbcUrl) {
		super();
		this.driver = driver;
		this.user = user;
		this.password = password;
		this.jdbcUrl = jdbcUrl;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

}
