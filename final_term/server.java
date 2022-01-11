package final_term;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class server {
	ServerSocket server;
	Socket socket = null;

	ObjectOutputStream out;
	ObjectInputStream in;

	String ack, pkt, data = "";
	int nack;

	int RWS = 1;
	int left = 0;
	int right = left + RWS;
	int Number_of_pkt = 5;
	int current_pkt = 0;

	Random rand = new Random();

	server() {
	}

	public void run() throws IOException, InterruptedException {
		server = new ServerSocket(2007);
		socket = server.accept();

		if (socket != null)
			System.out.println("Connection established :");

		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

		while ((current_pkt < right) && (current_pkt >= left) && (current_pkt <= Number_of_pkt)) {
			// System.out.println("current_pkt: " + current_pkt + " left: " + left + "
			// right: " + right);
			try {
				pkt = (String) in.readObject();
				String[] str = pkt.split("\\|");

				ack = str[0];
				data = str[1];

				nack = rand.nextInt(3);
				if (nack != 1) {
					current_pkt++;
					left++;
					right++;
					out.writeObject(ack);
					out.flush();

					System.out.println("sending ack " + ack);
				} else {
					System.out.println("not sending ack " + ack);
				}

			} catch (Exception e) {
			}
		}

		System.out.println("\nSending complete.");

		in.close();
		out.close();
		server.close();

		System.out.println("\nConnection Terminated.");

	}

	public static void main(String args[]) throws IOException, InterruptedException {
		server R = new server();
		R.run();
	}
}