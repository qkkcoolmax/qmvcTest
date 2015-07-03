package com.qmvc.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.qmvc.Proxyfactory.ProxyFactory;
import com.qmvc.bean.factory.BeanFactory;
import com.qmvc.common.Constant;
import com.qmvc.connection.pool.JdbcConfig;
import com.qmvc.connection.pool.Pool;
import com.qmvc.container.Container;
import com.qmvc.kit.OnlineDocKit;
import com.qmvc.kit.ScanKit;
import com.qmvc.onlinedoc.Online;

/**
 * jvn的配置文件类
 * 
 * @author Administrator
 * 
 */
public abstract class JvnConfig {

	// 全局访问，信息库
	public static Constant CONSTANT = new Constant();
	private ActionHandle handel = new ActionHandle();
	public static BeanFactory beanfactory = new BeanFactory();
	public static Pool pool;// 用户config中的实现配置。
	public Container container = new Container();
	public static ProxyFactory pf = new ProxyFactory();

	/**
	 * 初始化参数
	 */
	public void init() {
		setInitParam(CONSTANT);
		setRoute(CONSTANT);
		ScanClass();
		LoadOnlineDoc();
	}

	/**
	 * 请在这里指定要使用的数据库连接池
	 * 
	 * 
	 * */
	public abstract void setInitParam(Constant constant);

	/**
	 * 设置路由
	 * 
	 * @param constant
	 */
	public abstract void setRoute(Constant constant);

	/**
	 * 配置连接池:暂时支持："com.jvn.connection.pool.DruidPool"
	 * */
	protected void setConnectionPoolSimply(JdbcConfig jdbdConfig,
			String poolName) {
		try {
			Class<?> clazz = Class.forName(poolName);
			Constructor<?> constr = clazz.getConstructor(JdbcConfig.class);
			pool = (Pool) constr.newInstance(jdbdConfig);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("设置连接池时出错");
		}

	}

	/**
	 * 这里会扫描controller和model了，分别会将controller的路由表和model映射表设置到全局常量中。
	 * 
	 * */
	private void ScanClass() {

		ScanKit.scanClass(CONSTANT);

	}

	public ActionHandle getHandel() {
		return handel;
	}

	/**
	 * 加载在线文档，扫描，然后把扫描出来的接口全部放入常量类中。
	 * 
	 * */
	private void LoadOnlineDoc() {
		List<Online> list = OnlineDocKit.loadClass(this.CONSTANT.getRouteMap());
		System.out.println("list:" + list.size());
		CONSTANT.setOnlinedocs(list);

	}

}
