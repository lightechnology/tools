package com.util.tools;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrMake {

	/**
	 * 组织带时间的字符串
	 * 
	 * @param s
	 *            原字符串
	 * @return String
	 */
	public static String z(String s) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss:ms");
		return new StringBuilder("[").append(formatter.format(new Date())).append("] ").append(s).toString();
	}

	/**
	 * 修正逗号分割字符串，去头去尾逗号
	 * 
	 * @param str
	 *            字符串
	 * @return String
	 */
	public static String cstr(String str) {
		String s = "";
		if (null != str && !"".equals(str.trim())) {
			s = str.trim();
			if (0 == s.indexOf(","))
				s = s.substring(1);
			if (s.length() - 1 == s.lastIndexOf(","))
				s = s.substring(0, s.length() - 1);
			return s;
		} else {
			return s;
		}
	}

	/**
	 * byte数据组转为十六进制字串
	 * 
	 * @param b
	 *            字节数据
	 * @return String
	 */
	public static String byte2hex(byte[] b) {
		StringBuilder sb = new StringBuilder();
		if (0 < b.length) {
			sb.append(byte2hex_ex(b[0]));
			for (int n = 1; n < b.length; n++) {
				sb.append(" ");
				sb.append(byte2hex_ex(b[n]));
			}
		}
		return sb.toString();
	}

	/**
	 * byte转为十六进制字串
	 * 
	 * @param b
	 *            字节
	 * @return String
	 */
	public static String byte2hex_ex(byte b) {
		StringBuilder sb = new StringBuilder();
		String hex = Integer.toHexString(b & 0xFF).toUpperCase();
		if (2 > hex.length())
			sb.append("0");
		sb.append(hex);
		return sb.toString();
	}

	/**
	 * 单字符转为byte
	 * 
	 * @param c
	 *            字符
	 * @return byte
	 */
	public static byte hexChar2byte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 十六进制字符串转为byte数组
	 * 
	 * @param hex
	 *            字符串数据
	 * @return
	 */
	public static byte[] hexString2bytes(String hex) {
		String thex = hex.replace(" ", "").toUpperCase();
		if (thex.length() % 2 != 0)
			throw new IllegalArgumentException();
		char[] arr = thex.toCharArray();
		byte[] b = new byte[thex.length() / 2];
		for (int i = 0, j = 0, l = thex.length(); i < l; i++, j++) {
			String swap = "" + arr[i++] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();
		}
		return b;
	}

	/**
	 * 获取http请求地址里的参数，转map传出
	 * 
	 * @param url
	 *            http请求地址
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> url2ParamMap(String url) {
		String params = "";
		if ("".equals(TypeUtil.objToStr(url)))
			return null;
		else if (0 > url.indexOf("="))
			return null;
		else if (0 < url.indexOf("?"))
			params = url.substring(url.indexOf("?"));
		else
			params = url;
		String[] param = params.split("&");
		Map<String, Object> rparam = new HashMap<String, Object>();
		for (int i = 0; i < param.length; i++) {
			String key = param[i].substring(0, param[i].indexOf("="));
			String value = param[i].substring(param[i].indexOf("=") + 1);
			if ("".equals(key))
				return null;
			rparam.put(key, value);
		}
		return rparam;
	}

	/**
	 * bytes大端小端转化
	 * 
	 * @param b
	 *            字节数据
	 * @return byte[]
	 */
	public static byte[] bytesOverturn(byte[] b) {
		byte[] r = new byte[b.length];
		int l = b.length - 1;
		for (int i = l; -1 < i; i--) {
			r[l - i] = b[i];
		}
		return r;
	}

	/**
	 * 查找字符串里第几次出现子字符串的位置
	 * 
	 * @param s
	 *            原来字符串
	 * @param st
	 *            子字符串
	 * @param index
	 *            第几次出现
	 * @return int
	 */
	public static int indeOfs(String s, String st, int index) {
		int rint = -1;
		String _s = s;
		for (int i = 0; i < index - 1; i++) {
			if (-1 == rint)
				rint = _s.indexOf(st);
			else
				rint += _s.indexOf(st) + 1;
			_s = _s.substring(_s.indexOf(st) + 1);
		}
		if (-1 < _s.indexOf(st))
			return rint + _s.indexOf(st) + 1;
		else
			return s.length();
	}

	/**
	 * 把http链接里所带参数转换成Map返回
	 * 
	 * @param url
	 *            带参数的http链接字符串
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getParamsFromURL(String url) {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		String[] ps = url.split("&");
		for (int i = 0; i < ps.length; i++) {
			String s = ps[i];
			String key = "";
			String value = "";
			if (s.contains("?"))
				s = s.split("\\?")[1];
			if (s.startsWith("http://"))
				continue;
			if (s.contains("=")) {
				key = s.substring(0, s.indexOf("="));
				value = s.substring(s.indexOf("=") + 1);
			} else {
				key = s;
				value = "";
			}
			if (!"".equals(key))
				params.put(key, value);
		}
		return params;
	}

	/**
	 * 把http链接里所带参数utf-8编码后完整链接返回
	 * 
	 * @param url
	 *            带参数的http链接字符串
	 * @return String
	 */
	public static String encodeURL(String url) {
		StringBuilder paramstr1 = new StringBuilder();
		StringBuilder paramstr2 = new StringBuilder();
		String[] ps = url.split("&");
		for (int i = 0; i < ps.length; i++) {
			String s = ps[i];
			String key = "";
			String value = "";
			if (s.contains("?"))
				s = s.split("\\?")[1];
			if (s.startsWith("http://"))
				continue;
			if (s.contains("=")) {
				key = s.substring(0, s.indexOf("="));
				value = s.substring(s.indexOf("=") + 1);
			} else {
				key = s;
				value = "";
			}
			if (0 < paramstr1.toString().length() && s.contains("=")) {
				paramstr1.append("&").append(key).append("=").append(value);
				paramstr2.append("&").append(key).append("=").append(Public.encode(value));
			} else if (s.contains("=")) {
				paramstr1.append(key).append("=").append(value);
				paramstr2.append(key).append("=").append(Public.encode(value));
			} else if (0 < paramstr1.toString().length()) {
				paramstr1.append("&").append(key);
				paramstr2.append("&").append(key);
			} else {
				paramstr1.append(key);
				paramstr2.append(key);
			}
		}
		return url.replace(paramstr1.toString(), "") + paramstr2.toString();
	}

	/**
	 * 返回格式化好的UUID字符串
	 * 
	 * @return String
	 */
	public static String uuid() {
		return (java.util.UUID.randomUUID() + "").replace("-", "")
				.toUpperCase();
	}

	/**
	 * 连接两个byte[]
	 * 
	 * @param byte[] b1
	 * @param byte[] b2
	 * @return byte[]
	 */
	public static byte[] combanbyte(byte[] b1, byte[] b2) {
		if (null != b1 && null != b2) {
			ByteBuffer d = ByteBuffer.allocate(b1.length + b2.length);
			d.put(b1);
			d.put(b2);
			return d.array();
		} else if (null != b1) {
			ByteBuffer d = ByteBuffer.allocate(b1.length);
			d.put(b1);
			return d.array();
		} else if (null != b2) {
			ByteBuffer d = ByteBuffer.allocate(b2.length);
			d.put(b2);
			return d.array();
		} else {
			return null;
		}
	}

	/**
	 * 按照给定的长度和hex字符串内容判断字符串是否是一个正确的hex字符串
	 * 
	 * @param String
	 *            hex
	 * @param int len
	 * @return boolean
	 */
	public static boolean checkHexByLen(String hex, int len) {
		if (Pattern.compile("^[A-Fa-f0-9]{" + len + "}$").matcher(hex).find())
			return true;
		return false;
	}

	/**
	 * 给出URL请求参数Map数据集对象，转换成符合URL规则的参数字符串
	 * 
	 * @param paramterMap
	 *            请求参数Map数据集对象
	 * @return String 返回符合URL规则的参数字符串
	 */
	public static String paramterMap2ParamterStr(Map<String, Object> paramterMap) {
		String result = "";
		StringBuilder paramterStr = new StringBuilder();
		for (Iterator<Map.Entry<String, Object>> ite = paramterMap.entrySet().iterator(); ite.hasNext();) {
			Map.Entry<String, Object> entry = ite.next();
			paramterStr.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}
		result = paramterStr.toString();
		if (0 < result.length())
			result = result.substring(1);
		return result;
	}

	/**
	 * 去掉字符串内的空格和回车符等空白字符
	 * 
	 * @param str
	 *            要处理的字符串
	 * @return 返回处理后的字符串
	 */
	public static String trim(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	/**
	 * 截取字节数组
	 * 
	 * @param data
	 *            原来数组
	 * @param offset
	 *            截取起始位置，从0开始
	 * @param length
	 *            截取长度
	 * @return
	 */
	public static byte[] cutBytes(byte[] data, int offset, int length) {
		if (0 == length || offset == length || null == data || 0 == data.length
				|| offset + length > data.length)
			return null;
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = data[offset + i];
		}
		return result;
	}

}
