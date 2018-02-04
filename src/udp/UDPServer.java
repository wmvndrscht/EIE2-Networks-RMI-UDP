/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;
	private int msgNum = 0;
	long timeStart;
	boolean started = false;


	private void run() {
		int pacSize;
		byte[] pacData;
		DatagramPacket pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		while (!close) {
			pacSize = 1024;
			pacData = new byte[pacSize];
			pac = new DatagramPacket(pacData, pacSize);

			try {
				recvSoc.setSoTimeout(5000);
				recvSoc.receive(pac);
				processMessage(new String(pac.getData(), 0, pac.getLength()));

				if (msgNum >= totalMessages - 1) {
					System.out.println("Elapse time in ms: " + (System.currentTimeMillis() - timeStart));
					LOG();
				}

			} catch (Exception e) {
				System.out.println("Exception: " + e.toString());
				LOG();
				System.exit(-1);
			}

		}
	}


	public void processMessage(String data) {

		MessageInfo msg = null;

		if (!started) {
			started = true;
			timeStart = System.currentTimeMillis();
		}

		// TO-DO: Use the data tSystem.exit(-1);o construct a new MessageInfo object
		try {
			msg = new MessageInfo(data);
		} catch (Exception e) {
			System.out.println("Message info: " + msg.totalMessages);
			System.out.println("Error: " + e);
		}

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (receivedMessages == null) {
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];
		}
		msgNum = msg.messageNum;


		// TO-DO: Log receipt of the message
		// TO-DO: If this is the last expected message, then identify
		//        any missing messages

		if (msgNum < totalMessages) {
			receivedMessages[msgNum] = 1;
		}
	}



	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
			recvSoc = new DatagramSocket(rp);
		} catch (Exception e) {
			System.out.println("Error in creating Datagram Socket " + e.toString());
		}
//		close = false;

		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer aServer = new UDPServer(recvPort);

		try {
			aServer.run();
		} catch (Exception e) {
			System.out.println("Error in running: " + e.toString());
		}

	}


	private void LOG() {

		close = true;
		String s = "Specific messages lost: ";
		int messagelostcount = 0;
		for (int i = 0; i < totalMessages; i++) {
			if (receivedMessages[i] != 1) {
				messagelostcount++;
				s = s + ";" + (i + 1) + ", ";
			}
		}

		if (messagelostcount == 0) {
			s = "None";
		}

		System.out.println("LOG REPORT:");
		System.out.println("SENT:  " + totalMessages + " RECEIVED: " + (totalMessages - messagelostcount)
				+ " LOST: " + messagelostcount);
		System.out.println("Error rate: " + ((double)(messagelostcount)) / (double)(totalMessages));
		System.out.println(s);
		System.out.println("LOG END");
		System.exit(0);

	}

}
		}

		System.out.println("LOG REPORT:");
		System.out.println("SENT:  " + totalMessages + " RECEIVED: " + (totalMessages - messagelostcount)
				+ " LOST: " + messagelostcount);
		System.out.println("Error rate: " + ((double)(messagelostcount)) / (double)(totalMessages));
		System.out.println(s);
		System.out.println("LOG END");
		System.exit(0);

	}

}
