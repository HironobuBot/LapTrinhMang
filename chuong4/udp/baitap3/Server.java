package chuong4.udp.baitap3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	private int port;
	private InetAddress clientIP;
	private int clientPort;
	public static ArrayList<DatagramPacket> list;
	public Server(int port) {
		this.port = port;
	}
	
	public void start() throws IOException {
		DatagramSocket server = new DatagramSocket(port);
		SVSender sender = new SVSender(server);
		sender.start();
		
		System.out.println("Server is listening...");
		
		while (true) {
			String msg = receiveData(server);
			for (DatagramPacket packet : list) {
				if (!(packet.getAddress().equals(clientIP) && packet.getPort() == clientPort)) {
					sendData(msg, server, packet.getAddress(), packet.getPort());
				}	
			}
			System.out.println(msg);
		}
	}
	public static void main(String[] args) throws IOException {
		Server.list = new ArrayList<DatagramPacket>();
		Server server = new Server(2007);
		server.start();
	}
	private String receiveData(DatagramSocket server) throws IOException {
		byte[] buffer = new byte[1000];
		DatagramPacket dpc = new DatagramPacket(buffer,buffer.length);
		server.receive(dpc);
		clientIP = dpc.getAddress();
		clientPort = dpc.getPort();
		check(dpc);
		return new String(dpc.getData(), 0, dpc.getLength());
	}
	private void check(DatagramPacket packet) {
		for (DatagramPacket item : list) {
			if (item.getAddress().equals(packet.getAddress()) && item.getPort() == packet.getPort()) {
				list.set(list.indexOf(item), item);
				return;
			}
		}
		list.add(packet);
	}
	private void sendData(String msg, DatagramSocket server, InetAddress clientIP, int clientPort) throws IOException {
		byte[] buffer = new byte[1000];
		buffer = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, clientIP, clientPort);
		server.send(packet);
	}
}
class SVSender extends Thread {
	private DatagramSocket server;
	public SVSender(DatagramSocket server) {
		this.server = server;
	}
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			String msg = "SERVER: " + sc.nextLine();
			for (DatagramPacket packet : Server.list) {
	        	byte[] data = msg.getBytes();
	    		DatagramPacket serverDatagramPacket = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
	    		try {
					server.send(serverDatagramPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}