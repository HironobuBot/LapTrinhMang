package chuong4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
//import java.nio.charset.StandardCharsets;

public class baitap1_TT_client {

	public static void main(String[] args) {
		//output from server
		BufferedReader serverOutput = null; 
		//client output
		PrintWriter clientOutput = null;
		
		try {
			Socket cs = new Socket("localhost", 2007);
			System.out.println("Da ket noi voi server !");
			
			serverOutput = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			clientOutput = new PrintWriter(cs.getOutputStream());
			//user input
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			
			//utf-8 consoleOut
			//PrintWriter consoleOut = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"));
			
			while (true) {
				System.out.print("> (0 ~ 99): ");
				
				String userInput_String = userInput.readLine();
				if (userInput_String.equalsIgnoreCase("exit")) {
					clientOutput.println(userInput_String); 
					clientOutput.flush();
					break;
				}
				
				//send to server
				clientOutput.println(userInput_String); 
				clientOutput.flush(); //

				//print server output
				System.out.println("Server >: " + serverOutput.readLine());
			}
			cs.close();	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
