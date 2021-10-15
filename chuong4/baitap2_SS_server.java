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

public class baitap2_SS_server {
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
					RequestProcessing_bt2 rp = new RequestProcessing_bt2(s);
					rp.start();
					rp.clientNo=clientNo;
					baitap2_SS_server.clientOnLine++;
					System.out.println("So client online hien tai la: " + baitap2_SS_server.clientOnLine);
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

class RequestProcessing_bt2 extends Thread {
	Socket skc;
	public int messageNo=1;
	public int clientNo=0;
	public RequestProcessing_bt2(Socket s) {
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
					baitap2_SS_server.clientOnLine--;
					System.out.println("So client online hien tai la: " + baitap2_SS_server.clientOnLine);
					System.out.println();
					break;
				}
				
				System.out.println("Client[" + clientNo + "]" + " goi Message thu " + messageNo + ": " + clientOutput_str);
				messageNo++;
				
				//-----main processing----//
				int[] operants = baitap2_SS_server.getOperant(clientOutput_str);
				int a = operants[0]; int b = operants[1]; int sum = 0;
				
				switch (baitap2_SS_server.getOperator(clientOutput_str)) {
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
				//------------------------//
			}
			skc.close();
		} catch (IOException e) {
			System.out.println(" Connection Error: "+e);
		}
	}
}
