package chuong4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class baitap3_SS_server {
	private ServerSocket serverSocket;
	public static int userNo = 1;
	
	public baitap3_SS_server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public void StartServer() {
		try {
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("A new Client has connected !");
				ClientHandler clientHandler = new ClientHandler(socket);
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		} catch (IOException e) {
			closeServerSocket(serverSocket);
		}
	}
	
	public void closeServerSocket(ServerSocket serverSocket) {
		try {
			if(serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(2007);
		baitap3_SS_server server = new baitap3_SS_server(serverSocket);
		System.out.println("SERVER: starting...");
		server.StartServer();
	}

}
