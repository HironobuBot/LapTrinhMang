package chuong5.baitap1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	public final static int DEFAULT_PORT = 2007;
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(DEFAULT_PORT);
		System.out.println("Server running on port " + DEFAULT_PORT);
		while (true) {
			try {
				System.out.println("=====>Waiting for client...");
				Socket s = ss.accept();
				System.out.println("=====>a client has connect...");
				Processing proc = new Processing(s);
				proc.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//ss.close();
	}
}
class Processing extends Thread {
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private static String file = "src/chuong5/baitap1/database";
	
	public ThiSinh StringToThiSinh(String input) {
		ThiSinh result = new ThiSinh();
		String[] components = input.split("\\|");
		double[] diem = new double[6];
		result.setFullname(components[0]);
		result.setSbd(components[1]);
		for (int i = 0; i < 6; i++) {
			diem[i] = Double.valueOf(components[i+2]);
		}
		result.setDiem(diem);
		return result;
	}
	
	public ThiSinh search(String fullname, String sbd) {
		ThiSinh result = null;
		try {
			File myObj = new File(file);
	        Scanner myReader = new Scanner(myObj);
	        while (myReader.hasNextLine()) {
	        	String record = myReader.nextLine();
	        	String[] components = record.split("\\|");
	        	String name = components[0];
	        	String sobaodanh = components[1];
	        	if (name.equals(fullname) && sobaodanh.equals(sbd)) {
	        		result = StringToThiSinh(record);
	        	}
	        }
	        myReader.close();
		} catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
		}
		return result;
	}
	
	public Processing(Socket s) {
		this.s = s;
		try {
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			ThiSinh tssend = null;
			BufferedReader clientOutput = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (true) {
				try {
					String clientOutput_str = clientOutput.readLine();
					if(clientOutput_str.equals("quit"))  {
						System.out.println("=====>a client has quit");
						break;
					}
					ThiSinh tsreceive = (ThiSinh) ois.readObject();
					
					//display msg from client
					System.out.println("=====>Client sent: \n" + tsreceive.getMsg() + "\n==================");
					
					tssend = search(tsreceive.getFullname(), tsreceive.getSbd());
					oos.writeObject(tssend);
					oos.flush();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			s.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}