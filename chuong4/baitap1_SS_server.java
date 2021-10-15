package chuong4;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class baitap1_SS_server {
	public static String numberToVietnameseWords(int number) {
		String result = null;

		String[] hangDonVi = {
			"khong",
			"mot",
			"hai",
			"ba",
			"bon",
			"nam",
			"sau",
			"bay",
			"tam",
			"chin"
		};
		
		int number_donvi = number % 10;
		int number_chuc = (number/10) % 10;
		//int number_tram = (number/100) % 10;
		
		String result_donvi = hangDonVi[number_donvi];
		if (number_donvi == 5 && number_chuc > 0) result_donvi = "lam";
		if (number_donvi == 1 && number_chuc > 1) result_donvi = "mot";
		
		result = hangDonVi[number_donvi] ;
		
		if (number_chuc > 1 && number_donvi > 0) result = hangDonVi[number_chuc] + " muoi " + result_donvi;
		else if (number_chuc > 1 && number_donvi == 0) result = hangDonVi[number_chuc] + " muoi";
		else if (number_chuc == 1 && number_donvi > 0) result = "muoi " + result_donvi;
		else if (number_chuc == 1 && number_donvi == 0) result = "muoi";
			
		//if (number_tram > 0) result = hangDonVi[number_tram] + " trăm " + hangDonVi[number_chuc] + " mươi " + result_donvi;
		
		
		return result;
	}
	
	public final static int defaultPort = 2007;
	public static int clientOnLine=0;
	static int clientNo=0;
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(defaultPort);
			while (true) {
				try {
					System.out.println("Server dang hoat dong!");
					System.out.println("---------------------------------------------------");
					Socket s = ss.accept();
					clientNo++;
					System.out.println("---------------------------------------------------");
					System.out.println("Khoi dong luong cho Client[" + clientNo + "]");
					RequestProcessing rp = new RequestProcessing(s);
					rp.start();
					rp.clientNo=clientNo;
					baitap1_SS_server.clientOnLine++;
					System.out.println("So client online hien tai la: " + baitap1_SS_server.clientOnLine);
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

class RequestProcessing extends Thread {
	Socket skc;
	public int messageNo=1;
	public int clientNo=0;
	public RequestProcessing(Socket s) {
		skc = s;
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
					System.out.println("Client[" + clientNo + "]" + " is Disconnected");
					baitap1_SS_server.clientOnLine--;
					System.out.println("So client online hien tai la: " + baitap1_SS_server.clientOnLine);
					System.out.println();
					break;
				}
				
				System.out.println("Client[" + clientNo + "]" + " goi Message thu " + messageNo);
				messageNo++;

				//-----main processing----//
				//convert string input to integer
				try {
					int clientOutput_int = Integer.parseInt(clientOutput_str);
					//send to client
					String words = baitap1_SS_server.numberToVietnameseWords(clientOutput_int);
					serverOutput.println(words);
					System.out.println("Server >: " + words +" (to Client[" + clientNo + "])");
				} catch (Exception e) {
					// TODO: handle exception
					//System.out.println("Không phải số nguyên");
					serverOutput.println("Khong phai so nguyen");
					System.out.println("Server >: Khong phai so nguyen (to Client[" + clientNo + "])");
				}
				serverOutput.flush();
				//------------------------//
			}
			skc.close();
		} catch (IOException e) {
			System.out.println(" Connection Error: "+e);
		}
	}
}
