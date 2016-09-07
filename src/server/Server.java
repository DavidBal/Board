package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import config.ServerManager;


public class Server extends Thread {

	/**
	 * Server Port
	 */
	int SERVER_PORT;

	/**
	 * Zaehlt die Aufrufe des Workers
	 */
	int zaehler;

	/**
	 * Aktive Verbindungen
	 */
	int activ;

	/**
	 * Main Server Socket
	 */
	ServerSocket socket;
	ServerManager manager;

	/**
	 * Constructor
	 * 
	 * @param SERVER_PORT
	 */
	public Server(ServerManager manager) {
		this.SERVER_PORT = manager.getServerPort();
		this.zaehler = 0;
		this.setName("Server_MainThread");
		this.manager = manager;
	}

	/**
	 * Run Funktion nimmt eingehende Verbindungsanfragen an und startet einen
	 * Worker
	 */
	@Override
	public void run() {

		try {
			System.out.println("Server has been startet at IP: " + InetAddress.getLocalHost().getHostAddress()
					+ " ; Port: " + this.SERVER_PORT);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		intialCommanadExcuter();

		try {
			socket = new ServerSocket(this.SERVER_PORT);
			System.err.println("Socket Open");
			while (true) {
				if (ServerManager.debug)
					System.out.println("Waiting for Connection!!  -  " + zaehler);
				Socket client = socket.accept();
				zaehler++;

				if (ServerManager.debug)
					System.out.println("Connection get Create Thread!");
				Thread t = new WorkerThread(client, zaehler, manager);
				t.start();
				manager.onConnect();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//TODO Close Commandexcuter 
	}
	
	private void intialCommanadExcuter(){
		Thread cE = new CommandsExcuter(this.manager, this);
		cE.start();
	}

}
