package com.util.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.simple.JSONObject;

public class Public {

	/**
	 * 简化System.out.println，带时间
	 * 
	 * @param s
	 *            要输出的字符串
	 * @return void
	 */
	public static void p(String s) {
		Print.p(s);
	}

	/**
	 * 对map内容作日志输出显示
	 * 
	 * @param map
	 *            数据内容
	 * @return void
	 */
	public static void pMap(Map<String, Object> map) {
		Print.pMap(map);
	}

	/**
	 * 组织带时间的字符串
	 * 
	 * @param s
	 *            原字符串
	 * @return String
	 */
	public static String z(String s) {
		return StrMake.z(s);
	}

	/**
	 * 修正逗号分割字符串，去头去尾逗号
	 * 
	 * @param str
	 *            字符串
	 * @return String
	 */
	public static String cstr(String str) {
		return StrMake.cstr(str);
	}

	/**
	 * 获取请求参数
	 * 
	 * @param parameterMap
	 *            请求参数集合
	 * @param name
	 *            参数名
	 * @return String
	 */
	public static String getParam(Map<String, Object> parameterMap, String name) {
		if (parameterMap.containsKey(name))
			return ((String[]) parameterMap.get(name))[0];
		else
			return "";
	}

	/**
	 * 对象转字符串，当对应不null是返回空字符串
	 * 
	 * @param o
	 *            对象
	 * @return String
	 */
	public static String objToStr(Object o) {
		return TypeUtil.objToStr(o);
	}

	/**
	 * 对象转整型
	 * 
	 * @param o
	 *            对象
	 * @return int
	 */
	public static int objToInt(Object o) {
		return TypeUtil.objToInt(o);
	}

	/**
	 * 对象转浮点型
	 * 
	 * @param o
	 *            对象
	 * @return float
	 */
	public static float objToFloat(Object o) {
		return TypeUtil.objToFloat(o);
	}

	/**
	 * 对象转双精度型
	 * 
	 * @param o
	 *            对象
	 * @return double
	 */
	public static double objToDouble(Object o) {
		return TypeUtil.objToDouble(o);
	}

	/**
	 * 对象转长整型
	 * 
	 * @param o
	 *            对象
	 * @return double
	 */
	public static long objToLong(Object o) {
		return TypeUtil.objToLong(o);
	}

	/**
	 * byte数据组转为十六进制字串
	 * 
	 * @param b
	 *            字节数据
	 * @return String
	 */
	public static String byte2hex(byte[] b) {
		return StrMake.byte2hex(b);
	}

	/**
	 * byte转为十六进制字串
	 * 
	 * @param b
	 *            字节
	 * @return String
	 */
	public static String byte2hex_ex(byte b) {
		return StrMake.byte2hex_ex(b);
	}

	/**
	 * 单字符转为byte
	 * 
	 * @param c
	 *            字符
	 * @return byte
	 */
	public static byte hexChar2byte(char c) {
		return StrMake.hexChar2byte(c);
	}

	/**
	 * 十六进制字符串转为byte数组
	 * 
	 * @param hex
	 *            字符串数据
	 * @return
	 */
	public static byte[] hexString2bytes(String hex) {
		return StrMake.hexString2bytes(hex);
	}

	/**
	 * 对字符串内容作MD5加密
	 * 
	 * @param str
	 *            要加密的字符串
	 * @return String
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException 
	 */
	public static String stringToMD5(String str)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return CodeUtil.stringToMD5(str);
	}

	/**
	 * 对字符串内容作DES加密
	 * 
	 * @param str
	 *            要加密的字符串
	 * @param key
	 *            密钥
	 * @return String
	 * @throws UnsupportedEncodingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String desencrypt(String str, String key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {
		return CodeUtil.desencrypt(str, key);
	}

	/**
	 * 对字符串内容作DES解密
	 * 
	 * @param str
	 *            要解密的字符串
	 * @param key
	 *            密钥
	 * @return String
	 * @throws IOException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String desdecrypt(String str, String key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		return CodeUtil.desdecrypt(str, key);
	}

	/**
	 * 通过文件获取文件MD5校验码
	 * 
	 * @param file
	 *            文件
	 * @return String
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMd5ByFile(File file)
			throws NoSuchAlgorithmException, IOException {
		return CodeUtil.getMd5ByFile(file);
	}

	/**
	 * 通过文件路径获取文件MD5校验码
	 * 
	 * @param filedir
	 *            文件路径
	 * @return String
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMd5ByFile(String filedir)
			throws NoSuchAlgorithmException, IOException {
		return CodeUtil.getMd5ByFile(filedir);
	}

	/**
	 * 默认通过UTF-8返回编码字符串
	 * 
	 * @param v
	 *            要编码的字符串
	 * @return String
	 */
	public static String encode(String v) {
		return CodeUtil.encode(v);
	}

	/**
	 * 给定编码格式，返回编码字符串
	 * 
	 * @param v
	 *            要编码的字符串
	 * @param enc
	 *            编码格式
	 * @return String
	 */
	public static String encodeEx(String v, String enc) {
		return CodeUtil.encodeEx(v, enc);
	}

	/**
	 * 默认通过UTF-8返回解码字符串
	 * 
	 * @param v
	 *            要解码的字符串
	 * @return String
	 */
	public static String decode(String v) {
		return CodeUtil.decode(v);
	}

	/**
	 * 给定解码格式，返回解码字符串
	 * 
	 * @param v
	 *            要解码的字符串
	 * @param enc
	 *            解码格式
	 * @return String
	 */
	public static String decodeEx(String v, String enc) {
		return CodeUtil.decodeEx(v, enc);
	}

	/**
	 * 提交完整请求地址以字符串获取返回的信息
	 * 
	 * @param url
	 *            完整的请求地址
	 * @return String
	 */
	public static String getUrlData(String url) {
		return NetUtil.getUrlData(url);
	}

	/**
	 * 提交完整请求地址以数据流获取返回的信息
	 * 
	 * @param url
	 *            完整的请求地址
	 * @return InputStream
	 */
	public static InputStream getUrlStreamData(String url) {
		return NetUtil.getUrlStreamData(url);
	}

	/**
	 * 提交完整网络访问地址，附带session信息，以字符串获取返回的信息
	 * 
	 * @param url
	 *            完整的请求地址
	 * @param session
	 *            网络session信息
	 * @return String
	 */
	public static String getUrlData(String url, Map<String, Object> session) {
		return NetUtil.getUrlData(url, session);
	}

	/**
	 * 提交完整网络访问地址，附带session信息，以数据流获取返回的信息
	 * 
	 * @param url
	 *            完整的请求地址
	 * @param session
	 *            网络session信息
	 * @return InputStream
	 */
	public static InputStream getUrlStreamData(String url,
			Map<String, Object> session) {
		return NetUtil.getUrlStreamData(url, session);
	}

	/**
	 * 提交网络访问地址，附带参数信息和session信息，以字符串获取返回的信息
	 * 
	 * @param url
	 *            完整的请求地址
	 * @param params
	 *            请求参数信息
	 * @param session
	 *            网络session信息
	 * @return String
	 */
	public static String getUrlData(String url, Map<String, Object> params,
			Map<String, Object> session) {
		return NetUtil.getUrlData(url, params, session);
	}

	/**
	 * 提交网络访问地址，附带参数信息和session信息，以数据流获取返回的信息
	 * 
	 * @param url
	 *            完整的请求地址
	 * @param params
	 *            请求参数信息
	 * @param session
	 *            网络session信息
	 * @return InputStream
	 */
	public static InputStream getUrlStreamData(String url,
			Map<String, Object> params, Map<String, Object> session) {
		return NetUtil.getUrlStreamData(url, params, session);
	}

	/**
	 * 网络请求获取数据核心代码
	 * 
	 * @param url
	 *            完整的请求地址
	 * @param params
	 *            请求参数信息
	 * @param session
	 *            网络session信息
	 * @param needStream
	 *            是否以数据流方式获取返回信息 true 以数据流方式获取
	 * @param canRetry
	 *            当网络访问出现问题时是否尝试其他方法访问 true 尝试其他方法访问
	 * @return Object
	 */
	public static Object getUrlDataEx(String url, Map<String, Object> params,
			Map<String, Object> session, boolean needStream, boolean canRetry) {
		return NetUtil.getUrlDataEx(url, params, session, needStream, canRetry);
	}

	/**
	 * 提交完整请求地址以json对象获取返回的信息
	 * 
	 * @param url
	 *            完整的请求地址
	 * @return JSONObject
	 */
	public static JSONObject getUrlJsonData(String url) {
		return NetUtil.getUrlJsonData(url);
	}

	/**
	 * 提交完整请求地址，附带session信息，以json对象获取返回的信息
	 * 
	 * @param url
	 *            完整的请求地址
	 * @param session
	 *            网络session信息
	 * @return JSONObject
	 */
	public static JSONObject getUrlJsonData(String url,
			Map<String, Object> session) {
		return NetUtil.getUrlJsonData(url, session);
	}

	/**
	 * 提交请求地址，附带请求参数信息和session信息，以json对象获取返回的信息
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数信息
	 * @param session
	 *            网络session信息
	 * @return JSONObject
	 */
	public static JSONObject getUrlJsonData(String url,
			Map<String, Object> params, Map<String, Object> session) {
		return NetUtil.getUrlJsonData(url, params, session);
	}

	/**
	 * 设置请求链接里的参数，把拼接好的链接返回
	 * 
	 * @param url
	 *            请求地址
	 * @param parName
	 *            请求参数名
	 * @param parValue
	 *            请求参数值
	 * @return String
	 */
	public static String setUrlParamValue(String url, String parName,
			String parValue) {
		return NetUtil.setUrlParamValue(url, parName, parValue);
	}

	/**
	 * 清空请求链接里的参数，把拼接好的链接返回
	 * 
	 * @param url
	 *            请求地址
	 * @param parName
	 *            请求参数名
	 * @return String
	 */
	public static String removeOneUrlParam(String url, String parName) {
		return NetUtil.removeOneUrlParam(url, parName);
	}

	/**
	 * 获取请求链接里指定参数名的值，并返回
	 * 
	 * @param url
	 *            请求地址
	 * @param parName
	 *            请求参数名
	 * @return String
	 */
	public static String getUrlParamValue(String url, String parName) {
		return NetUtil.getUrlParamValue(url, parName);
	}

	/**
	 * 获取请求链接所有的参数信息 ，并以Map对象返回
	 * 
	 * @param url
	 *            请求地址
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getUrlParamMap(String url) {
		return NetUtil.getUrlParamMap(url);
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
		return TypeUtil.inputStream2File(ins, file);
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
		return TypeUtil.file2InputStream(file);
	}

	/**
	 * 字符串转数据流
	 * 
	 * @param str
	 *            字符串
	 * @return InputStream
	 */
	public static InputStream string2InputStream(String str) {
		return TypeUtil.string2InputStream(str);
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
		return TypeUtil.inputStream2String(ins);
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
		return TypeUtil.inputStream2StringEx(ins, code);
	}

	/**
	 * 获取http请求地址里的参数，转map传出
	 * 
	 * @param url
	 *            http请求地址
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> url2ParamMap(String url) {
		return StrMake.url2ParamMap(url);
	}

	/**
	 * 把json字符串转成json对象
	 * 
	 * @param str
	 *            json字符串
	 * @return JSONObject
	 */
	public static JSONObject str2Json(String jsonStr) {
		return TypeUtil.str2Json(jsonStr);
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
		return TypeUtil.str2Map(jsonStr);
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
		return TypeUtil.map2JsonStr(map);
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
	public static void map2Json(JSONObject jsonObj, Map<String, Object> map)
			throws Exception {
		TypeUtil.map2Json(jsonObj, map);
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
	public static void json2Map(JSONObject jsonObj, Map<String, Object> map)
			throws Exception {
		TypeUtil.json2Map(jsonObj, map);
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
		return TypeUtil.xmlToMap(xml);
	}

	/**
	 * 获取工程根路径，如果是打包成jar，就返回jar所在路径
	 * 
	 * @return String
	 */
	public static String getJarRootDir() {
		return SysParams.getJarRootDir();
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
		return SysParams.getJarInnerFile(url);
	}

	/**
	 * 获取Classes的根路径
	 * 
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String getClassesDir() throws UnsupportedEncodingException {
		return SysParams.getClassesDir();
	}

	/**
	 * 获取Class所在绝对全路径（含包路径)
	 * 
	 * @param class Class对象
	 * @return String
	 */
	public static String getClassDir(Class<?> clazz) {
		return SysParams.getClassDir(clazz);
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
		return TypeUtil.int2Bytes(iSource, iArrayLen);
	}

	/**
	 * byte字符串(低高排序)转整形
	 * 
	 * @param bytes
	 *            待转换的byte字符串
	 * @return int
	 */
	public static int bytes2Int(byte[] bytes) {
		return TypeUtil.bytes2Int(bytes);
	}

	/**
	 * 对16进制字符串进行crc-16的计算,返回2位byte
	 * 
	 * @param data
	 *            16进制字符串
	 * @return byte[]
	 */
	public static byte[] crc16(byte[] data) {
		return CodeUtil.crc16(data);
	}

	public static byte[] crc16_(byte[] data) {
		return CodeUtil.crc16_(data);
	}
	
	/**
	 * 对16进制字符串进行crc-16-a001的计算,返回2位byte
	 * 
	 * @param data
	 *            16进制字符串
	 * @return byte[]
	 */
	public static byte[] crc16_A001(byte[] data) {
		return CodeUtil.crc16_A001(data);
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
		return TypeUtil.float2Bytes(f);
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
		return TypeUtil.bytes2float(bytes);
	}

	/**
	 * bytes大端小端转化
	 * 
	 * @param b
	 *            字节数据
	 * @return byte[]
	 */
	public static byte[] bytesOverturn(byte[] b) {
		return StrMake.bytesOverturn(b);
	}

	/**
	 * 判断字符串是否是数字
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return
	 */
	public static boolean isNumeric(String str) {
		return TypeUtil.isNumeric(str);
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
		return TypeUtil.long2Bytes(iSource, iArryaLen);
	}

	/**
	 * byte字符串(低高排序)转长整形
	 * 
	 * @param bytes
	 *            待转换的byte字符串
	 * @return long
	 */
	public static long bytes2Long(byte[] bytes) {
		return TypeUtil.bytes2Long(bytes);
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
		return StrMake.indeOfs(s, st, index);
	}

	/**
	 * 对字符串进行BASE64加密
	 * 
	 * @param str
	 *            待加密字符串
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String deBase64(String str)
			throws UnsupportedEncodingException {
		return CodeUtil.deBase64(str);
	}

	/**
	 * 对字符串进行BASE64解密
	 * 
	 * @param str
	 *            待解密字符串
	 * @return String
	 * @throws IOException
	 */
	public static String enBase64(String str) throws IOException {
		return CodeUtil.enBase64(str);
	}

	/**
	 * 把http链接里所带参数转换成Map返回
	 * 
	 * @param url
	 *            带参数的http链接字符串
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getParamsFromURL(String url) {
		return StrMake.getParamsFromURL(url);
	}

	/**
	 * 把http链接里所带参数utf-8编码后完整链接返回
	 * 
	 * @param url
	 *            带参数的http链接字符串
	 * @return String
	 */
	public static String encodeURL(String url) {
		return StrMake.encodeURL(url);
	}

	/**
	 * 对字符串内容作AES加密
	 * 
	 * @param content
	 *            要加密的字符串
	 * @param password
	 *            密钥
	 * @return byte[]
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] aesencrypt(String content, String password)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		return CodeUtil.aesencrypt(content, password);
	}

	/**
	 * 对字符串内容作AES解密
	 * 
	 * @param content
	 *            要解密的字符串
	 * @param password
	 *            密钥
	 * @return byte[]
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] asedecrypt(byte[] content, String password)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		return CodeUtil.asedecrypt(content, password);
	}

	/**
	 * 返回格式化好的UUID字符串
	 * 
	 * @return String
	 */
	public static String uuid() {
		return StrMake.uuid();
	}

	/**
	 * 连接两个byte[]
	 * 
	 * @param byte[] b1
	 * @param byte[] b2
	 * @return byte[]
	 */
	public static byte[] combanbyte(byte[] b1, byte[] b2) {
		return StrMake.combanbyte(b1, b2);
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
		return StrMake.checkHexByLen(hex, len);
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
		return TypeUtil.getDiffEntryByCompareMap(oldMap, newMap);
	}

	/**
	 * 给出URL请求参数Map数据集对象，转换成符合URL规则的参数字符串
	 * 
	 * @param paramterMap
	 *            请求参数Map数据集对象
	 * @return String 返回符合URL规则的参数字符串
	 */
	public static String paramterMap2ParamterStr(Map<String, Object> paramterMap) {
		return StrMake.paramterMap2ParamterStr(paramterMap);
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
		return TypeUtil.vo2Map(obj);
	}

	/**
	 * 去掉字符串内的空格和回车符等空白字符
	 * 
	 * @param str
	 *            要处理的字符串
	 * @return 返回处理后的字符串
	 */
	public static String trim(String str) {
		return StrMake.trim(str);
	}
	
	/**
	 * 上传文件附带参数信息
	 * 
	 * @param serverUrl
	 *            上传文件的服务地址
	 * @param formFields
	 *            参数信息
	 * @param uploadFiles
	 *            文件（全路径地址）
	 * @return 返回服务请求结果
	 * @throws Exception
	 */
	public static String sendHttpPostRequest(String serverUrl,
			Map<String, Object> formFields, Map<String, Object> uploadFiles)
			throws Exception {
		return NetUtil.sendHttpPostRequest(serverUrl, formFields, uploadFiles);
	}
	
	/**
	 * 序列号对象列表集合深拷贝
	 * 
	 * @param list
	 *            序列号对象列表集合
	 * @return 返回拷贝好的列表结合
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> List<T> deepListCopy(List<T> list) throws IOException,
			ClassNotFoundException {
		return TypeUtil.deepListCopy(list);
	}
	
	/**
	 * 序列号
	 * @param obj
	 * @return
	 */
	public static byte[] serializer(Object obj) {
		return TypeUtil.serializer(obj);
	}
	
	/**
	 * 反序列化
	 * @param data
	 * @return
	 */
	public static Object deserializer(byte[] data) {
		return TypeUtil.deserializer(data);
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
		return StrMake.cutBytes(data, offset, length);
	}
	
	/**
	 * 线程方式延时，即休眠线程，这个方式避开中断，就是即使中断了还会继续休眠线程继续延时
	 * 
	 * @param time
	 *            休眠时间
	 */
	public static void sleepWithOutInterrupted(long time) {
		long t = time;
		long tx = System.currentTimeMillis();
		while (true && 0 < t) {
			try {
				Thread.sleep(t);
			} catch (Exception e) {
				t = System.currentTimeMillis() - tx;
				continue;
			}
			break;
		}
	}
	
}
