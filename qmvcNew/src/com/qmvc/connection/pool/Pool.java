package com.qmvc.connection.pool;

import java.sql.Connection;

public interface Pool {

	public Connection getConnection();

	public  void clearConnection();
}
