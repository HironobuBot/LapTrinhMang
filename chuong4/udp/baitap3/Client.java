package chuong4.udp.baitap3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
	private InetAddress host;
	private int port;
	public Client(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}
	private void start() throws IOException {
		DatagramSocket client = new DatagramSocket();
		
		System.out.printf("Your name: ");
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		//notf that this client have joined
		String msg = name + " has joined group!";
		byte[] data = msg.getBytes();
		DatagramPacket datagramPacket=new DatagramPacket(data,data.length,host,port);
		client.send(datagramPacket);
		
		CLReceiver receiver = new CLReceiver(client);
		receiver.start();
		CLSender sender = new CLSender(client, name, host, port);
		sender.start();
	}
	public static void main(String[] args) throws IOException {
		Client client = new Client(InetAddress.getLocalHost(), 2007);
		client.start();
	}
}
class CLReceiver extends Thread {
	private DatagramSocket client;
	public CLReceiver(DatagramSocket client) {
		this.client = client;
	}
	@Override
	public void run() {
		try {
			while (true) {
				byte buffer[]=new byte[65507];
				DatagramPacket datagramPacket=new DatagramPacket(buffer,buffer.length);
				client.receive(datagramPacket);
            	String msg=new String(datagramPacket.getData(),0,datagramPacket.getLength());
            	System.out.println(msg);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
class CLSender extends Thread {
	private DatagramSocket client;
	private InetAddress host;
	private int port;
	private String name;
	public CLSender(DatagramSocket client, String name, InetAddress host, int port) {
		this.client = client;
		this.host = host;
		this.port = port;
		this.name = name;
	}
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			try {
				String input = sc.nextLine();
				if (input.equals("quit")) {
					String msg = name + " out!";
	        		byte[] data = msg.getBytes();
	        		DatagramPacket datagramPacket=new DatagramPacket(data,data.length,host,port);
	        		client.send(datagramPacket);
					break;
				}
				String msg = name + ": " + input;
        		byte[] data = msg.getBytes();
        		DatagramPacket datagramPacket=new DatagramPacket(data,data.length,host,port);
        		client.send(datagramPacket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
}