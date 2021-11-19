package chuong4.udp.baitap2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
	public final static int DEFAULT_PORT = 2007;
	public static void main(String[] args) {
        byte[] buffer=new byte[1000];
    	try {
            DatagramSocket ds= new DatagramSocket(DEFAULT_PORT);
            System.out.println("Server da san sang lang nghe lien ket...");
            DatagramPacket dpc=new DatagramPacket(buffer,buffer.length);
            while (true) {
                ds.receive(dpc);
                ProcessingBT2 proc = new ProcessingBT2(ds, dpc);
                proc.start();
            }
    	}
    	catch(IOException e) {
            System.err.println(e);
        }
	}
}
class ProcessingBT2 extends Thread {
    DatagramSocket datagramSocket;
    DatagramPacket clientDatagramPacket;
    public ProcessingBT2 (DatagramSocket datagramSocket, DatagramPacket datagramPacket) {
		this.datagramSocket = datagramSocket;
		this.clientDatagramPacket = datagramPacket;
	}
    
	public String getOperator(String input) {
    	String operator = "";
    	String operator_pattern = "(\\+|-|\\*|/)";

    	//match operator
    	Pattern pattern_opr = Pattern.compile(operator_pattern);
    	Matcher matcher_opr = pattern_opr.matcher(input);

        if (matcher_opr.find()) operator = matcher_opr.group(0);
        
		return operator;
	}
	
	public int[] getOperant(String input) {
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
    
    @Override
    public void run() {
    	String input = new String(clientDatagramPacket.getData(),0,clientDatagramPacket.getLength());
    	if (input.equals("quit")) {
    		System.out.println("client exit");
    		byte[] data = input.getBytes();
    		DatagramPacket serverDatagramPacket = new DatagramPacket(data, data.length, clientDatagramPacket.getAddress(), clientDatagramPacket.getPort());
    		try {
    			datagramSocket.send(serverDatagramPacket);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	} else {
    		int[] operants = getOperant(input);
    		int a = operants[0]; int b = operants[1]; int result = 0;
    		switch (getOperator(input)) {
			case "+":
				result = a + b;
				break;
			case "-":
				result = a - b;
				break;
			case "*":
				result = a * b;
				break;
			case "/":
				result = a / b;
				break;
			}
    		String res_str = String.valueOf(result);
        	byte[] data = res_str.getBytes();
    		DatagramPacket serverDatagramPacket = new DatagramPacket(data, data.length, clientDatagramPacket.getAddress(), clientDatagramPacket.getPort());
    		try {
    			datagramSocket.send(serverDatagramPacket);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }
}
