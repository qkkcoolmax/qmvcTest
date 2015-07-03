package com.qmvc.kit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 该方法用于读取文件，保存文件�?
 * 
 * @author everxs
 * 
 */
public class FileKit {

	public static String charset = "utf-8";

	/**
	 * 保存�?��文件到指定路�?
	 * 
	 * @param savePath
	 */
	public static void saveFile(String savePath, InputStream inputStream) {
		FileOutputStream out = null;
		try {
			File file = new File(savePath);
			if (!file.exists())
				file.createNewFile();
			out = new FileOutputStream(file);
			int c;
			byte buffer[] = new byte[1024];
			while ((c = inputStream.read(buffer)) != -1) {
				for (int i = 0; i < c; i++)
					out.write(buffer[i]);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}

			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	/**
	 * 将旧文件拷贝到新文件目录
	 * 
	 * @param oldPath
	 * @param newPath
	 * @throws Exception
	 */
	public static void copyFile(String oldPath, String newPath) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(oldPath);
			File file = new File(newPath);
			if (!file.exists())
				file.createNewFile();
			out = new FileOutputStream(file);
			int c;
			byte buffer[] = new byte[1024];
			while ((c = in.read(buffer)) != -1) {
				for (int i = 0; i < c; i++)
					out.write(buffer[i]);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 读取文本文件格式的，返回String
	 * 
	 * @param filePath
	 * @return
	 */
	public static String readFile2String(String filePath) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, charset));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 读取文件，返回inputStream
	 * 
	 * @return
	 */
	public static InputStream readFile2InputStream(String filePath) {
		try {
			FileInputStream in = new FileInputStream(filePath);
			return in;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static void delete(File file) {
		if (file != null && file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					delete(files[i]);
				}
			}
			file.delete();
		}
	}

	/**
	 * 返回路径下的�?��file，并加入list
	 * 
	 * @return
	 */
	public static List<String> listFileAbsolutePath(String filePath,
			List<String> list) {

		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				/*
				 * 递归调用
				 */
				listFileAbsolutePath(file.getAbsolutePath(), list);
			} else {
				list.add(file.getAbsolutePath());

			}
		}
		return list;
	}

	/**
	 * 返回路径下的�?��file，并加入list
	 * 
	 * @return
	 */
	public static List<String> listClassFileAbsolutePath(String filePath) {

		List<String> list = new ArrayList<String>();
		List<String> classList = new ArrayList<String>();
		listFileAbsolutePath(filePath, list);

		for (String s : list) {
			if (s.endsWith(".class")) {
				// s = s.substring(filePath.length());
				String ss = s.substring(filePath.length() - 1);
				String prefix = ss.substring(0, ss.length() - 6).replace("\\",
						".");
				classList.add(prefix);
			}
		}
		return classList;
	}

	/**
	 * 加载文件
	 * 
	 * @param path
	 * @return
	 */
	public static Properties loadProperties(String path) {
		Properties prop = new Properties();
		InputStream in = FileKit.class.getResourceAsStream(path);
		if (in == null) {
			throw new RuntimeException("in为空");
		}

		try {
			prop.load(in);

		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
		return prop;
	}

}
