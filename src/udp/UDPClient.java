/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc;  //sendSoc is datagram socket for sending, receiving

	public static void main(String[] args) {
		InetAddress	serverAddr = null;
		int			recvPort;
		int 		countTo;
		String 		message;

		// Get the parameters
		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]); //try to get correct internet address
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);


		// TO-DO: Construct UDP client class and try to send messages
		try{
			UDPClient aClient = new UDPClient(recvPort);  //UDPClient constructor
			aClient.testLoop(serverAddr, recvPort, countTo); //UDPClient testloop send
		}
		catch(Exception e){
			System.out.println("Exception: " + e.toString() );
		}

	}

	public UDPClient(int recvPort) {
		// TO-DO: Initialise the UDP socket for sending data
		try {
			sendSoc = new DatagramSocket(recvPort);
		} catch (Exception e) {
			System.out.println("Exception:  " + e.toString() );
		}
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
		int				tries = 0;
		// TO-DO: Send the messages to the server
		for(tries = 0; tries < countTo; tries++) {
			String m = new String(Integer.toString(countTo) + ";" + Integer.toString(tries)); //construct message
			send(m, serverAddr, recvPort);
		}
	}

	private void send(String payload, InetAddress destAddr, int destPort) {
		int				payloadSize = payload.length();
		byte[]				pktData = payload.getBytes();


		// TO-DO: build the datagram packet and send it to the server
		/*DatagramPacket(byte[] buf, int length, InetAddress address, int port)*/
		try {
			DatagramPacket pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);
			sendSoc.send(pkt);
		} catch (Exception e){
			System.out.println("Exception " + e.toString() );
		}
	}
}
