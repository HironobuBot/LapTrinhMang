package chuong3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Scanner;

public class baitap6 {
	
//	static String byteToString(byte[] ip_byte) {
//		int[] ip_int = new int[ip_byte.length];
//		for (int i = 0; i < 4; i++)
//			ip_int[i] = ip_byte[i] & 0xFF; //dùng AND (toán tử bit) 
//		String ip_string = ip_int[0] + "." + ip_int[1] + "." + ip_int[2] + "." + ip_int[3];
//		return ip_string;
//	}
	
	static void redirect(String input) {
		try {
			URI uri = new URI(input);
			java.awt.Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("URL: ");
		Scanner sc = new Scanner(System.in);
		
		//get url
		String inputUrl = sc.nextLine();
		
		URL url = new URL(inputUrl);
		String hostName = url.getHost();
		InetAddress addr = InetAddress.getByName(hostName);
		
		//get ip
		String ipString = addr.getHostAddress();
		
		//get date access
		URLConnection urlCon = url.openConnection();
		String date_string = urlCon.getDate() > 0? new Date(urlCon.getDate()).toString() : "0";
		
		//truy cap web
		System.out.println("is accessing...");
		redirect(inputUrl);
		
		//them vao history
		String text = "URL: " + inputUrl + "\n"
					+ "IP: " + ipString + "\n"
					+ "Ngay truy cap: " + date_string + "\n\n";
		Files.write(Paths.get("src/chuong3/webHistory"), text.getBytes(), StandardOpenOption.APPEND);
		System.out.println("Successfully wrote to the file.");
	}
}
