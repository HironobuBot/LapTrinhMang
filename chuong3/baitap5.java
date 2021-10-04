package chuong3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class baitap5 {
	
	static boolean isBlocked(String input) {
		try {
			//read webBlocked
			File myObj = new File("src/chuong3/webBlocked");
	        Scanner myReader = new Scanner(myObj);
	        while (myReader.hasNextLine()) {
	        	String data = myReader.nextLine();
	        	data = data.split("[|]")[1].toString();
	        	if (data.equalsIgnoreCase(input)) return true;
	        }
	        myReader.close();
		} catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
		}
		return false;
	}
	
	static void redirect(String input) {
		try {
			URI uri = new URI("https://" + input +"/");
			java.awt.Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws UnknownHostException {
		/* blocked site:
		 * 20.205.243.166 or www.github.com
		 * 151.101.129.69
		 */
		/* sample site
		 * www.google.com
		 */
		try (Scanner sc = new Scanner(System.in)) {
			
			//get input
			System.out.println("Search (ip or hostname): ");
			String input = sc.nextLine();
			
			//pattern for ip
			String ValidIpAddressRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
			//pattern for hostname
			String ValidHostnameRegex = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$";
			Pattern ipPattern = Pattern.compile(ValidIpAddressRegex);
			Pattern hostPattern = Pattern.compile(ValidHostnameRegex);
			
			//ip matching
			Matcher matcher = ipPattern.matcher(input);
			boolean matchFound = matcher.find();
			
			if (matchFound) {
				if (isBlocked(input)) System.out.println("trang web da bi chan");
				else redirect(input);
			}
			
			//hostname matching
			else {
				matcher = hostPattern.matcher(input);
				matchFound = matcher.find();
				if (matchFound) {
					InetAddress host = InetAddress.getByName(input);
					String hostAddress_string = host.getHostAddress();
					if (isBlocked(hostAddress_string)) System.out.println("trang web da bi chan");
					else redirect(input);
				}
				else System.out.println("not valid");
			}
		}
	}

}
