package chuong4.udp.baitap1;

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
    private boolean stopped=false;
    private int port;
	public CLSender(InetAddress address, int port) throws SocketException {
        this.server=address;
	    this.port=port;
	    this.datagramSocket=new DatagramSocket();
	    this.datagramSocket.connect(server,port);
	}
    public void halt() {
        this.stopped=true;
    }
    public DatagramSocket getSocket() {
        return this.datagramSocket;
    }
    public void run() {
        try {
            BufferedReader userInput=new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                if(stopped) return;
        		String line=userInput.readLine();
        		byte[] data=line.getBytes();
        		DatagramPacket datagramPacket=new DatagramPacket(data,data.length,server,port);
        		datagramSocket.send(datagramPacket);
                if(line.equals("quit")){
                    System.out.println("Client kết thúc");
                    break;
                }
                Thread.yield();
            }
    	}
    	catch(IOException e) {
            System.err.println("error: "+e);
    	}
    }
}

class CLReceiver extends Thread {
    private DatagramSocket datagramSocket;
    private boolean stopped = false;	
	public CLReceiver(DatagramSocket datagramSocket) throws SocketException {
        this.datagramSocket = datagramSocket;
	}
    public void halt() {
        this.stopped=true;
    }
    public void run() {
        byte buffer[]=new byte[65507];
        while(true) {
            if(stopped) return;
            DatagramPacket datagramPacket=new DatagramPacket(buffer,buffer.length);
            try {
            	datagramSocket.receive(datagramPacket);
            	String s=new String(datagramPacket.getData(),0,datagramPacket.getLength());
                if(s.equals("quit")) break;
                System.out.println(s);
                Thread.yield();
            }
            catch(IOException e) {
                System.err.println("error: "+e);
            }
    	}
    }
}
