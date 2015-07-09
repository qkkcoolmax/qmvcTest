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
 * controller�ĸ���,�ڴ������Ƚ�����controller�п���Ҫ�õ������������úã�����ͨ�����佫��ע���ȥ��
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
	 * ��������ʹ�ã�ֱ���õ���������request��response
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
	 * ��װһЩ���õķ���������ʹ�á�
	 * 
	 * */

	// �����ṩһ��ֱ���������д���ַ����ķ�����
	protected final void renderString(String str) {

		try {
			getResponse().getWriter().write(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ��װһ�������������һ��html��̬ҳ�棬���o���ʹ�á�
	// ע�����url��������/������Ӧ�ø�Ŀ¼��Ȼ����ת��Ӧ���ڵ���Դ����ȥ

	protected final void renderHtmL(String viewPath) {
		try {
			getRequest().getRequestDispatcher(viewPath).forward(request,
					response);
			// ��дrequest�еķ����Լ���ת��Щ�������ڽӿ��У��ų�֧����Щ�淶����������ʵ����Щ������
			// ע����⣬�Ծ�̬��Դ��forwardʵ���������µ�����Ĭ�ϵ�servlet��tomcat�ṩ�������ؾ�̬�ļ�����
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��request�������������ԣ����ܽ�Ҫforward
	 * 
	 * */
	protected final void setAttrbute(String arg0, Object arg1) {
		getRequest().setAttribute(arg0, arg1);
	}

	/**
	 * 
	 * forward��jsp
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

	// ��װһ���ض��򷽷���

	protected final void redirect(String newUrl) {
		try {
			getResponse().sendRedirect(newUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * �ӵ�ǰ������ֱ�����ĳ��model��������ע��õ�model��
	 * 
	 * ��������ʹ�ã�������ഫ������mode�಻ƥ�䣬��ô����ע��ʧ�ܣ����ص���һ��Ĭ�ϳ�ʼ����model
	 * 
	 * */
	protected final Object getObject(Class<?> clazz) {
		Map<String, Object> queryMap = getMap();
		return MapKit.map2Object(queryMap, clazz);
	}

	// ��request�е��������mapת���ɺ��ʵ�map
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
	 * ��ȡ�ϴ��������ļ����˴���Ҫ���� commons-fileupload.jar��commons-io.jar
	 * 
	 * @param dirPath
	 * @param fileName
	 * @param maxSize
	 *            1024*1024*1M
	 * @return
	 */

	/**
	 * ע�⣬����Ū�����һ���£������������е����ݣ�tomcat���Ӻ���ġ������{��servlet�r��socketݔ������ָ�����Ո���w�_ʼ�ĵط��ġ�
	 * Ȼ�����������getParmeter����getInputStream��ô�����socket��ʣ�µĲ��ֿ�ʼ���ж�ȡ������
	 * 
	 * ����������ļ��A������post�����ģ��Ͳ���ֱ��ʹ��getP�������ǵò��������ģ������ֻ����ĵ�socket������ʹ��common��
	 * fileupload��ר�Ž��������ϴ��ļ�������
	 * 
	 * */
	protected final void getFile(String dirPath, String fileNamePrefix,
			int maxSize) {
		try {
			DiskFileItemFactory diskFactory = new DiskFileItemFactory();
			// threshold ���ޡ��ٽ�ֵ����Ӳ�̻��� 1M
			// ����������޵��ļ��ᱻ�ȴ洢����ʱ�ļ��С�
			diskFactory.setSizeThreshold(40 * 1024 * 1024);

			String temPath = "/tmp";
			File dir = new File(temPath);

			if (!dir.exists()) {
				dir.mkdirs();
			}

			System.out.println("��ʱ�ļ���·��" + temPath);
			// repository �����ң�����ʱ�ļ�Ŀ¼
			diskFactory.setRepository(new File(temPath));

			ServletFileUpload upload = new ServletFileUpload(diskFactory);
			// ���ö԰����ļ���http�������е�ͷ���Ľ����ַ�����
			upload.setHeaderEncoding(QmvcConfig.CONSTANT.getEncoding());
			// ���������ϴ�������ļ���С 4M 4 * 1024 * 1024
			upload.setSizeMax(maxSize);
			// ����HTTP������Ϣͷ
			// �����Զ���ķ�ʽ���ؽ���������ÿһ��������һЩ�����ļ�Ŷ��
			List<FileItem> fileItems = upload.parseRequest(request);

			Iterator<FileItem> iter = fileItems.iterator();

			List<Object> list = new ArrayList<Object>();

			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				// ������߰����԰�æ��request�н��ļ�����������ֳ�������
				// ����������а������ļ���ͬ�r�ְ����˅��������N�@Щ������getParmeter��ȡ��������
				// ֻ��������������������ļ������_��
				if (item.isFormField()) {
					// ������
					String name = item.getFieldName().trim();
					String value = item.getString(QmvcConfig.CONSTANT
							.getEncoding());
					paramMap.put(name, value);
					// ���ｫ��������������������úá�
				} else {
					// �ļ�����
					String type = StrKit.getEndType(item.getName(), "\\.");
					FileKit.saveFile(dirPath + fileNamePrefix + "." + type,
							item.getInputStream());

				}
				/**
				 * ���ｫ���ݱ��浽���ݿ�
				 */

			}
		} catch (Exception e) {
			e.getStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * ��ȡ�ϴ��������ļ����˴���Ҫ���� commons-fileupload.jar��commons-io.jar
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

			// threshold ���ޡ��ٽ�ֵ����Ӳ�̻��� 1M
			diskFactory.setSizeThreshold(40 * 1024 * 1024);
			String temPath = "/tmp";
			File dir = new File(temPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			System.out.println("��ʱ�ļ���·��" + temPath);
			// repository �����ң�����ʱ�ļ�Ŀ¼
			diskFactory.setRepository(new File(temPath));

			ServletFileUpload upload = new ServletFileUpload(diskFactory);

			upload.setHeaderEncoding(QmvcConfig.CONSTANT.getEncoding());
			// ���������ϴ�������ļ���С 4M 4 * 1024 * 1024
			upload.setSizeMax(maxSize);
			// ����HTTP������Ϣͷ
			List fileItems = upload.parseRequest(request);
			Iterator iter = fileItems.iterator();
			List<Object> list = new ArrayList<Object>();
			int count = 0;
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					// ������
					String name = item.getFieldName().trim();
					String value = item.getString(QmvcConfig.CONSTANT
							.getEncoding());
					paramMap.put(name, value);

				} else {
					// �ļ�����
					// �����ʾ�����ָ�����ļ�������ʵ���ϴ����ļ���������ôֻ������ǰ�ù����ļ����ˡ�
					String type = StrKit.getEndType(item.getName(), "\\.");
					if (count > (fileNamePrefix.length - 1)) {
						count = 0;
					}
					FileKit.saveFile(dirPath + fileNamePrefix[count] + "."
							+ type, item.getInputStream());
					count++;

				}
				/**
				 * ���ｫ���ݱ��浽���ݿ�
				 */

			}
		} catch (Exception e) {
			e.getStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * ��ȡ�ϴ��ļ���������ͨ����
	 * 
	 * @param attr
	 * @return
	 */
	protected final String getFormParm(String attr) {
		return paramMap.get(attr);
	}

	/**
	 * 
	 * ��String�ķ�ʽ��ȡ��ͨ����������������ϴ��ļ��Ĳ����޷�������ȡ
	 * 
	 * */
	protected final String getPar(String key) {
		return getRequest().getParameter(key);
	}

	/**
	 * �����ʹ����ʹ�ã��ṩ�ھ��ĵ��Č��󼯺ϡ�
	 * 
	 * 
	 * */
	protected final List<Online> getDocs() {
		return QmvcConfig.CONSTANT.getOnlinedocs();
	}

}
