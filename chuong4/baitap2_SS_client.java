package chuong4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class baitap2_SS_client {
	
	public static String inputConverter(String input) {
		int[] operants = {0, 0};
    	String operator = "";
    	String operant_pattern = "\\d+";
    	String operator_pattern = "(\\+|-|\\*|/)";
    	
    	//match operant
    	Pattern pattern_opt = Pattern.compile(operant_pattern);
    	Matcher matcher_opt = pattern_opt.matcher(input);
    	//match operator
    	Pattern pattern_opr = Pattern.compile(operator_pattern);
    	Matcher matcher_opr = pattern_opr.matcher(input);
    	
    	int index = 0;
        while (matcher_opt.find()) {
        	operants[index] = Integer.parseInt(matcher_opt.group(0));
        	index++;
        }
        if (matcher_opr.find()) operator = matcher_opr.group(0);
        String result = operator + " " + operants[0] + " " + operants[1];
        
		return result;
	}

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
				System.out.print("> (exit de thoat): ");
				String userInput_string = userInput.readLine();
				
				if (userInput_string.equalsIgnoreCase("exit")) {
					clientOutput.println(userInput_string);
					clientOutput.flush();
					break;
				}
				//send to server
				clientOutput.println(inputConverter(userInput_string)); 
				clientOutput.flush();
				
				//System.out.println("Client da goi message thu: "+messageNo + " cho Server!");
				
				//get message from server
				System.out.println("Client >: " + inputConverter(userInput_string));
				System.out.println("Server >: " + serverOutput.readLine());
				
			}
			System.out.println();
			System.out.println("Client ngat ket noi!");
			s.close();
		} catch(IOException ie){
			System.out.println("Loi: Khong tao duoc socket");
		}
	}

}
