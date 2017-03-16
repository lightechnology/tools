package com.util.tools.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpSocket {

	DatagramSocket udp;

	/**
	 * 在指定端口监听udp消息
	 * 
	 * @param port
	 * @throws SocketException
	 */
	public UdpSocket(int port) throws SocketException {
		udp = new DatagramSocket(port);
	}

	/**
	 * 向指定的ip地址的端口发送消息
	 * 
	 * @param message
	 *            要发送的消息
	 * @param port
	 *            指定的端口
	 * @param ip
	 *            指定的ip地址
	 * @throws IOException
	 */
	public void send(String message, int port, String ip) throws IOException {
		byte temp[] = message.getBytes();
		InetAddress address = InetAddress.getByName(ip);
		DatagramPacket send_packet = new DatagramPacket(temp, temp.length,
				address, port);
		udp.send(send_packet);
	}
	
	/**
	 * 向指定的ip地址的端口发送消息
	 * 
	 * @param message
	 *            要发送的消息
	 * @param port
	 *            指定的端口
	 * @param ip
	 *            指定的ip地址
	 * @throws IOException
	 */
	public void send(byte[] message, int port, String ip) throws IOException {
		InetAddress address = InetAddress.getByName(ip);
		DatagramPacket send_packet = new DatagramPacket(message, message.length,
				address, port);
		udp.send(send_packet);
	}

	/**
	 * 从端口读取报文并返回报文数据
	 * 
	 * @return 报文数据
	 * @throws IOException
	 */
	public String readString() throws IOException {
		byte temp[] = new byte[2048];
		DatagramPacket receive_packet = new DatagramPacket(temp, temp.length);
		udp.receive(receive_packet);
		String result = new String(receive_packet.getData(), 0,
				receive_packet.getLength());
		return result;
	}
	
	/**
	 * 从端口读取报文并返回报文数据
	 * 
	 * @return 报文数据
	 * @throws IOException
	 */
	public byte[] readBytes() throws IOException {
		byte temp[] = new byte[2048];
		DatagramPacket receive_packet = new DatagramPacket(temp, temp.length);
		udp.receive(receive_packet);
		return receive_packet.getData();
	}
	
}