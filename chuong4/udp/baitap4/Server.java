package chuong4.udp.baitap4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

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
                ProcessingBT4 proc = new ProcessingBT4(ds, dpc);
                proc.start();
            }
    	}
    	catch(IOException e) {
            System.err.println(e);
        }
	}
}
class ProcessingBT4 extends Thread {
    DatagramSocket datagramSocket;
    DatagramPacket clientDatagramPacket;
    private static String file = "src/chuong4/udp/baitap4/accounts";
    public ProcessingBT4 (DatagramSocket datagramSocket, DatagramPacket datagramPacket) {
		this.datagramSocket = datagramSocket;
		this.clientDatagramPacket = datagramPacket;
	}
    
	static boolean isValid(String input) {
		try {
			File myObj = new File(file);
	        Scanner myReader = new Scanner(myObj);
	        while (myReader.hasNextLine()) {
	        	String data = myReader.nextLine();
	        	if (data.equals(input)) return true;
	        }
	        myReader.close();
		} catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
		}
		return false;
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
    		String usrnpw = input.substring(1);
    		if (input.startsWith("1")) {
    			String msg = "";
    			if (isValid(usrnpw)) {
    				msg = "1true";
    			} else {
    				msg = "1false";
    			}
				byte[] data = msg.getBytes();
				DatagramPacket serverDatagramPacket = new DatagramPacket(data, data.length, clientDatagramPacket.getAddress(), clientDatagramPacket.getPort());
	    		try {
        			datagramSocket.send(serverDatagramPacket);
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
    		} else if (input.startsWith("2")) {
				try {
					String text = input.substring(1) + "\n"; 
					Files.write(Paths.get(file), text.getBytes(), StandardOpenOption.APPEND);
					String msg = "2true";
    				byte[] data = msg.getBytes();
    				DatagramPacket serverDatagramPacket = new DatagramPacket(data, data.length, clientDatagramPacket.getAddress(), clientDatagramPacket.getPort());
    	    		try {
	        			datagramSocket.send(serverDatagramPacket);
	        		} catch (IOException e) {
	        			e.printStackTrace();
	        		}
				} catch (IOException ex) {
					String msg = "2false";
    				byte[] data = msg.getBytes();
    				DatagramPacket serverDatagramPacket = new DatagramPacket(data, data.length, clientDatagramPacket.getAddress(), clientDatagramPacket.getPort());
    				try {
						datagramSocket.send(serverDatagramPacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
    		}
    	}
    }
}
