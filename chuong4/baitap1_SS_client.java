package chuong4;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class baitap1_SS_client {

	public static void main(String[] args) {
		try {
			Socket s = new Socket("localhost", 2007);
			System.out.println("Client da ket noi den Server!");
			
			//server output (client input)
			BufferedReader serverOutput = new BufferedReader(new InputStreamReader(s.getInputStream()));
			//client output (server input)
			PrintWriter clientOutput = new PrintWriter(s.getOutputStream());  
			//userinput
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			
			while(true) {
				System.out.println();
				System.out.print("> (exit de thoat) (0 ~ 99): ");
				String userInput_string = userInput.readLine();
				if (userInput_string.equalsIgnoreCase("exit")) {
					clientOutput.println(userInput_string);
					clientOutput.flush();
					break;
				}
				
				//send to server
				clientOutput.println(userInput_string);
				clientOutput.flush();
				
				//System.out.println("Client da goi message thu: "+messageNo + " cho Server!");
				
				//get message from server
				userInput_string = serverOutput.readLine();
				System.out.println("Server >: " + userInput_string);
			}
			System.out.println();
			System.out.println("Client ngat ket noi!");
			s.close();
		} catch(IOException ie){
			System.out.println("Loi: Khong tao duoc socket");
		}
	}

}
