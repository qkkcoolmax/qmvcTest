package com.qmvc.kit;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import com.qmvc.annotation.Aop;
import com.qmvc.annotation.Controller;
import com.qmvc.annotation.Model;
import com.qmvc.annotation.Service;
import com.qmvc.common.Constant;

/**
 * 扫描�?
 * 
 * @author Administrator
 * 
 */
public class ScanKit {

	/**
	 * 注意tomcat加载webapp下的lib中的�?方包仍然是用的webapp加载器，和web应用使用的是�?��加载器�?
	 * 每个应用的webapp加载器加载的是他的webapp下的/WEB-INF/下的类�?�?��包括classes和lib下的类�?
	 * 
	 * 
	 * tomcat第一次得到一个请求时，开启一个线程（设置线程上下文加载器为webapp�?
	 * 线程中会使用webapp加载器加载相应webapp的filter，当然这个filter是框架提供的filter.而我们没�?
	 * 把框架包放到公共lib中的
	 * �?那么�?��加载这个filter的就确实会是webapp加载�?然後此時線程上下文的加載器應該也被tomcat设置为了
	 * 对应的webapp加载�?
	 * 如果我�?把框架包放到公共lib下了,那在使用webapp加載框架filter時會發現filter已经被父级加载器加载过了,�?��直接使用.
	 * 调用其相应的方法,如果这时框架源码中想要获取用户的资源就只能先通過线程上下文加载器来获得webapp加載�?然后再用
	 * 這�?加載器來尋找并加載用户的类，不然可能找不到�?因为此时直接使用class.forname加载類使用的類加載器是父级加载器�?
	 * 
	 * 框架放到lib包一般不会出問題�?
	 * */
	public static void scanClass(Constant constant) {
		// 拿到classes绝对路劲
		/*
		 * System.out.println(DiskFileItemFactory.class.getClassLoader().getResource
		 * (""));
		 */
		// 這裡如果直接用框架某個類的加載器來獲得路徑的话，路径可能是不对的，拿到的不是classpath。扫描出来的类也就不对�?
		String path = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath();
		// 拿实际上下面加载用户类时也应该用webapp加载器加载才保险。否则找不到对应的用户类�?
		// 因為class。forname默认使用的调用位置的类加载器来加载�?

		// tomcat应该设置过当前执行这个方法的线程为webapp加载器�?
		// 这里�?��使用线程上下文加载器，拿到tomcat中加载web应用的类加载器，这样才能获取到正确的classpath路径�?
		// String path =
		// ScanKit.class.getClassLoader().getResource("").getPath();
		// 得到类的全名�?例如�?con.everxs.test.TestController.class
		List<String> listClass = FileKit.listClassFileAbsolutePath(path);
		// 拿到用户路径下的�?��类的名字（全限定名）
		for (String clazzStr : listClass) {
			try {
				// 找到这个类全名称的Class
				// 首次申請使用的是加载scanClass类的类加载器�?
				// 这里其实�?��也使用線程上下文加載器來加載�?
				Class clazz = Thread.currentThread().getContextClassLoader()
						.loadClass(clazzStr);
				// Class clazz = Class.forName(clazzStr);
				System.out.println("加载�? + clazz.getName()");

				if (clazz != null) {
					Controller controller = (Controller) clazz
							.getAnnotation(Controller.class);
					Model model = (Model) clazz.getAnnotation(Model.class);
					Service serv = (Service) clazz.getAnnotation(Service.class);
					if (controller != null) {
						constant.setRoute(controller.space(), clazz);
					}
					if (model != null) {
						// filter初始化的时�?，执行这里，扫描到用户所有的model，将modelclass和表名的映射放置到常量中�?
						constant.getTable().setTable(clazz, model.tablename());
					}
					if (serv != null) {
						constant.addClass(clazz);
					}

				}
			} catch (Exception e) {
				System.out.println("找不到类文件");
			}
		}
	}
}
