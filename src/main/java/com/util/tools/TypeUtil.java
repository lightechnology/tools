package com.util.tools;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TypeUtil {

	/**
	 * 对象转字符串，当对应不null是返回空字符串
	 * 
	 * @param o
	 *            对象
	 * @return String
	 */
	public static String objToStr(Object o) {
		if (null == o)
			return "";
		return o.toString();
	}

	/**
	 * 对象转整型
	 * 
	 * @param o
	 *            对象
	 * @return int
	 */
	public static int objToInt(Object o) {
		int i = -9999999;
		try {
			i = Integer.parseInt(objToStr(o));
		} catch (Exception e) {
		}
		return i;
	}

	/**
	 * 对象转浮点型
	 * 
	 * @param o
	 *            对象
	 * @return float
	 */
	public static float objToFloat(Object o) {
		float f = (float) -999999.9;
		try {
			f = Float.parseFloat(objToStr(o));
		} catch (Exception e) {
		}
		return f;
	}

	/**
	 * 对象转双精度型
	 * 
	 * @param o
	 *            对象
	 * @return double
	 */
	public static double objToDouble(Object o) {
		double d = -99999999.99;
		try {
			d = Double.parseDouble(objToStr(o));
		} catch (Exception e) {
		}
		return d;
	}

	/**
	 * 对象转长整型
	 * 
	 * @param o
	 *            对象
	 * @return long
	 */
	public static long objToLong(Object o) {
		long d = 0L;
		try {
			d = Long.parseLong(objToStr(o));
		} catch (Exception e) {
		}
		return d;
	}

	/**
	 * 把json字符串转成json对象
	 * 
	 * @param str
	 *            json字符串
	 * @return JSONObject
	 */
	public static JSONObject str2Json(String jsonStr) {
		Object o = JSONValue.parse(jsonStr.trim());
		return (JSONObject) o;
	}

	/**
	 * 把json格式的字符串转map数据对象
	 * 
	 * @param jsonStr
	 *            json格式字符串
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public static Map<String, Object> str2Map(String jsonStr) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>();
		JSONObject jsonObj = str2Json(jsonStr);
		json2Map(jsonObj, res);
		return res;
	}

	/**
	 * 把map转成json格式的字符串
	 * 
	 * @param map
	 *            map类型数据对象
	 * @return String
	 * @throws Exception
	 */
	public static String map2JsonStr(Map<String, Object> map) throws Exception {
		JSONObject jo = new JSONObject();
		map2Json(jo, map);
		String jstr = jo.toString();
		return jstr;
	}

	/**
	 * 把map数据对象转json数据对象
	 * 
	 * @param jsonObj
	 *            json出参对象
	 * @param map
	 *            map入参对象
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void map2Json(JSONObject jsonObj, Map<String, Object> map)
			throws Exception {
		for (Iterator<Map.Entry<String, Object>> ite = map.entrySet()
				.iterator(); ite.hasNext();) {
			Map.Entry<String, Object> entry = ite.next();
			String k = entry.getKey();
			Object o = entry.getValue();
			if (o instanceof List) {
				JSONArray ja = new JSONArray();
				List ds = (List) o;
				for (int i = 0; i < ds.size(); i++) {
					Object o2 = ds.get(i);
					if (o2 instanceof Map) {
						JSONObject jo2 = new JSONObject();
						map2Json(jo2, (Map) o2);
						ja.add(jo2);
					} else
						ja.add(o2);
				}
				jsonObj.put(k, ja);
			} else if (o instanceof Map) {
				JSONObject jo2 = new JSONObject();
				map2Json(jo2, (Map) o);
				jsonObj.put(k, jo2);
			} else
				jsonObj.put(k, o);
		}
	}

	/**
	 * 把json数据对象转map数据对象
	 * 
	 * @param jsonObj
	 *            json入参对象
	 * @param map
	 *            map出参对象
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static void json2Map(JSONObject jsonObj, Map<String, Object> map)
			throws Exception {
		Iterator ks = jsonObj.keySet().iterator();
		while (ks.hasNext()) {
			Object o = ks.next();
			String k = (String) o;
			Object v;
			v = jsonObj.get(k);
			if (v instanceof JSONObject) {
				// MAP
				Map<String, Object> m = new HashMap<String, Object>();
				json2Map((JSONObject) v, m);
				map.put(k, m);
			} else if (v instanceof JSONArray) {
				JSONArray ja = (JSONArray) v;
				if (k.startsWith("DataBand_") && k.endsWith("_WmList")
						&& ja.size() > 0) {
					// 精简列表，第一行是标题
					List<String> ds_t = new ArrayList<String>();
					JSONArray jt = (JSONArray) ja.get(0);
					for (int j = 0; j < jt.size(); j++)
						ds_t.add(objToStr(jt.get(j)));
					// 之后是数据
					List<Map<String, Object>> ds = new ArrayList<Map<String, Object>>();
					for (int i = 1; i < ja.size(); i++) {
						Map<String, Object> rec = new HashMap<String, Object>();
						JSONArray jk = (JSONArray) ja.get(i);
						for (int j = 0; j < jk.size(); j++)
							rec.put(ds_t.get(j), objToStr(jk.get(j)));
						ds.add(rec);
					}
					map.remove(k);
					map.put(k.substring(0, k.length() - 7), ds);
				} else {
					// 普通MAP列表
					List<Object> ds = new ArrayList<Object>();
					for (int i = 0; i < ja.size(); i++) {
						Map<String, Object> rec = new HashMap<String, Object>();
						Object jk = ja.get(i);
						if (jk instanceof JSONObject) {
							json2Map((JSONObject) jk, rec);
							ds.add(rec);
						} else if (jk != null) {
							if (jk instanceof String)
								ds.add(jk);
							else {
								rec.put("VALUE", jk);
								ds.add(rec);
							}
						}
					}
					map.put(k, ds);
				}
			} else {
				// 其它
				map.put(k, v);
			}
		}
	}

	/**
	 * 标准格式的xml字符字符串转成Map对象
	 * 
	 * @param xml
	 *            标准格式的xml字符字符串
	 * @return Map<String, Object>
	 */
	// 将XML转成MAP
	public static Map<String, Object> xmlToMap(String xml) {
		try {
			// 获取XML解释器
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			// 解释XML
			Document doc = db.parse(new InputSource(new StringReader(xml)));

			if (doc == null)
				return null;

			// 获取根节点
			Element root = doc.getDocumentElement();
			// 创建结果和参数MAP
			Map<String, Object> res = new HashMap<String, Object>();
			Map<String, Object> param = null;// new HashMap<String, Object>();
			// 遍历生成结果MAP
			addElementToMap(res, root, param);

			return res;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 把xml里的节点遍历转成Map对象
	 * 
	 * @param map
	 *            存储数据的Map对象
	 * @param pnd
	 *            xml里的父节点
	 * @param param
	 *            遍历辅助用的Map容器
	 * @return Map<String, Object>
	 */
	@SuppressWarnings("unchecked")
	private static void addElementToMap(Map<String, Object> map, Node pnd,
			Map<String, Object> param) {
		NodeList cn = pnd.getChildNodes();
		if (cn.getLength() == 0) {
			// 如果没有子节点，直接PUT属性并返回
			// 暂时不考虑属性值 2014-07-18 10:38
			/*
			 * String v = pnd.getNodeValue(); if(v != null && 0 <
			 * v.trim().length()) map.put(pnd.getNodeName(), v);
			 */
			return;
		}
		// 遍历所有子节点
		for (int i = 0; i < cn.getLength(); i++) {
			// 获取子节点
			Node nd = cn.item(i);
			if (!(nd instanceof Element)) {
				// 如果不是子节点，继续遍历 2014-07-18 10:40
				/*
				 * String v=nd.getNodeValue(); if(v != null && 0 <
				 * v.trim().length()) map.put(nd.getNodeName(), v);
				 */
				continue;
			}
			Element el = (Element) nd;
			// 获取子节点名称
			String nam = el.getTagName();

			if (map.containsKey(nam)) {// 如果MAP中已经有此节点，说明它是个LIST
				Object o = map.get(nam);
				List<Map<String, Object>> lst = null;
				if (!(o instanceof List)) {// 如果之前还只有一个普通对象，未形成LIST，则到第二个时将它转成LIST
					lst = new ArrayList<Map<String, Object>>();// 创建新LIST
					if (o instanceof Map) // 如果之前那一个就是MAP，直接加到新LIST中
						lst.add((Map<String, Object>) o);
					else {
						// 如果之前那个还不是MAP，先转成MAP再添加到新LIST
						Map<String, Object> m = new HashMap<String, Object>();
						m.put(nam, o);
						lst.add(m);
					}
					map.put(nam, lst);// 新LIST加到父MAP中
				} else
					// 如果之前已经有一个LIST，直接用
					lst = (List<Map<String, Object>>) o;

				// 把所有属性扔到新MAP里，再把新MAP添加LIST中
				// 这部获取子节点属性的功能已经屏蔽 2014-07-18 10:42
				Map<String, Object> m = new HashMap<String, Object>();
				addElementToMap(m, nd, param);
				lst.add(m);
			} else {// 如果之前MAP中没有此节点，当新值处理
				NodeList ccn = nd.getChildNodes();
				if (ccn.getLength() == 0) {
					// 没有子子节点，当属性处理
					map.put(nam, nd.getNodeValue());
				} else if (ccn.getLength() == 1
						&& ccn.item(0).getNodeName().equals("#text")) {
					String v = ccn.item(0).getNodeValue();
					if (v != null && 0 < v.trim().length())
						map.put(nam, v);
				} else {// 有子节点？
					// 把所有属性扔到新MAP里，再把新MAP添加LIST中
					// 这部获取子节点属性的功能已经屏蔽 2014-07-18 10:44
					Map<String, Object> m = new HashMap<String, Object>();
					addElementToMap(m, nd, param);
					map.put(nam, m);
				}
			}
		}
	}

	/**
	 * 把数据流转存磁盘文件
	 * 
	 * @param ins
	 *            数据字节流
	 * @param file
	 *            文件
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean inputStream2File(InputStream ins, File file)
			throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			return true;
		} finally {
			if (null != os)
				os.close();
			if (null != ins)
				ins.close();
		}
	}

	/**
	 * 把磁盘文件转数据流
	 * 
	 * @param file
	 *            文件
	 * @return InputStream
	 * @throws FileNotFoundException
	 */
	public static InputStream file2InputStream(File file)
			throws FileNotFoundException {
		InputStream in = null;
		in = new FileInputStream(file);
		return in;
	}

	/**
	 * 字符串转数据流
	 * 
	 * @param str
	 *            字符串
	 * @return InputStream
	 */
	public static InputStream string2InputStream(String str) {
		ByteArrayInputStream stream = null;
		if (null != str)
			stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	/**
	 * 数据流转字符串
	 * 
	 * @param ins
	 *            数据流
	 * @return String
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream ins) throws IOException {
		return inputStream2StringEx(ins, "utf-8");
	}

	/**
	 * 数据流转字符串
	 * 
	 * @param ins
	 *            数据流
	 * @param code
	 *            数据流编码
	 * @return String
	 * @throws IOException
	 */
	public static String inputStream2StringEx(InputStream ins, String code)
			throws IOException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(ins, code));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line).append("\n");
			}
			return buffer.toString();
		} finally {
			if (null != in)
				in.close();
			if (null != ins)
				ins.close();
		}
	}

	/**
	 * 整形数字转低高排序的byte字符串
	 * 
	 * @param iSource
	 *            要转换的整形数字
	 * @param iArryaLen
	 *            转换成的byte字符串的长度
	 * @return byte[]
	 */
	public static byte[] int2Bytes(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}

	/**
	 * byte字符串(低高排序)转整形
	 * 
	 * @param bytes
	 *            待转换的byte字符串
	 * @return int
	 */
	public static int bytes2Int(byte[] bytes) {
		int iOutcome = 0;
		byte bLoop;
		for (int i = 0; i < bytes.length; i++) {
			bLoop = bytes[i];
			iOutcome += (bLoop & 0xFF) << (8 * i);
		}
		return iOutcome;
	}

	/**
	 * 浮点转换为字节
	 * 
	 * @param f
	 *            要转换的浮点数
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] float2Bytes(float f) throws Exception {

		// 把float转换为byte[]
		int fbit = 0;
		try {
			fbit = Float.floatToIntBits(f);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (fbit >> (24 - i * 8));
		}

		// 翻转数组
		int len = b.length;
		// 建立一个与源数组元素类型相同的数组
		byte[] dest = new byte[len];
		// 为了防止修改源数组，将源数组拷贝一份副本
		System.arraycopy(b, 0, dest, 0, len);
		byte temp;
		// 将顺位第i个与倒数第i个交换
		for (int i = 0; i < len / 2; ++i) {
			temp = dest[i];
			dest[i] = dest[len - i - 1];
			dest[len - i - 1] = temp;
		}

		return dest;

	}

	/**
	 * 字节转换为浮点
	 * 
	 * @param bytes
	 *            要转换的字节串（至少4个字节）
	 * @return
	 * @throws Exception
	 */
	public static float bytes2float(byte[] bytes) throws Exception {
		return bytes2float_ex(bytes, 0);
	}

	/**
	 * 字节转换为浮点
	 * 
	 * @param bytes
	 *            要转换的字节串（至少4个字节）
	 * @param index
	 *            开始位置
	 * @return
	 * @throws Exception
	 */
	public static float bytes2float_ex(byte[] bytes, int index)
			throws Exception {
		float f = (float) -999999.9;
		if (null == bytes || 0 == bytes.length)
			return f;
		int l;
		l = bytes[index + 0];
		l &= 0xff;
		l |= ((long) bytes[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) bytes[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) bytes[index + 3] << 24);
		try {
			f = Float.intBitsToFloat(l);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return f;
	}

	/**
	 * 判断字符串是否是数字
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("^(\\-)?([1-9]\\d*|0)(\\.\\d*)?$");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 长整形数字转低高排序的byte字符串
	 * 
	 * @param iSource
	 *            要转换的长整形数字
	 * @param iArryaLen
	 *            转换成的byte字符串的长度
	 * @return byte[]
	 */
	public static byte[] long2Bytes(long iSource, int iArryaLen) {
		byte[] byteNum = new byte[iArryaLen];
		for (int ix = 0; (ix < 8) && (ix < iArryaLen); ++ix) {
			int offset = ix * 8;
			byteNum[ix] = (byte) ((iSource >> offset) & 0xff);
		}
		return byteNum;
	}

	/**
	 * byte字符串(低高排序)转长整形
	 * 
	 * @param bytes
	 *            待转换的byte字符串
	 * @return long
	 */
	public static long bytes2Long(byte[] bytes) {
		long num = 0;
		for (int ix = 7; ix > -1; --ix) {
			num <<= 8;
			num |= (bytes[ix] & 0xff);
		}
		return num;
	}

	/**
	 * 比较新旧Map对象内的key和value，找出新增和修改的数据 整理成单独一个Map对象返回
	 * 
	 * @param oldMap
	 *            旧Map数据对象
	 * @param newMap
	 *            新Map数据对象
	 * @return 返回包含新增和修改的数据的Map对象
	 */
	public static Map<String, Object> getDiffEntryByCompareMap(
			Map<String, Object> oldMap, Map<String, Object> newMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Iterator<Map.Entry<String, Object>> ite = oldMap.entrySet().iterator(); ite.hasNext();) {
			Map.Entry<String, Object> entry = ite.next();
			String key = entry.getKey().toString();
			if (newMap.containsKey(entry.getKey())) {
				if (!newMap.get(key).equals(entry.getValue())) {
					// str = str + "\n\t修改了key: " + e.getKey();
					map.put(key, newMap.get(entry.getKey()));
				} else {
					// str = str + "\n\t删除了key: " + e.getKey();
				}
			}
		}
		Map<String, Object> nm = new HashMap<String, Object>();
		nm.putAll(newMap);
		nm.keySet().removeAll(oldMap.keySet());
		for (Iterator<Map.Entry<String, Object>> ite = nm.entrySet().iterator(); ite.hasNext();) {
			Map.Entry<String, Object> entry = ite.next();
			// str = str + "\n\t新增了key: " + e.getKey();
			map.put(entry.getKey().toString(), entry.getValue());
		}
		// System.out.println(str);
		return map;
	}

	/**
	 * 把VO数据对象转换成Map数据对象
	 * 
	 * @param obj
	 *            VO数据对象
	 * @return 返回转换成Map的数据对象
	 * @throws ClassNotFoundException
	 */
	public static Map<String, Object> vo2Map(Object obj)
			throws ClassNotFoundException {
		Map<String, Object> map = new HashMap<String, Object>();
		Class<?> c;
		c = Class.forName(obj.getClass().getName());
		Method[] m = c.getMethods();
		for (int i = 0; i < m.length; i++) {
			String method = m[i].getName();
			if (method.startsWith("get")) {
				try {
					Object value = m[i].invoke(obj);
					if (value != null) {
						String key = method.substring(3);
						key = key.substring(0, 1).toLowerCase()
								+ key.substring(1);
						map.put(key, value);
					}
				} catch (Exception e) {
					System.out.println(new StringBuilder("error in ").append(method).append(" of info: ").append(e.getMessage()).toString());
				}
			}
		}
		return map;
	}

	/**
	 * 序列化对象列表集合深拷贝
	 * 
	 * @param list
	 *            序列化对象列表集合
	 * @return 返回拷贝好的列表结合
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> List<T> deepListCopy(List<T> list) throws IOException,
			ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(list);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(
				byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		@SuppressWarnings("unchecked")
		List<T> dest = (List<T>) in.readObject();
		return dest;
	}
	
	/**
	 * 序列号
	 * @param obj
	 * @return
	 */
	public static byte[] serializer(Object obj) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != baos)
					baos.close();
				if (null != oos)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 反序列化
	 * @param data
	 * @return
	 */
	public static Object deserializer(byte[] data) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(data);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != bais)
					bais.close();
				if (null != ois)
					ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
