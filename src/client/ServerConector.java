package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import config.ClientManager;
import dataOrga.ControllCalls;
import dataOrga.Message;
import dataOrga.User;

public class ServerConector {

	private String name = "";
	private int serverPort;
	private InetAddress serverIP;

	public boolean ereichbar = false;
	
	private Socket socket;

	private BufferedReader in;
	private PrintWriter out;

	public int getServerPort() {
		return serverPort;
	}

	public InetAddress getServerIP() {
		return serverIP;
	}

	/**
	 * 
	 * @param serverIP
	 * @param serverPort
	 * @throws UnknownHostException
	 */
	public ServerConector(String serverIP, int serverPort) throws UnknownHostException {

		this.serverIP = InetAddress.getByName(serverIP);
		
		this.serverPort = serverPort;
		
		this.ereichbar = true;

	}

	/**
	 * 
	 * @param serverIP
	 * @param serverPort
	 * @param name
	 * @throws UnknownHostException
	 */
	public ServerConector(String serverIP, int serverPort, String name) throws UnknownHostException {
		this.serverIP = InetAddress.getByName(serverIP);
		this.serverPort = serverPort;
		this.name = name;
	}

	/**
	 * Connecting to the Server If everything goes right connected = TRUE
	 * 
	 */
	public void connect() throws IOException {
		System.err.println(this.toString());

		this.socket = new Socket(this.serverIP, serverPort);
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream(), true);

	}

	/**
	 * @throws IOException
	 * 
	 */
	public void disconnect() throws IOException {
		this.socket.close();
	}

	/**
	 * Send a "Ping" to the Server and if he work correct the Server will return
	 * a "Pong"!
	 * 
	 * @return TRUE = Server Okay ; FALSE = Server fail
	 * @throws ConnectException
	 * 
	 */
	public boolean ping() throws IOException {
		this.connect();
		this.out.println(ControllCalls.Ping.toString());

		String tmp = "";

		tmp = this.in.readLine();

		System.out.println("Ping  :  " + tmp);

		if (tmp.equals("Pong")) {
			return true;
		}
		this.disconnect();
		return false;
	}

	/**
	 * Sendet eine Nachricht an den Server.
	 * 
	 * @param msg
	 * @throws ConnectException
	 */
	public void sendNewMessage(Message msg) throws IOException {
		this.connect();

		this.out.println(dataOrga.ControllCalls.NEWMESSAGE);
		//
		this.out.println(msg.toString());
		//
		this.out.println(dataOrga.ControllCalls.END);

		this.disconnect();
	}

	/**
	 * Sendet eine Update auffoderung an den Server.
	 * 
	 * @param manager
	 * @throws ConnectException
	 */
	public void update(ClientManager manager) throws IOException {
		this.connect();

		manager.deleteAllMessage(); // TODO Besser

		this.out.println(dataOrga.ControllCalls.UPDATE);

		String input;

		try {
			input = in.readLine();

			while (!input.equals(dataOrga.ControllCalls.END.toString())) {
				manager.addMessage(Message.stringToMessage(input));
				input = in.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.disconnect();
	}

	// TODO Make this Function usefull
	/**
	 * @throws ConnectException
	 * 
	 */
	public void identifyUser(User user) throws IOException {
		this.connect();

		this.out.println(dataOrga.ControllCalls.LOGIN);
		this.out.println(user.getName());
		this.out.println(user.getPw());
		this.out.println(dataOrga.ControllCalls.END);

		try {
			user.setBerechtigung(Integer.valueOf(this.in.readLine()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.disconnect();
	}

	public boolean addUser(User user) throws IOException {
		boolean anlegen_erfolgreich = false;

		this.connect();

		this.out.println(ControllCalls.ADDUSER.toString());
		this.out.println(user.getName());
		this.out.println(user.getPw());
		this.out.println(user.getBerechtigung());

		try {
			String s = this.in.readLine();
			anlegen_erfolgreich = Boolean.valueOf(s);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.disconnect();
		return anlegen_erfolgreich;
	}

	public boolean deleteMessage(Message msg) throws IOException {

		boolean loeschen_erfolgreich = false;

		this.connect();

		this.out.println(ControllCalls.DELETEMSG.toString());
		this.out.println(msg.toString());

		try {
			loeschen_erfolgreich = Boolean.valueOf(this.in.readLine());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return loeschen_erfolgreich;
	}

	@Override
	public String toString() {
		String tmp = this.name + ":" + this.serverIP.getHostName() + ":" + this.serverPort;
		return tmp;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ServerConector) {
			ServerConector other = (ServerConector) o;
			if (this.name.equals(other.name)) {
				if (this.serverIP.equals(other.serverIP)) {
					if (this.serverPort == other.serverPort) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
