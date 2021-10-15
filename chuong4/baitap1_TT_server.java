package chuong4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
//import java.nio.charset.StandardCharsets;


public class baitap1_TT_server {
	
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
	
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(2007);
			System.out.println("Server started, waiting for request...");
			
			//a client socket
			Socket cs = null;
			//connect c to s
			cs = ss.accept();
			System.out.println("a Client has been connected !");
			
			//ServerOutput
			PrintWriter serverOutput = new PrintWriter(new OutputStreamWriter(cs.getOutputStream())); 
			//ClientOUtput (server input)
			BufferedReader clientOutput = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			
			while (true) {
				String clientOutput_String = clientOutput.readLine();

				//print client ouput
				System.out.print("\nClient >: " + clientOutput_String);
				if (clientOutput_String.equalsIgnoreCase("exit")) break;
				
				//convert string input to integer
				try {
					int clientOutput_int = Integer.parseInt(clientOutput_String);
					//send to client
					serverOutput.println(numberToVietnameseWords(clientOutput_int));
				} catch (Exception e) {
					// TODO: handle exception
					//System.out.println("Không phải số nguyên");
					serverOutput.println("Khong phai so nguyen");
				}
				serverOutput.flush();
			}
			cs.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
