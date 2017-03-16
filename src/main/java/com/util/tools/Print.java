package com.util.tools;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Print {

	/**
	 * 简化System.out.println，带时间
	 * 
	 * @param s
	 *            要输出的字符串
	 * @return void
	 */
	public static void p(String s) {
		System.out.println(StrMake.z(s));
	}

	/**
	 * 对map内容作日志输出显示
	 * 
	 * @param map
	 *            数据内容
	 * @return void
	 */
	public static void pMap(Map<?, ?> map) {
		StringBuilder str = new StringBuilder(StrMake.z(""));
		for (Iterator<?> ite = map.entrySet().iterator(); ite.hasNext();) {
			Map.Entry<?, ?> entry = (Entry<?, ?>) ite.next();
			str.append("\n\t").append(entry.getKey()).append(": ").append(entry.getValue());
		}
		System.out.println(str.toString());
	}

}
