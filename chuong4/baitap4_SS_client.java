package chuong4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class baitap4_SS_client {
	private static boolean isValid = false;
	private static boolean isOut = false;
	public static void main(String[] args) throws UnknownHostException, IOException {
		try {
			Socket s = new Socket("localhost", 2007);
			System.out.println("Client da ket noi den Server!");
			
			//server output (client input)
			BufferedReader serverOutput = new BufferedReader(new InputStreamReader(s.getInputStream()));
			//client output (server input)
			PrintWriter clientOutput = new PrintWriter(s.getOutputStream());  
			//userinput
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			
			while(!isValid) {
				System.out.print("1.Sign in\n2.Sign up\n3.exit\n");
				System.out.print(">: ");
				String choice = userInput.readLine();
				if(choice.equals("1")) {
					System.out.print("Enter your username: ");
					String username = userInput.readLine();
					System.out.print("Enter your password: ");
					String password = userInput.readLine();
					clientOutput.println("1" + username + "|" + password); 
					clientOutput.flush();
					if(serverOutput.readLine().equalsIgnoreCase("true")) {
						System.out.println("You have joined the server ");
						isValid = true;
					} else {
						System.out.println("Your username or password is incorrect");
					}
				} else if (choice.equals("2")) {
					System.out.print("Enter your username: ");
					String username = userInput.readLine();
					System.out.print("Enter your password: ");
					String password = userInput.readLine();
					clientOutput.println("2" + username + "|" + password); 
					clientOutput.flush();
					if(serverOutput.readLine().equalsIgnoreCase("true")) {
						System.out.println("Sign up successful !");
						isValid = true;
					} else {
						System.out.println("Server error");
					}
				} else if (choice.equals("3")) {
					clientOutput.println("exit");
					clientOutput.flush();
					isOut = true;
					break;
				} else {
					System.out.println("1 or 2 please");
				}
			}
			
			while(!isOut) {
				
				System.out.print("> (exit de thoat): ");
				String userInput_string = userInput.readLine();
				
				if (userInput_string.equalsIgnoreCase("exit")) {
					clientOutput.println(userInput_string);
					clientOutput.flush();
					isOut = true;
				}
				
				//send to server
				//clientOutput.println(userInput_string); 
				//clientOutput.flush();
				
				//get message from server
				//System.out.println("Client >: " + userInput_string);
				//System.out.println("Server >: " + serverOutput.readLine());
				
			}
			System.out.println();
			System.out.println("Client ngat ket noi!");
			s.close();
		} catch(IOException ie){
			System.out.println("Loi: Khong tao duoc socket");
		}
	}

}
