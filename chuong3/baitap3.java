package chuong3;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class baitap3 {

	public static void main(String[] args) throws UnknownHostException {
		//www.google.com
		//vnexpress.net
		//archive.org
		try (Scanner sc = new Scanner(System.in)) {
			InetAddress localHost = InetAddress.getLocalHost();
			System.out.print("Host: ");
			InetAddress host = InetAddress.getByName(sc.nextLine());
			String localHostAddress_string = localHost.getHostAddress();
			String hostAddress_string = host.getHostAddress();;
			System.out.println("Dia chi host : " + hostAddress_string + "\n"
								+ "Dia chi localhost: " + localHostAddress_string);
		}
	}
}
