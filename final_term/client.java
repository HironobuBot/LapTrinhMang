package final_term;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class client {
	Socket sender;

	ObjectOutputStream out;
	ObjectInputStream in;

	int SWS = 1;
	int right = SWS, left = 0, Number_of_pkt = 5;
	String pkt;
	String ack_received;
	int current_ack = 0, last_ack = -1;
	char data = ' ';
	int timeout;
	boolean complete = false;

	client() {

	}

	public void ReceiveFrames() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (current_ack <= Number_of_pkt) {
					try {
						ack_received = (String) in.readObject();
						if (Integer.parseInt(ack_received) == Number_of_pkt)
							complete = true;
						last_ack = current_ack;
						current_ack++;
						left++;
						right++;
						System.out.println("ack received : " + ack_received);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void run() throws IOException {
		sender = new Socket("localhost", 2007);

		out = new ObjectOutputStream(sender.getOutputStream());
		in = new ObjectInputStream(sender.getInputStream());

		while (!complete) {
			try {
				System.out.println("START");
				ReceiveFrames();
				while ((current_ack >= left) && (current_ack < right) && (current_ack <= Number_of_pkt)) {
					if (current_ack == last_ack) {
						TimeUnit.SECONDS.sleep(7);
					} else {
						TimeUnit.SECONDS.sleep(2);
					}
					pkt = current_ack + "|" + data;
					try {
						if (!complete) {
							out.writeObject(pkt);
							System.out.println("Sent  " + pkt);

							out.flush();
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
			}
		}

		System.out.println("\nSending complete.");

		in.close();
		out.close();
		sender.close();

		System.out.println("\nConnection Terminated.");
	}

	public static void main(String as[]) throws IOException {
		client c = new client();
		c.run();
	}
}