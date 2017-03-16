package com.util.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SysParams {

	/**
	 * 获取工程根路径，如果是打包成jar，就返回jar所在路径
	 * 
	 * @return String
	 */
	public static String getJarRootDir() {
		return new File("").getAbsolutePath();
	}

	/**
	 * 读取打包好的jar文件内指定路径的文件
	 * 
	 * @param dir
	 *            jar文件内文件路径
	 * @return InputStream
	 * @throws IOException
	 */
	public static InputStream getJarInnerFile(String url) throws IOException {
		InputStream instream = null;
		String sdir = getClassesDir();
		sdir = URLDecoder.decode(
				sdir.substring("jar:file:/".length(), sdir.indexOf("!")),
				"UTF-8");
		JarFile jarFile = new JarFile(sdir);// 读入jar文件
		JarEntry entry = jarFile.getJarEntry(url);
		instream = jarFile.getInputStream(entry);
		jarFile.close();
		return instream;
	}

	/**
	 * 获取Classes的根路径
	 * 
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String getClassesDir() throws UnsupportedEncodingException {
		String sdir = String.valueOf(ClassLoader
				.getSystemResource("log4j.properties"));
		sdir = URLDecoder.decode(
				sdir.substring("jar:file:/".length(), sdir.indexOf("!")),
				"UTF-8");
		return sdir;
	}

	/**
	 * 获取Class所在绝对全路径（含包路径)
	 * 
	 * @param class Class对象
	 * @return String
	 */
	public static String getClassDir(Class<?> clazz) {
		return clazz.getResource("").getPath() + clazz.getSimpleName();
	}

}
