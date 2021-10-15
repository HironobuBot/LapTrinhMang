package chuong4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class baitap2_TT_client {
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
			
			while (true) {
				System.out.print("> (exit de thoat): ");
				String userInput_String = userInput.readLine();
				if (userInput_String.equalsIgnoreCase("exit")) {
					clientOutput.println(userInput_String);
					clientOutput.flush();
					break;
				}
				//-------main process--------//
				//send to server
				clientOutput.println(inputConverter(userInput_String)); 
				clientOutput.flush(); //
				//---------------------------//
				
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
