package chuong4.udp.baitap4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	public final static int DEFAULT_PORT = 2007;
	public static boolean isValid = false;
	public static boolean isOut = false;
	public static void main(String[] args) {
        String hostname="localhost";
    	if(args.length>0) {
    		hostname=args[0];
    	}
    	try {
            InetAddress ia=InetAddress.getByName(hostname);
            CLSender sender=new CLSender(ia,DEFAULT_PORT);
            sender.start();
            CLReceiver receiver=new CLReceiver(sender.getSocket());
            receiver.start();
    	}
    	catch(UnknownHostException e) {
            System.err.println(e);
    	}
    	catch(SocketException e) {
            System.err.println(e);
    	}
	}
}
class CLSender extends Thread {
    private InetAddress server;
    private DatagramSocket datagramSocket;
    private int port;

	public CLSender(InetAddress address, int port) throws SocketException {
        this.server=address;
	    this.port=port;
	    this.datagramSocket=new DatagramSocket();
	    this.datagramSocket.connect(server,port);
	}
    public DatagramSocket getSocket() {
        return this.datagramSocket;
    }
    public void run() {
    	BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
    	while(!Client.isValid) {
            try {
				System.out.print("1.Sign in\n2.Sign up\n3.exit\n");
				System.out.print(">: ");
                String choice = userInput.readLine();
                if(choice.equals("1")) {
                	System.out.print("Enter your username: ");
					String username = userInput.readLine();
					System.out.print("Enter your password: ");
					String password = userInput.readLine();
					String msg = "1" + username + "|" + password;
					byte[] data = msg.getBytes();
            		DatagramPacket datagramPacket=new DatagramPacket(data,data.length,server,port);
            		datagramSocket.send(datagramPacket);
                } else if (choice.equals("2")) {
                	System.out.print("Enter your username: ");
					String username = userInput.readLine();
					System.out.print("Enter your password: ");
					String password = userInput.readLine();
					String msg = "2" + username + "|" + password;
					byte[] data = msg.getBytes();
            		DatagramPacket datagramPacket=new DatagramPacket(data,data.length,server,port);
            		datagramSocket.send(datagramPacket);
                } else if (choice.equals("3")) {
                	System.out.println("Exit");
                	String msg = "quit";
                	byte[] data = msg.getBytes();
            		DatagramPacket datagramPacket=new DatagramPacket(data,data.length,server,port);
            		datagramSocket.send(datagramPacket);
            		Client.isOut = true;
            		break;
                } else {
					System.out.println("[1 | 2 | 3] please");
				}
                sleep(100);
        	}
        	catch(IOException | InterruptedException e) {
                System.err.println("error: "+e);
        	}
    	}
        while(!Client.isOut) {
        	System.out.print("> (quit de thoat): ");
        	String userInput_string;
			try {
				userInput_string = userInput.readLine();
				if (userInput_string.equalsIgnoreCase("quit")) {
	            	String msg = "quit";
	            	byte[] data = msg.getBytes();
	        		DatagramPacket datagramPacket=new DatagramPacket(data,data.length,server,port);
					datagramSocket.send(datagramPacket);
	        		Client.isOut = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		System.out.println();
		System.out.println("Client ngat ket noi!");
    }
}

class CLReceiver extends Thread {
    private DatagramSocket datagramSocket;
	public CLReceiver(DatagramSocket datagramSocket) throws SocketException {
        this.datagramSocket = datagramSocket;
	}
    public void run() {
        byte buffer[]=new byte[65507];
        while(true) {
            DatagramPacket datagramPacket=new DatagramPacket(buffer,buffer.length);
            try {
            	datagramSocket.receive(datagramPacket);
            	String s=new String(datagramPacket.getData(),0,datagramPacket.getLength());
                if(s.equals("quit")) break;
                else if (s.startsWith("1")) {
                	if (s.substring(1).equals("true")) {
                    	System.out.println("You have joined the server ");
                    	Client.isValid = true;
                	} else {
                		System.out.println("Your username or password is incorrect");
                	}
                } else if (s.startsWith("2")) {
                	if (s.substring(1).equals("true")) {
                		System.out.println("Sign up successful !");
                    	Client.isValid = true;
                	} else {
                		System.out.println("Server error");
                	}
				}
                //System.out.println(s);
                Thread.yield();
            }
            catch(IOException e) {
                System.err.println("error: "+e);
            }
    	}
    }
}
