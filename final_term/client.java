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
	static int current_ack = 0;
	static char data = ' ';
	int timeout = 10;
	static boolean resend = false;

	client() {

	}

	public void run() throws IOException {
		sender = new Socket("localhost", 2007);

		out = new ObjectOutputStream(sender.getOutputStream());
		in = new ObjectInputStream(sender.getInputStream());

		while ((current_ack >= left) && (current_ack < right) && (current_ack <= Number_of_pkt)) {
			try {

				pkt = current_ack + "|" + data;

				out.writeObject(pkt);
				System.out.println("Sent  " + pkt);

				out.flush();

				Timer timer = new Timer(timeout, out);
				timer.start();

				System.out.println("Waiting for ack " + pkt);

				ack_received = (String) in.readObject();

				if (Integer.parseInt(ack_received) == current_ack) {
					current_ack++;
					left++;
					right++;
				}
				timer.timer_stop();

				System.out.println("ack received : " + ack_received);
				TimeUnit.SECONDS.sleep(5);
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

class Timer extends Thread {
	private int start, delay;
	private boolean stop;
	private ObjectOutputStream out;

	public Timer(int delay, ObjectOutputStream out) {
		this.start = 0;
		this.delay = delay;
		this.stop = false;
		this.out = out;
	}

	public void timer_stop() {
		this.stop = true;
	}

	@Override
	public void run() {
		int i = start;
		while (!stop) {
			try {
				TimeUnit.SECONDS.sleep(1);
				i++;
				if ((i % delay) == 0) {
					String pkt = client.current_ack + "|" + client.data;
					out.writeObject(pkt);
					System.out.println(delay + " seconds later...");
					System.out.println("Resending  " + pkt);
				}
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}