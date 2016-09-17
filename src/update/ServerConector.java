package update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import dataOrga.ControllCalls;
import dataOrga.Message;
import dataOrga.User;
import messageSaving.MessageSaver;

//DONE Client kann server Daten abfragen
public class ServerConector {

	private String abteilungsName;

	public String getAbteilungsName() {
		return abteilungsName;
	}

	private int abtID;

	public int getAbtID() {
		return abtID;
	}

	private int serverPort;

	public int getServerPort() {
		return serverPort;
	}

	private InetAddress serverIP;

	public InetAddress getServerIP() {
		return serverIP;
	}

	private Socket socket;

	private BufferedReader in;
	private PrintWriter out;

	/**
	 * 
	 * @param serverIP
	 * @param serverPort
	 * @throws UnknownHostException
	 */
	public ServerConector(String serverIP, int serverPort) throws UnknownHostException {
		this.serverIP = InetAddress.getByName(serverIP);
		this.serverPort = serverPort;
		this.abteilungsName = "Unknown";
		this.abtID = -1;
	}

	/**
	 * 
	 * @param serverIP
	 * @param serverPort
	 * @param name
	 * @throws UnknownHostException
	 */
	public ServerConector(String serverIP, int serverPort, String abteilungsName) throws UnknownHostException {
		this(serverIP, serverPort);
		this.abteilungsName = abteilungsName;
	}

	/**
	 * Connecting to the Server
	 * 
	 */
	public void connect() throws IOException {
		System.err.println(this.toString());

		this.socket = new Socket(this.serverIP, serverPort);
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream(), true);
	}

	/**
	 * Disconnect
	 * 
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
	 * Sendet eine neue Nachricht an den Server.
	 * 
	 * @param msg
	 * @throws ConnectException
	 */
	protected void sendNewMessage(Message msg) throws IOException {

		// TODO Erfolgreich?

		this.connect();

		this.out.println(dataOrga.ControllCalls.NEWMESSAGE);
		msg.sendMessage(out);
		this.out.println(dataOrga.ControllCalls.END);

		this.disconnect();
	}

	/**
	 * Löscht eine Nachricht
	 * 
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	protected boolean deleteMessage(Message msg) throws IOException {

		boolean loeschen_erfolgreich = false;

		this.connect();

		this.out.println(ControllCalls.DELETEMSG.toString());
		msg.sendMessage(out);

		loeschen_erfolgreich = Boolean.valueOf(this.in.readLine());

		return loeschen_erfolgreich;
	}

	/**
	 * Sendet eine Update auffoderung an den Server.
	 * 
	 * @param manager
	 * @throws ConnectException
	 */
	protected void update(MessageSaver messageSaver) throws IOException {
		this.connect();
		synchronized (messageSaver) {

			int marker = 1000000;

			this.out.println(dataOrga.ControllCalls.UPDATE);

			messageSaver.deleteAllMessage(); // TODO Besser

			String input;

			in.mark(marker);
			input = in.readLine();

			while (!input.equals(dataOrga.ControllCalls.END.toString())) {
				in.reset();
				messageSaver.addMessage(Message.stringToMessage(Message.getMessage(in)));
				in.mark(marker);
				input = in.readLine();
			}
		}

		this.disconnect();
	}

	/**
	 * Überträgt den Namen und das Passwort und bekommt die Berechtigung zurück;
	 * Berchtigung Null wenn der User nicht exestiert
	 * 
	 * @param user
	 * @throws IOException
	 */
	public void identifyUser(User user) throws IOException {
		this.connect();

		this.out.println(dataOrga.ControllCalls.LOGIN);
		this.out.println(user.getName());
		this.out.println(user.getPw());
		this.out.println(dataOrga.ControllCalls.END);

		user.setBerechtigung(Integer.valueOf(this.in.readLine()));

		this.disconnect();
	}

	/**
	 * Übrtägt die Daten einen Users der neu angelegt werden soll.
	 * 
	 * @param user
	 * @return
	 * @throws IOException
	 */
	public boolean addUser(User user) throws IOException {
		boolean anlegen_erfolgreich = false;

		this.connect();

		this.out.println(ControllCalls.ADDUSER.toString());
		this.out.println(user.getName());
		this.out.println(user.getPw());
		this.out.println(user.getBerechtigung());

		String s = this.in.readLine();
		anlegen_erfolgreich = Boolean.valueOf(s);

		this.disconnect();
		return anlegen_erfolgreich;
	}

	/**
	 * 
	 * @throws IOException
	 */
	public boolean callServerData() {
		try {
			this.connect();

			this.out.println(ControllCalls.SERVER.toString());

			this.abteilungsName = this.in.readLine();
			this.abtID = Integer.valueOf(this.in.readLine());

			this.disconnect();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean pushMessage(Message msg) throws IOException {
		boolean push = true;

		this.connect();

		this.out.println(ControllCalls.PUSH.toString());

		msg.sendMessage(out);

		this.disconnect();

		return push;
	}

	public void editMessage(Message msg) throws IOException {
		this.connect();

		this.out.println(ControllCalls.EDITMESSAGE.toString());

		msg.sendMessage(out);

		this.disconnect();
	}

	public void pushServerAllMessages(ArrayList<Message> msgs) throws IOException {
		this.connect();

		this.out.println(ControllCalls.SERVERPUSH.toString());

		this.out.println(this.abteilungsName);
		for (Message msg : msgs) {
			msg.sendMessage(out);
		}

		this.out.println(ControllCalls.END);
	}

	@Override
	public String toString() {
		String tmp = this.abteilungsName + ":" + this.serverIP.getHostName() + ":" + this.serverPort;
		return tmp;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ServerConector) {
			ServerConector other = (ServerConector) o;
			if (this.abteilungsName.equals(other.abteilungsName)) {
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
