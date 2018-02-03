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
	private int msgNum=0;
	long timeStart;
	boolean started = false; 

	public static void main(String args[]) {
		int	recvPort;

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
			System.out.println("Error in running");
		}

	}

	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
			recvSoc = new DatagramSocket(rp);
		} catch (Exception e) {
			System.out.println("Error in creating Datagram Socket");
		}
//		close = false;

		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		while(true) {
			pacSize = 1024;
			pacData = new byte[pacSize];
			pac = new DatagramPacket(pacData, pacSize);

			try {
				recvSoc.setSoTimeout(30000);
				recvSoc.receive(pac);

			} catch (IOException e) {
				System.out.println("Error IOException: " + e.toString());
				LOG();
				System.exit(-1);
			}
			processMessage(new String(pac.getData(), 0, pac.getLength()));

			if (msgNum == totalMessages - 1){ // || received == totalMessages) {
				System.out.println("Elapse time in millis: " + (System.currentTimeMillis() - timeStart));
				LOG();				
			}
		}
	}


	public void processMessage(String data) {

		MessageInfo msg = null;

		if(!started){
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
		if(receivedMessages == null) {
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];
		}
		msgNum = msg.messageNum;
		

		// TO-DO: Log receipt of the message
		// TO-DO: If this is the last expected message, then identify
		//        any missing messages

		if (msgNum < totalMessages) {
			//receivedMessages.add(msgNum);
			receivedMessages[msgNum]=1;
			//received++;
		}
	}


	private void LOG(){

		close = true;

		String s = "Lost packet numbers: ";
		int count = 0;

		for(int i = 0; i < totalMessages; i++){
			if(receivedMessages[i] != 1){
				count++;
				s = s + (i+1) + ", ";
			}
		}

		if (count == 0){
			s = s + "None";
		}

		System.out.println("LOG:");
		System.out.println("Number of messages received successfully: " + (totalMessages - count));
		System.out.println("of a total of " + totalMessages);
		System.out.println("Number of messages not received: " + count);
		//System.out.println(s);
		System.out.println("END OF LOG.");
		System.exit(-1);

	}

}
