package com.util.tools;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class NetUtil {

	// 网络访问超时设置
	public static int timeoutConnection = 1000 * 20;
	public static int timeoutSocket = 1000 * 90;
	// 网络访问session记录
	public static String lastSessionId = null;
	// 一些网络访问需要用到代理地址
	public static String proxySvr = null;
	public static int proxyPort = 80;
	// 是否输出网络访问日志信息
	public static boolean netLogEnabled = false;

	/**
	 * 错误信息字符串化
	 * 
	 * @param ex
	 *            错误信息对象
	 * @return String
	 */
	private static String getExceptionTraceStack(Throwable ex) {
		ByteArrayOutputStream deo = new ByteArrayOutputStream();
		PrintStream de = new PrintStream(deo);
		ex.printStackTrace(de);
		String err = new String(deo.toByteArray());
		return err;
	}

	/**
	 * 提交完整请求地址以字符串获取返回的信息
	 * 
	 * @param url
	 *            完整的请求地址
	 * @return String
	 */
	public static String getUrlData(String url) {
		return getUrlData(url, null, null);
	}

	/**
	 * 提交完整请求地址以数据流获取返回的信息
	 * 
	 * @param url
	 *            完整的请求地址
	 * @return InputStream
	 */
	public static InputStream getUrlStreamData(String url) {
		return getUrlStreamData(url, null, null);
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
		return getUrlData(url, null, session);
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
		return getUrlStreamData(url, null, session);
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
		return (String) getUrlDataEx(url, params, session, false, true);
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
		return (InputStream) getUrlDataEx(url, params, session, true, true);
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
		long tm = 0;
		if (canRetry)
			if (isNetLogEnabled()) {
				addNetLog("getting url data: " + url);
				tm = System.currentTimeMillis();
			}

		String postData = "";
		try {
			if (params != null)
				for (String k : params.keySet()) {
					Object v = params.get(k);
					if (postData.length() > 0)
						postData += "&";
					if (!"".equals(k))
						k += "=";
					postData += k + Public.encode(v.toString());
				}
		} catch (Throwable e) {
			if (isNetLogEnabled()) {
				addNetLog("输入参数出错:");
				addNetLog(getExceptionTraceStack(e));
			}
			throw new RuntimeException("输入参数出错:" + e.getMessage());
		}

		HttpURLConnection httpConn = null;
		InputStream inStrm, resStrm = null;
		URL urlo;
		boolean needRetry = false;
		boolean isBaReq = false;
		String res = "";
		int statusCode = 0;
		String redirectUrl = null;
		try {
			try {
				urlo = new URL(url);
				if (proxySvr != null) {
					Proxy proxy = new Proxy(Proxy.Type.HTTP,
							new InetSocketAddress(proxySvr, proxyPort));
					httpConn = (HttpURLConnection) urlo.openConnection(proxy);
				} else
					httpConn = (HttpURLConnection) urlo.openConnection();

				if (session != null && isBaReq) {
					String ck = "";
					if (lastSessionId != null && lastSessionId.length() > 0) {
						if (ck.length() > 0)
							ck = ck + "; ";
						ck = "JSESSIONID=" + lastSessionId;
					}
					if (ck.length() > 0)
						httpConn.addRequestProperty("Cookie", ck);
				}

				httpConn.setInstanceFollowRedirects(true);
				if (postData.length() > 0) {
					httpConn.setDoOutput(true);
					// 设置以POST方式
					httpConn.setRequestMethod("POST");
					// Post 请求不能使用缓存
					httpConn.setUseCaches(false);
					// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
					httpConn.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded");
				}
				// 设置请求超时
				httpConn.setConnectTimeout(timeoutConnection);
				httpConn.setReadTimeout(timeoutSocket);

				if (isNetLogEnabled()) {
					Map<String, List<String>> props = httpConn
							.getRequestProperties();
					addNetLog("Request Headers:");
					for (String k : props.keySet()) {
						List<String> vs = props.get(k);
						String v;
						if (vs == null || vs.size() == 0)
							v = "";
						else if (vs.size() == 1)
							v = vs.get(0);
						else
							v = vs.toString();
						addNetLog(k + "=" + v);
					}
					if (postData.length() > 0) {
						addNetLog("Request Content:");
						addNetLog(postData);
					}
				}

				httpConn.connect();

			} catch (Throwable e) {
				if (isNetLogEnabled()) {
					addNetLog("连接失败:");
					addNetLog(getExceptionTraceStack(e));
				}
				if (e instanceof SocketTimeoutException)
					throw new RuntimeException("连接失败： 连接服务器超时，网络可能较慢请稍后重试");
				else
					throw new RuntimeException("连接失败：" + getNetErrorMsg(e));
			}

			if (postData.length() > 0) {
				try {
					// DataOutputStream流
					DataOutputStream out = new DataOutputStream(
							httpConn.getOutputStream());
					// 要上传的参数
					String content = postData;
					// 将要上传的内容写入流中
					out.writeBytes(content);
					// 刷新、关闭
					out.flush();
					out.close();
				} catch (Throwable e) {
					if (isNetLogEnabled()) {
						addNetLog("提交数据出错:");
						addNetLog(getExceptionTraceStack(e));
					}
					throw new RuntimeException("提交数据出错：" + getNetErrorMsg(e));
				}
			}

			String charset = "utf-8";
			String contentType = "";
			try {
				// 若状态码为200 ok
				statusCode = httpConn.getResponseCode();
				if (statusCode == 302)
					redirectUrl = httpConn.getHeaderField("Location");
				else if (statusCode == HttpURLConnection.HTTP_OK
						|| statusCode == -1) {
					// 取得HTTP response
					Map<String, List<String>> headerFields = httpConn
							.getHeaderFields();
					for (String k : headerFields.keySet()) {
						if ("content-type".equalsIgnoreCase(k)) {
							String v = headerFields.get(k).toString()
									.toLowerCase();
							contentType = v;
							if (v.startsWith("[") && v.endsWith("]"))
								v = v.substring(1, v.length() - 1);
							int p1 = v.indexOf("charset=");
							if (p1 >= 0) {
								p1 += "charset=".length();
								int p2 = v.indexOf(";", p1);
								if (p2 > p1)
									charset = v.substring(p1, p2);
								else
									charset = v.substring(p1);
							}
						}
						if ("set-cookie".equalsIgnoreCase(k) && isBaReq) {
							String v = headerFields.get(k).toString();
							if (v.startsWith("[") && v.endsWith("]"))
								v = v.substring(1, v.length() - 1);
							String sv = getCookieSV(v, "JSESSIONID");
							if (sv.length() > 0)
								lastSessionId = sv;
						}
					}

				} else if (statusCode != 400)
					throw new Exception("响应代码错误" + Integer.toString(statusCode));

				inStrm = httpConn.getInputStream();
				if (inStrm == null) {
					throw new Exception("无法获取结果流");
				}
			} catch (Throwable e) {
				if (isNetLogEnabled()) {
					addNetLog(getExceptionTraceStack(e));
				}
				throw new RuntimeException("打开数据失败：" + getNetErrorMsg(e));
			}

			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte buf[] = new byte[1024 * 8];
				while (true) {
					int c = inStrm.read(buf);
					if (c == -1)
						break;
					out.write(buf, 0, c);
				}
				byte[] bs = out.toByteArray();
				if (!needStream || isNetLogEnabled())
					res = res + (new String(bs, charset));
				if (isNetLogEnabled()) {
					tm = System.currentTimeMillis() - tm;
					addNetLog("Response Time: " + Long.toString(tm));
					addNetLog("Response Code: " + statusCode);
					addNetLog("Response Size: " + bs.length);
					addNetLog("Response Header:");
					Map<String, List<String>> headerFields = httpConn
							.getHeaderFields();
					for (String k : headerFields.keySet()) {
						String v = headerFields.get(k).toString();
						addNetLog(k + "=" + v);
					}
					if (!needStream) {
						addNetLog("Response Content:");
						addNetLog(res);
					}
					addNetLog("");
					addNetLog("");
				}
				out.close();
				if (inStrm != null)
					inStrm.close();
				if (statusCode == 400 || statusCode == 500)
					throw new Exception("响应代码错误" + Integer.toString(statusCode));
				if (canRetry) {
					if (statusCode == 302)
						needRetry = true;
					if (contentType.toLowerCase().contains("wap.wmlc"))
						needRetry = true;
					else if (statusCode == -1 && bs.length == 0)
						needRetry = true;
				} else if (statusCode == -1 && bs.length == 0)
					throw new Exception("响应错误: 无法连接到服务器");

				if (needStream && !needRetry) {
					resStrm = new ByteArrayInputStream(bs);
				}
			} catch (Throwable e) {
				addNetLog("获取出错:");
				addNetLog(getExceptionTraceStack(e));
				throw new RuntimeException("获取数据出错：" + getNetErrorMsg(e));
			}
		} finally {
			try {
				if (httpConn != null) {
					httpConn.disconnect();
				}
			} catch (Throwable te) {
			}
		}
		if (needRetry) {
			if (statusCode == 302 && redirectUrl != null) {
				if (!redirectUrl.contains("&isWM="))
					redirectUrl = redirectUrl + "&isWM=1";
				if (isNetLogEnabled())
					addNetLog("Redirect to: " + redirectUrl);
				return getUrlDataEx(redirectUrl, null, session, needStream,
						true);
			} else
				return getUrlDataEx(url, params, session, needStream, false);
		} else if (needStream)
			return resStrm;
		else
			return res;
	}

	/**
	 * 解析cookies字符串，获取对应名称cookie值
	 * 
	 * @param cookies
	 *            cookies字符串
	 * @param sn
	 *            cookie指所对应名称
	 * @return String
	 */
	private static String getCookieSV(String cookies, String sn) {
		String sv = null;
		int p1 = cookies.toUpperCase().indexOf(sn + "=");
		if (p1 >= 0) {
			p1 += sn.length() + 1;
			int p2 = cookies.indexOf(";", p1);
			if (p2 > p1)
				sv = cookies.substring(p1, p2);
			else
				sv = cookies.substring(p1);
		}
		return sv;
	}

	/**
	 * 包装解析网络错误信息并返回
	 * 
	 * @param e
	 *            网络访问错误信息对象
	 * @return String
	 */
	private static String getNetErrorMsg(Throwable e) {
		if (e instanceof java.net.UnknownHostException)
			return "无法获取服务器，请检查网络";
		if (e instanceof java.net.NoRouteToHostException)
			return "无法连到服务器，请检查网络";
		if (e instanceof java.net.ProtocolException)
			return "服务器响应格式错误，请稍后重试";
		if (e instanceof SocketTimeoutException)
			return "操作超时，网络可能较慢请稍后重试";
		if (e instanceof java.net.ConnectException)
			return "连接失败，请检查网络";
		if (e instanceof java.net.SocketException)
			return "网络操作失败，请检查网络";
		if (e instanceof java.io.IOException)
			return "打开网络失败，请检查网络";
		return e.getMessage();
	}

	/**
	 * 是否进行网络访问日志记录
	 * 
	 * @return boolean
	 */
	public static boolean isNetLogEnabled() {
		return netLogEnabled;
	}

	/**
	 * 日志信息统一输出
	 * 
	 * @param msg
	 *            日志信息
	 */
	public static void addNetLog(String msg) {
		Public.p(msg);
	}

	/**
	 * 提交完整请求地址以json对象获取返回的信息
	 * 
	 * @param url
	 *            完整的请求地址
	 * @return JSONObject
	 */
	public static JSONObject getUrlJsonData(String url) {
		return getUrlJsonData(url, null, null);
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
		return getUrlJsonData(url, null, session);
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
		String sr = getUrlData(url, params, session);
		if (sr == null || sr.length() == 0)
			return null;
		return TypeUtil.str2Json(sr);
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
		if (url == null)
			return url;
		if (parValue != null)
			parValue = Public.encode(parValue);
		int p0 = url.indexOf('?');

		// 块参数： &paramName=[[xxx]]&
		// 支持&test=[[uuu]]的格式，但这种参数有多个时，如&test=123&test=[[345]]，会先处理=[[345]]的值
		boolean isFirst = true;
		String vKeyName = "?" + parName + "=[[";
		String vEndKey = "]]&";
		int p1 = url.indexOf(vKeyName);
		if (p1 < 0) {
			vKeyName = "?" + parName + "=";
			vEndKey = "&";
			p1 = url.indexOf(vKeyName);
		}
		if (p1 < 0) {
			vKeyName = "&" + parName + "=[[";
			vEndKey = "]]&";
			p1 = url.indexOf(vKeyName);
			isFirst = false;
		}
		if (p1 < 0) {
			vKeyName = "&" + parName + "=";
			vEndKey = "&";
			p1 = url.indexOf(vKeyName);
			isFirst = false;
		}
		if (p1 < 0) {
			if (parValue == null)
				return url;
			if (p0 >= 0)
				return url + "&" + parName + "=" + parValue;
			else
				return url + "?" + parName + "=" + parValue;
		} else {
			String res1 = url.substring(0, p1);
			String res2 = "";
			int p2 = url.indexOf(vEndKey, p1 + vKeyName.length());
			if (p2 > 0)
				res2 = url.substring(p2 + vEndKey.length() - 1);
			else {
				if ("]]&".equals(vEndKey) && url.endsWith("]]")) {
					p2 = url.length() - 2;
					res2 = "";
				}
			}
			if (parValue == null) {
				if (isFirst && res2.length() > 0)
					return res1 + "?" + res2.substring(1);
				else
					return res1 + res2;
			}
			if (p0 >= 0)
				return res1 + "&" + parName + "=" + parValue + res2;
			else
				return res1 + "?" + parName + "=" + parValue + res2;
		}
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
		return setUrlParamValue(url, parName, null);
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
		if (url == null)
			return null;

		// 支持&test=[[uuu]]的格式，但这种参数有多个时，如&test=123&test=[[345]]，会先取=[[345]]的值
		String vKeyName = "?" + parName + "=[[";
		int p1 = url.indexOf(vKeyName);
		if (p1 < 0) {
			vKeyName = "&" + parName + "=[[";
			p1 = url.indexOf(vKeyName);
		}
		if (p1 >= 0) {
			String res = url.substring(p1 + vKeyName.length()) + "&";
			int p2 = res.indexOf("]]&");
			if (p2 >= 0) {
				res = res.substring(0, p2);
				res = Public.decode(res);
				return res;
			}
		}

		vKeyName = "?" + parName + "=";
		p1 = url.indexOf(vKeyName);
		if (p1 < 0) {
			vKeyName = "&" + parName + "=";
			p1 = url.indexOf(vKeyName);
		}
		if (p1 < 0) {
			return null;
		} else {
			String res = url.substring(p1 + vKeyName.length());
			int p2 = res.indexOf('&');
			if (p2 > 0)
				res = res.substring(0, p2);
			res = Public.decode(res);
			return res;
		}
	}

	/**
	 * 获取请求链接所有的参数信息 ，并以Map对象返回
	 * 
	 * @param url
	 *            请求地址
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getUrlParamMap(String url) {
		Map<String, Object> result = new HashMap<String, Object>();
		int p1 = url.indexOf("?"), p2, p3, p4;
		if (p1 < 0) {
			return result;
		}
		String parStr = "&" + url.substring(p1 + 1) + "&";
		while (parStr.length() > 0) {
			p1 = parStr.indexOf('&');
			String sNam = "", sVal = "";
			if (p1 < 0)
				break;
			parStr = parStr.substring(p1 + 1);
			boolean isBlock = false;
			p3 = parStr.indexOf('=');
			if (p3 < 0)
				break;
			p4 = parStr.indexOf('&');
			if (p3 > p4) {
				parStr = parStr.substring(p4);
				continue;
			}
			sNam = parStr.substring(0, p3);
			parStr = parStr.substring(p3 + 1);
			if (parStr.startsWith("[[")) {
				p2 = parStr.indexOf("]]&");
				if (p2 >= 0) {
					sVal = parStr.substring(2, p2);
					parStr = parStr.substring(p2 + 2);
					isBlock = true;
				}
			}
			if (!isBlock) {
				p2 = parStr.indexOf("&");
				if (p2 < 0)
					break;
				sVal = parStr.substring(0, p2);
				parStr = parStr.substring(p2);
			}
			if (sNam != null && sNam.trim() != null && !sNam.trim().equals("")
					&& sNam.trim().length() > 0) {
				sVal = Public.decode(sVal);
				result.put(sNam.trim(), sVal.trim());
			}
		}

		return result;
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
		String BOUNDARY = "----------";
		
		// 向服务器发送post请求
		URL url = new URL(serverUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 发送POST请求必须设置如下两行
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty(
				"Content-Type",
				"multipart/form-data; boundary=" + BOUNDARY
						+ System.currentTimeMillis());

		// 头
		String boundary = BOUNDARY + System.currentTimeMillis();

		// 传输内容
		StringBuffer contentBody = new StringBuffer("--" + BOUNDARY
				+ System.currentTimeMillis());

		// 尾
		String endBoundary = "\r\n--" + boundary + "--\r\n";

		OutputStream out = connection.getOutputStream();

		for (Map.Entry<String, Object> entry : formFields.entrySet()) {
			contentBody.append("\r\n")
					.append("Content-Disposition: form-data; name=\"")
					.append(entry.getKey() + "\"").append("\r\n")
					.append("\r\n").append(entry.getValue()).append("\r\n")
					.append("--").append(boundary);
		}

		String boundaryMessage1 = contentBody.toString();

		out.write(boundaryMessage1.getBytes("utf-8"));
		out.flush();

		// 2. 处理文件上传
		for (Map.Entry<String, Object> entry : uploadFiles.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			contentBody = new StringBuffer();
			contentBody
					.append("\r\n")
					.append("Content-Disposition:form-data; name=\"")
					.append(key + "\"; ")
					// form中field的名称
					.append("filename=\"")
					.append(value + "\"")
					// 上传文件的文件名，包括目录
					.append("\r\n")
					.append("Content-Type:application/octet-stream")
					.append("\r\n\r\n");

			String boundaryMessage2 = contentBody.toString();
			out.write(boundaryMessage2.getBytes("utf-8"));
			out.flush();

			// 开始真正向服务器写文件
			File file = new File(value.toString());
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			byte[] buffer = new byte[500 * 1024 * 1024];  
			int len = -1;  
			while ((len = dis.read(buffer)) != -1) {  
				out.write(buffer, 0, len);
				out.flush();
			}
			dis.close();

			contentBody.append("--" + BOUNDARY + System.currentTimeMillis());

			String boundaryMessage = contentBody.toString();

			out.write(boundaryMessage.getBytes("utf-8"));
			out.flush();
		}

		out.write(("--" + BOUNDARY + System.currentTimeMillis() + "--\r\n")
				.getBytes("UTF-8"));

		// 3. 写结尾
		out.write(endBoundary.getBytes("utf-8"));
		out.flush();
		out.close();

		// 4. 从服务器获得回答的内容
		String strLine = "";
		String responseStr = "";

		InputStream in = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		while ((strLine = reader.readLine()) != null) {
			responseStr += strLine + "\n";
		}

		return responseStr;
	}

}
