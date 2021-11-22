package chuong5.baitap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		try {
			Socket s = new Socket("localhost", 2007);
			
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			PrintWriter clientOutput = new PrintWriter(s.getOutputStream());
			
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			BufferedReader serverOutput = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			System.out.println("=====Client da ket noi den Server!=====");
			System.out.println("===========Tra cuu diem thi============");

			  
			//userinput
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			
			while(true) {
				System.out.println();
				System.out.print("===> Ho Ten ('quit' de thoat): ");
				String fullname = userInput.readLine();
				if (fullname.equalsIgnoreCase("quit")) {
					System.out.println("===============Ket thuc================");
					clientOutput.println("quit");
					clientOutput.flush();
					break;
				}
				System.out.print("===> So Bao Danh ('quit' de thoat): ");
				String sbd = userInput.readLine();
				System.out.println();
				if (fullname.equalsIgnoreCase("quit")) {
					System.out.println("===============Ket thuc================");
					clientOutput.println("quit");
					clientOutput.flush();
					break;
				}
				
				clientOutput.println("continue");
				clientOutput.flush();
				
				//create thisinh obj
				ThiSinh tssend = new ThiSinh(fullname, sbd);
				
				//send to server
				oos.writeObject(tssend);
				oos.flush();
				
				//get message from server
				ThiSinh tsreceive = null;
				tsreceive = (ThiSinh) ois.readObject();
				
				//display result
				if (tsreceive != null) System.out.println("===> Server: \n" + tsreceive.toString() + "\n============");
				else System.out.println("===> error: Ho Ten hoac So Bao Danh khon ton tai!");
			}
			s.close();
		} catch(IOException ie){
			System.out.println("Loi: Khong tao duoc socket");
		}
	}

}
