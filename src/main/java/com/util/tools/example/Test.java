package com.util.tools.example;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.util.tools.Public;

public class Test {
	public static void main(String args[]) {
		int str = 2;
		byte[] bytes = { (byte) str };
		Public.p(Public.byte2hex(bytes));
		getDateTime();
		char[] c = new char[8];
		c = "SBSDFSDF000".toCharArray();
		Public.p(c.length + "");
		Public.p("sss".length() + "");
	}

	public static void getDateTime() {
		ByteBuffer buffer = ByteBuffer.allocate(7);
		String dataTime = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss:ms").format(new Date());
		int year = Public.objToInt(dataTime.substring(0, 4));
		int month = Public.objToInt(dataTime.substring(5, 7));
		int day = Public.objToInt(dataTime.substring(8, 10));
		int hour = Public.objToInt(dataTime.substring(11, 13));
		int minutes = Public.objToInt(dataTime.substring(14, 16));
		int seconds = Public.objToInt(dataTime.substring(17, 19));
		byte[] b = toByteArray(year, 2);
		buffer.put(b);
		b = toByteArray(month, 1);
		buffer.put(b);
		b = toByteArray(day, 1);
		buffer.put(b);
		b = toByteArray(hour, 1);
		buffer.put(b);
		b = toByteArray(minutes, 1);
		buffer.put(b);
		b = toByteArray(seconds, 1);
		buffer.put(b);
		buffer.flip();
		Public.p(Public.byte2hex(buffer.array()));
	}

	public static byte[] toByteArray(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}
}
