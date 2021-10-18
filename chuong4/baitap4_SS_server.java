package chuong4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class baitap4_SS_server {

	public final static int defaultPort = 2007;
	public static int clientOnLine=0;
	static int clientNo=0;
	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	public static LocalDateTime now = LocalDateTime.now();
	public static String currentDate = dtf.format(now);
	public static void main(String[] args) throws IOException {
		try {
			ServerSocket ss = new ServerSocket(defaultPort);
			while (true) {
				try {
					System.out.println("Server dang hoat dong!");
					System.out.println("---------------------------------------------------");
					Socket s = ss.accept();
					clientNo++;
					System.out.println("---------------------------------------------------");
					System.out.println(currentDate + " Khoi dong luong cho Client[" + clientNo + "]");
					RequestProcessing_bt4 rp = new RequestProcessing_bt4(s);
					rp.start();
					rp.clientNo=clientNo;
					baitap4_SS_server.clientOnLine++;
					System.out.println(currentDate + " So client online hien tai la: " + baitap4_SS_server.clientOnLine);
					System.out.println("---------------------------------------------------");
				} catch (IOException e) {
					System.out.println(" Connection Error: "+e);
				}
			}
		} catch (IOException e) {
			System.out.println("Creation Socket Error:"+e);
		}
	}

}

class RequestProcessing_bt4 extends Thread {
	Socket skc;
	public int messageNo=1;
	public int clientNo=0;
	//public boolean isValid = false;
	private static String file = "src/chuong4/accounts";
	public RequestProcessing_bt4(Socket s) {
		skc = s;
	}
	
	static boolean isValid(String input) {
		try {
			File myObj = new File(file);
	        Scanner myReader = new Scanner(myObj);
	        while (myReader.hasNextLine()) {
	        	String data = myReader.nextLine();
	        	if (data.equals(input)) return true;
	        }
	        myReader.close();
		} catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
		}
		return false;
	}
	
	public  void run()	{
		try {
			//server output (client input)
			PrintWriter serverOutput = new PrintWriter(new OutputStreamWriter(skc.getOutputStream())); 
			//client output (server input)
			BufferedReader clientOutput = new BufferedReader(new InputStreamReader(skc.getInputStream()));
			while(true) {
				System.out.println();
				//get message from client
				String clientOutput_str = clientOutput.readLine();
				if(clientOutput_str.equals("exit"))  {
					serverOutput.println(clientOutput_str);
					System.out.println(baitap4_SS_server.currentDate + " Client[" + clientNo + "]" + " is Disconnected");
					baitap4_SS_server.clientOnLine--;
					System.out.println(baitap4_SS_server.currentDate + " So client online hien tai la: " + baitap4_SS_server.clientOnLine);
					System.out.println();
					break;
				}
				
				System.out.println(baitap4_SS_server.currentDate + " Client[" + clientNo + "]" + " sent message no." + messageNo + ": " + clientOutput_str);
				messageNo++;
				
				//-----main processing----//
				String usrnpw = clientOutput_str.substring(1);
				if (clientOutput_str.startsWith("1")) {
					System.out.println(baitap4_SS_server.currentDate + " Client[" + clientNo + "]" + " sign in");
					if (isValid(usrnpw)) {
						serverOutput.println("true");
						serverOutput.flush();
					} else {
						serverOutput.println("false");
						serverOutput.flush();
					}
				} else if (clientOutput_str.startsWith("2")) {
					System.out.println(baitap4_SS_server.currentDate + " Client[" + clientNo + "]" + " sign up");
					try {
						String text = clientOutput_str.substring(1) + "\n"; 
						Files.write(Paths.get(file), text.getBytes(), StandardOpenOption.APPEND);
						serverOutput.println("true");
						serverOutput.flush();
					} catch (IOException e) {
						serverOutput.println("false");
						serverOutput.flush();
					}
				}
				//------------------------//
			}
			skc.close();
		} catch (IOException e) {
			System.out.println(" Connection Error: "+e);
		}
	}
}
