package chuong4.udp.baitap1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
                ProcessingBT1 proc = new ProcessingBT1(ds, dpc);
                proc.start();
            }
    	}
    	catch(IOException e) {
            System.err.println(e);
        }
	}
}
class ProcessingBT1 extends Thread {
    DatagramSocket datagramSocket;
    DatagramPacket clientDatagramPacket;
    public ProcessingBT1(DatagramSocket datagramSocket, DatagramPacket datagramPacket) {
		this.datagramSocket = datagramSocket;
		this.clientDatagramPacket = datagramPacket;
	}
    
	public String numberToVietnameseWords(int number) {
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
        	int input_int = Integer.parseInt(input);
        	String result = numberToVietnameseWords(input_int);
        	byte[] data = result.getBytes();
    		DatagramPacket serverDatagramPacket = new DatagramPacket(data, data.length, clientDatagramPacket.getAddress(), clientDatagramPacket.getPort());
    		try {
    			datagramSocket.send(serverDatagramPacket);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }
}