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
 * jvn�������ļ���
 * 
 * @author Administrator
 * 
 */
public abstract class QmvcConfig {

	// ȫ�ַ��ʣ���Ϣ��
	public static Constant CONSTANT = new Constant();
	private ActionHandle handel = new ActionHandle();
	public static BeanFactory beanfactory = new BeanFactory();
	public static Pool pool;// �û�config�е�ʵ�����á�
	public Container container = new Container();
	public static ProxyFactory pf = new ProxyFactory();

	/**
	 * ��ʼ������
	 */
	public void init() {
		setInitParam(CONSTANT);
		setRoute(CONSTANT);
		ScanClass();
		LoadOnlineDoc();
	}

	/**
	 * ��������ָ��Ҫʹ�õ���ݿ����ӳ�
	 * 
	 * 
	 * */
	public abstract void setInitParam(Constant constant);

	/**
	 * ����·��
	 * 
	 * @param constant
	 */
	public abstract void setRoute(Constant constant);

	/**
	 * �������ӳ�:��ʱ֧�֣�"com.jvn.connection.pool.DruidPool"
	 * */
	protected void setConnectionPoolSimply(JdbcConfig jdbdConfig,
			String poolName) {
		try {
			Class<?> clazz = Class.forName(poolName);
			Constructor<?> constr = clazz.getConstructor(JdbcConfig.class);
			pool = (Pool) constr.newInstance(jdbdConfig);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�������ӳ�ʱ����");
		}

	}

	/**
	 * �����ɨ��controller��model�ˣ��ֱ�Ὣcontroller��·�ɱ��modelӳ������õ�ȫ�ֳ����С�
	 * 
	 * */
	private void ScanClass() {

		ScanKit.scanClass(CONSTANT);

	}

	public ActionHandle getHandel() {
		return handel;
	}

	/**
	 * ���������ĵ���ɨ�裬Ȼ���ɨ������Ľӿ�ȫ�����볣�����С�
	 * 
	 * */
	private void LoadOnlineDoc() {
		List<Online> list = OnlineDocKit.loadClass(this.CONSTANT.getRouteMap());
		System.out.println("list:" + list.size());
		CONSTANT.setOnlinedocs(list);

	}

}
