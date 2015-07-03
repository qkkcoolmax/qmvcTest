package com.qmvc.db;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.qmvc.core.JvnConfig;
import com.qmvc.core.JvnModel;
import com.qmvc.kit.MapKit;
import com.qmvc.kit.PrintKit;

/**
 * 数据库查询�?用类，内置在model中的增删改查的实际�?辑在这里实现，就像是�?��工具类�?
 * 
 * @author everxs
 * 
 */
public class Db {

	/**
	 * 保存操作
	 * 
	 * @return
	 */
	public static int save(String tableName, JvnModel model) {
		return save(tableName, model, JvnConfig.pool.getConnection());
	}

	/**
	 * 保存操作
	 * 
	 * @return
	 * 
	 *         insert into tableName (xxx,xxx) values ('a','b'); insert into
	 *         tableName (xxx,xxx) values ( ? , ?);
	 * 
	 */
	public static int save(String tableName, JvnModel model, Connection conn) {
		int result = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String keys = "";
			String wenhao = "";
			// 查看要insert的这条记录传入了多少个参数，,构建�?��字符串数组，准备放置这些值�?
			Object values[] = new String[model.getAttrs().size()];

			int i = 0;
			// 将mode中开发�?设置的参数转换成String类型的，以方便用来给动�?sql传�?参数�?
			Map<String, String> strMap = MapKit.toStringMap(model.getAttrs());

			// 將参数读出来，构造成�?��动�?sql语句，同時將实际的�?放入�?��用来传�?的数据�?
			for (String attr : strMap.keySet()) {
				keys = keys + attr + ",";
				wenhao = wenhao + "?,";
				values[i] = strMap.get(attr);
				i++;
			}

			// 去掉�?���?��逗号
			if (keys.endsWith(",")) {
				keys = keys.substring(0, keys.length() - 1);
			}
			// 去掉多出来的逗号
			if (wenhao.endsWith(",")) {
				wenhao = wenhao.substring(0, wenhao.length() - 1);
			}

			// 拼接处动态sql语句�?
			String sql = "insert into " + tableName + "(" + keys + ")values("
					+ wenhao + ")";

			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// 动�?传参，全部以字符串的形式
			for (i = 0; i < values.length; i++) {
				ps.setObject(i + 1, values[i]);
				// 传参是从1�?��的�?
			}
			// 执行操作
			result = ps.executeUpdate();
			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				// 知其仅有�?��，故获取第一�?
				Long id = rs.getLong(1);
				model.set("id", id);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			// 注意即使要做事务，没执行�?��也是要关闭结果集，声明的，只是说connection不能断�?
			try {
				// 如果连接部位空或者连接是自动提交的，也就是说没有打算拿来做事务，那么就还给线程池，即save�?��已经自动提交了，可以把连接还给线程池.下次要用再拿�?
				if (conn != null && conn.getAutoCommit()) {
					// 这里表示conn是自动提交，�?��到这里事务已经结束，要将线程的保险箱中的连接也清理掉�?
					conn.close();// 这个close应该被重写了，并不是关闭连接，�?是将连接交还给连接池，连接池可以将其交给其他人用�?
					JvnConfig.pool.clearConnection();
					// 如果这里用户忘记关闭了，那么强大的一点的连接池会自行回收久了没用的连接�?
				}

				//如果不是自动提交，那么就不执行close也就是不归还这个连接先�?
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	public static int delete() {

		return 0;
	}

}
