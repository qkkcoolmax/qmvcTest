package com.qmvc.core;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.qmvc.kit.FileKit;
import com.qmvc.kit.MapKit;
import com.qmvc.kit.StrKit;
import com.qmvc.onlinedoc.Online;

/**
 * controller的父类,在此类中先将子类controller中可能要用到两个对象设置好，后面通过反射将其注入进去。
 * 
 * @author Administrator
 * 
 */
public class QmvcController {

	private HttpServletRequest request;

	private HttpServletResponse response;

	/*
	 * private SimpleDateFormat SDF_DATE_TIME = new SimpleDateFormat(
	 * "yyyy-MM-dd HH:mm:ss"); private SimpleDateFormat SDF_DATE = new
	 * SimpleDateFormat("yyyy-MM-dd");
	 */

	private Map<String, String> paramMap = new HashMap<String, String>();

	/**
	 * 
	 * 由子类来使用，直接拿到传进来的request和response
	 * 
	 * */

	protected final HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	protected final HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * 封装一些常用的方法供子类使用。
	 * 
	 * */

	// 父类提供一个直接向浏览器写回字符串的方法，
	protected final void renderString(String str) {

		try {
			getResponse().getWriter().write(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 封装一个向浏览器返回一个html静态页面，供給子類使用。
	// 注意这个url必须是用/来代表应用根目录，然後跳转到应用内的资源里面去

	protected final void renderHtmL(String viewPath) {
		try {
			getRequest().getRequestDispatcher(viewPath).forward(request,
					response);
			// 这写request中的方法以及跳转这些都定义在接口中，号称支持这些规范的容器必须实现这些方法。
			// 注意理解，对静态资源的forward实际上是重新调用了默认的servlet（tomcat提供）来返回静态文件流。
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向request的域中设置属性，可能将要forward
	 * 
	 * */
	protected final void setAttrbute(String arg0, Object arg1) {
		getRequest().setAttribute(arg0, arg1);
	}

	/**
	 * 
	 * forward到jsp
	 * 
	 * */
	public final void renderJsp(String viewPath) {
		try {
			getRequest().getRequestDispatcher(viewPath).forward(request,
					response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 封装一个重定向方法，

	protected final void redirect(String newUrl) {
		try {
			getResponse().sendRedirect(newUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 从当前请求中直接填充某个model，并返回注入好的model。
	 * 
	 * 由子类来使用，如果子类传进来的mode类不匹配，那么将会注入失败，返回的是一个默认初始化的model
	 * 
	 * */
	protected final Object getObject(Class<?> clazz) {
		Map<String, Object> queryMap = getMap();
		return MapKit.map2Object(queryMap, clazz);
	}

	// 将request中的请求参数map转换成合适的map
	private Map<String, Object> getMap() {
		Map<String, Object> tmp = new HashMap();
		Map<String, String[]> map = (Map<String, String[]>) getRequest()
				.getParameterMap();
		for (String key : map.keySet()) {
			tmp.put(key, map.get(key)[0]);
		}
		return tmp;

	}

	/**
	 * 获取上传过来的文件，此处需要增加 commons-fileupload.jar，commons-io.jar
	 * 
	 * @param dirPath
	 * @param fileName
	 * @param maxSize
	 *            1024*1024*1M
	 * @return
	 */

	/**
	 * 注意，这里弄清楚了一件事，对于请求体中的数据，tomcat是延后处理的。所以調用servlet時，socket輸入流的指針是在請求體開始的地方的。
	 * 然后如果调用了getParmeter或者getInputStream那么都会对socket中剩下的部分开始进行读取解析。
	 * 
	 * 但是如果是文件夾帶参数post过来的，就不能直接使用getP，那样是得不到参数的，但是又会消耗掉socket，所以使用common。
	 * fileupload来专门解析这种上传文件的请求。
	 * 
	 * */
	protected final void getFile(String dirPath, String fileNamePrefix,
			int maxSize) {
		try {
			DiskFileItemFactory diskFactory = new DiskFileItemFactory();
			// threshold 极限、临界值，即硬盘缓存 1M
			// 大于这个门限的文件会被先存储到临时文件夹。
			diskFactory.setSizeThreshold(40 * 1024 * 1024);

			String temPath = "/tmp";
			File dir = new File(temPath);

			if (!dir.exists()) {
				dir.mkdirs();
			}

			System.out.println("临时文件夹路径" + temPath);
			// repository 贮藏室，即临时文件目录
			diskFactory.setRepository(new File(temPath));

			ServletFileUpload upload = new ServletFileUpload(diskFactory);
			// 设置对包含文件的http请求体中的头部的解析字符集。
			upload.setHeaderEncoding(QmvcConfig.CONSTANT.getEncoding());
			// 设置允许上传的最大文件大小 4M 4 * 1024 * 1024
			upload.setSizeMax(maxSize);
			// 解析HTTP请求消息头
			// 这里以对象的方式返回解析出来的每一个表单域，有一些域是文件哦。
			List<FileItem> fileItems = upload.parseRequest(request);

			Iterator<FileItem> iter = fileItems.iterator();

			List<Object> list = new ArrayList<Object>();

			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				// 这个工具包可以帮忙从request中将文件和请求参数分出来，。
				// 因為如果表單中包含了文件，同時又包含了參數，那麼這些參數用getParmeter就取不出來咯。
				// 只有先用这个包将参数和文件流分開。
				if (item.isFormField()) {
					// 表单内容
					String name = item.getFieldName().trim();
					String value = item.getString(QmvcConfig.CONSTANT
							.getEncoding());
					paramMap.put(name, value);
					// 这里将解析出来请求参数先设置好。
				} else {
					// 文件内容
					String type = StrKit.getEndType(item.getName(), "\\.");
					FileKit.saveFile(dirPath + fileNamePrefix + "." + type,
							item.getInputStream());

				}
				/**
				 * 这里将数据保存到数据库
				 */

			}
		} catch (Exception e) {
			e.getStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取上传过来的文件，此处需要增加 commons-fileupload.jar，commons-io.jar
	 * 
	 * @param dirPath
	 * @param fileName
	 * @param maxSize
	 *            1024*1024*1M
	 * @return
	 */
	protected final void getFiles(String dirPath, String[] fileNamePrefix,
			int maxSize) {
		try {
			DiskFileItemFactory diskFactory = new DiskFileItemFactory();

			// threshold 极限、临界值，即硬盘缓存 1M
			diskFactory.setSizeThreshold(40 * 1024 * 1024);
			String temPath = "/tmp";
			File dir = new File(temPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			System.out.println("临时文件夹路径" + temPath);
			// repository 贮藏室，即临时文件目录
			diskFactory.setRepository(new File(temPath));

			ServletFileUpload upload = new ServletFileUpload(diskFactory);

			upload.setHeaderEncoding(QmvcConfig.CONSTANT.getEncoding());
			// 设置允许上传的最大文件大小 4M 4 * 1024 * 1024
			upload.setSizeMax(maxSize);
			// 解析HTTP请求消息头
			List fileItems = upload.parseRequest(request);
			Iterator iter = fileItems.iterator();
			List<Object> list = new ArrayList<Object>();
			int count = 0;
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					// 表单内容
					String name = item.getFieldName().trim();
					String value = item.getString(QmvcConfig.CONSTANT
							.getEncoding());
					paramMap.put(name, value);

				} else {
					// 文件内容
					// 这里表示如果你指定的文件名少于实际上传的文件个数，那么只好用以前用过的文件名了。
					String type = StrKit.getEndType(item.getName(), "\\.");
					if (count > (fileNamePrefix.length - 1)) {
						count = 0;
					}
					FileKit.saveFile(dirPath + fileNamePrefix[count] + "."
							+ type, item.getInputStream());
					count++;

				}
				/**
				 * 这里将数据保存到数据库
				 */

			}
		} catch (Exception e) {
			e.getStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取上传文件，表单的普通参数
	 * 
	 * @param attr
	 * @return
	 */
	protected final String getFormParm(String attr) {
		return paramMap.get(attr);
	}

	/**
	 * 
	 * 以String的方式获取普通请求参数，包含了上传文件的参数无法这样获取
	 * 
	 * */
	protected final String getPar(String key) {
		return getRequest().getParameter(key);
	}

	/**
	 * 供框架使用者使用，提供在線文旦的對象集合。
	 * 
	 * 
	 * */
	protected final List<Online> getDocs() {
		return QmvcConfig.CONSTANT.getOnlinedocs();
	}

}
