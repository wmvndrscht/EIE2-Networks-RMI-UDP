/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if(receivedMessages == null){
			totalMessages = msg.totalMessages;
			receivedMessages = new int[msg.totalMessages];
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum >= totalMessages -1){
			LOG();
		}

	}


	public static void main(String[] args) {

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if(System.getSecurityManager() == null){
			System.setSecurityManager(new SecurityManager());
		}
		String urlServer = new String("rmi://" + "localhost" + "/RMIServer");

		// TO-DO: Instantiate the server class
		// TO-DO: Bind to RMI registry
		try {
			// default registry port 1099
			RMIServer myserver = new RMIServer();
			rebindServer(urlServer, myserver); // ?
		}
		catch (RemoteException e) {
			System.out.println("RemoteException: "+ e.toString() );
			System.exit(-1);
		}

		System.out.println("Waiting..........");


	}

	protected static void rebindServer(String serverURL, RMIServer server) throws RemoteException {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		try{
			LocateRegistry.createRegistry(1099); // ?
		}
		catch(RemoteException e){
			System.out.println("RemoteException: "+ e.toString() );
		}

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		try{
			Naming.bind(serverURL, server);
		} catch (AlreadyBoundException e){
			System.out.println("AlreadyBoundException: "+ e.toString() );
		} catch (RemoteException e){
			System.out.println("RemoteException: "+ e.toString() );
		} catch(MalformedURLException e){
			System.out.println("MalformedURLException: "+ e.toString() );
		} catch(Exception e){
			System.out.println("Exception: "+ e.toString() );
		}
	}

	private void LOG(){

		String s = "Specific messages lost: ";
		int messagelostcount = 0;
		for(int i = 0; i < totalMessages; i++){
			if(receivedMessages[i] != 1){
				messagelostcount++;
				s = s + ";" + (i+1) + ", ";
			}
		}

		if(messagelostcount == 0){
			s = s + "0";
		}

		System.out.println("Log Report:");
		System.out.println("Sent:  " + totalMessages + " Received: " + (totalMessages - messagelostcount)
		+ " Lost: " + messagelostcount);
		System.out.println("Error rate: " + ((totalMessages - messagelostcount))/totalMessages);
		System.out.println(s);
		System.out.println("LOG END");
		System.exit(0);

	}
}
