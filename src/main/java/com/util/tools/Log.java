package com.util.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

public class Log {

	private String propertiesFileDir;
	private String logFileDir;
	private String logType;
	private URL logConfigPathUrl;
	private Logger log;

	public Log() throws IOException {
		propertiesFileDir = "";
		logFileDir = "";
		logType = "";
		init();
	}

	public Log(String propertiesFileDir, String logFileDir, String logType)
			throws IOException {
		this.propertiesFileDir = propertiesFileDir;
		this.logFileDir = logFileDir;
		this.logType = logType;
		init();
	}

	@SuppressWarnings("deprecation")
	public void init() throws IOException {
		String dir = "";
		if ("".equals(logFileDir)) {
			File directory = new File("");
			try {
				dir = directory.getCanonicalPath();// 获取标准的路径
			} catch (Exception e) {
			}
		} else
			dir = logFileDir;
		System.setProperty("WORKDIR", dir);
		if ("".equals(propertiesFileDir)) {
			logConfigPathUrl = ClassLoader
					.getSystemResource("log4j.properties");
		} else
			logConfigPathUrl = new File(propertiesFileDir).toURL();
		if ("".equals(logType))
			logType = "data";
		org.apache.log4j.PropertyConfigurator.configure(logConfigPathUrl);
		initLogDir("log4j.appender." + logType + ".File");
		this.log = Logger.getLogger(logType);
	}

	public void pError(String str) {
		log.error(str);
	}

	public void pDebug(String str) {
		log.debug(str);
	}

	public void pInfo(String str) {
		log.info(str);
	}

	private void initLogDir(String dirProperty) throws IOException {
		// 判断日志路径是否存在，如果不存在就创建
		InputStream istream = null;
		Properties props = new Properties();
		JarFile jarFile = null;
		if ("".equals(propertiesFileDir)) {
			String sdir = String.valueOf(ClassLoader
					.getSystemResource("log4j.properties"));
			sdir = URLDecoder.decode(sdir, "UTF-8");
			if (!sdir.contains("jar:file:/")) {
				sdir = sdir.substring("file:/".length());
				istream = new FileInputStream(new File(sdir));
			} else {
				sdir = sdir.substring("jar:file:/".length(), sdir.indexOf("!"));
				Public.p(sdir);
				jarFile = new JarFile(sdir);// 读入jar文件
				JarEntry entry = jarFile.getJarEntry("log4j.properties");
				istream = jarFile.getInputStream(entry);
			}
		} else
			istream = new FileInputStream(URLDecoder.decode(
					logConfigPathUrl.getPath(), "UTF-8"));
		props.load(istream);
		istream.close();
		if (null != jarFile)
			jarFile.close();
		String path = props.getProperty(dirProperty);
		if (path.contains("${WORKDIR}"))
			return; // 避免在替换了${WORKDIR}参数后，还生成${WORKDIR}名称的路径，会产生两个日志路径
		if (path.lastIndexOf("\\") > path.lastIndexOf("/"))
			path = path.substring(0, path.lastIndexOf("\\"));
		else
			path = path.substring(0, path.lastIndexOf("/"));
		File filePath = new File(path);
		if (filePath.exists() == false) {
			filePath.mkdirs();
		}
	}
}
