package chuong4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class baitap2_TT_server {
	
	public static String getOperator(String input) {
    	String operator = "";
    	String operator_pattern = "(\\+|-|\\*|/)";

    	//match operator
    	Pattern pattern_opr = Pattern.compile(operator_pattern);
    	Matcher matcher_opr = pattern_opr.matcher(input);

        if (matcher_opr.find()) operator = matcher_opr.group(0);
        
		return operator;
	}
	
	public static int[] getOperant(String input) {
		int[] operants = {0, 0};
		String operant_pattern = "\\d+";
		
    	//match operant
    	Pattern pattern_opt = Pattern.compile(operant_pattern);
    	Matcher matcher_opt = pattern_opt.matcher(input);
    	
    	int index = 0;
        while (matcher_opt.find()) {
        	operants[index] = Integer.parseInt(matcher_opt.group(0));
        	index++;
        }
        
        return operants;
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
				
				//-------main process-------//
				int[] operants = getOperant(clientOutput_String);
				int a = operants[0]; int b = operants[1]; int sum = 0;
				
				switch (getOperator(clientOutput_String)) {
				case "+":
					sum = a + b;
					break;
				case "-":
					sum = a - b;
					break;
				case "*":
					sum = a * b;
					break;
				case "/":
					sum = a / b;
					break;
				}
				serverOutput.println(sum);
				serverOutput.flush();
				//-------------------------//
			}
			cs.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
