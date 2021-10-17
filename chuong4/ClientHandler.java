package chuong4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
	
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String userName;
	
	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.userName = "User " + baitap3_SS_server.userNo;
			baitap3_SS_server.userNo += 1;
			clientHandlers.add(this);
			broadCastMessage("I'm joined the chat!");
		} catch (IOException e) {
			closeEveryThing(socket, bufferedReader, bufferedWriter);
		}
	}
	
	@Override
	public void run() {
		String messageFromClient;
		
		while(socket.isConnected()) {
			try {
				messageFromClient = bufferedReader.readLine();
				broadCastMessage(messageFromClient);
			} catch (IOException e) {
				closeEveryThing(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
		
	}
	
	public void broadCastMessage(String msg) {
		for (ClientHandler clientHandler: clientHandlers) {
			try {
				if (!clientHandler.userName.equals(this.userName)) {
					clientHandler.bufferedWriter.write(this.userName + ": " + msg);
					clientHandler.bufferedWriter.newLine();
					clientHandler.bufferedWriter.flush();
				}
			} catch (IOException e) {
				closeEveryThing(socket, bufferedReader, bufferedWriter);
			}
		}
	}
	
	public void removeClientHandler() {
		clientHandlers.remove(this);
		broadCastMessage("SERVER: " + this.userName + " has left the chat!");
	}
	
	public void closeEveryThing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		removeClientHandler();
		try {
			if (socket != null) {
				socket.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

