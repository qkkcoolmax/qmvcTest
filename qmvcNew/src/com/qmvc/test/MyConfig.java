package com.qmvc.test;

import com.qmvc.common.Constant;
import com.qmvc.connection.pool.JdbcConfig;
import com.qmvc.core.QmvcConfig;

public class MyConfig extends QmvcConfig {

	@Override
	public void setInitParam(Constant constant) {

		JdbcConfig jdbc = new JdbcConfig();
		jdbc.setUser("root");
		jdbc.setPassword("root");
		jdbc.setDriver("com.mysql.jdbc.Driver");
		jdbc.setJdbcUrl("jdbc:mysql://127.0.0.1/test?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull");
		jdbc.setInitialSize(3);
		jdbc.setMaxActive(5);
		jdbc.setMaxWait(60000);
		setConnectionPoolSimply(jdbc, "com.jvn.connection.pool.DruidPool");
	}

	/**
	 * ����·�ɣ�ֻ��Ҫ�O��·����action�Ķ�Ӧ��ϵ�����ʵľ��巽��ֱ����action�з�������ͬ���ɡ�
	 * 
	 */
	@Override
	public void setRoute(Constant constant) {
		/* constant.setRoute("/user", UserController.class); */
	}

}
