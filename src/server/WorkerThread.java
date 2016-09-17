package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import config.ServerManager;
import dataOrga.ControllCalls;
import dataOrga.Message;
import dataOrga.User;

/**
 * Jede Anfrage eines Clients oeffnet eine neue Thread ...
 * 
 * @author David
 *
 */
public class WorkerThread extends Thread {

	Socket client;
	BufferedReader in;
	PrintWriter out;
	int id;

	ServerManager manager;

	public WorkerThread(Socket client, int id, ServerManager manager) {
		this.client = client;
		this.id = id;
		this.manager = manager;

		this.setName("WorkerThread" + this.id);

		try {
			this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			this.out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace(); // TODO
		}
	}

	@Override
	public void run() {

		try {

			if (ServerManager.debug)
				System.out.println("Worker- " + this.id + ": I‘m UP!!");

			String controllCall = this.in.readLine(); // Lese Befehl

			if (ServerManager.debug)
				System.out.println(controllCall);

			this.controll(controllCall);

			client.close();

		} catch (IOException e) {
			e.printStackTrace(); // TODO
		}

		manager.onFinish();

		if (ServerManager.debug)
			System.out.println("Worker - " + this.id + " END!!");
	}

	private void controll(String controllCall) {
		switch (ControllCalls.stringToControllCall(controllCall)) {
		case Ping:
			this.out.println("Pong");
			break;
		case NEWMESSAGE:
			try {
				String msg = Message.getMessage(in);

				this.manager.getDatabase().addMessage(Message.stringToMessage(msg));
				this.in.readLine();
				// TODO END ??
			} catch (IOException e) {
				// TODO
				e.printStackTrace();
			}

			break;
		case LOGIN:
			try {
				// User Name
				String userName = this.in.readLine();
				// PW
				String passwort = this.in.readLine();

				//
				this.out.println(this.manager.getDatabase().getUserBerechtigung(new User(userName, passwort, 0, 0)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case UPDATE:
			ArrayList<Message> msgs = this.manager.getDatabase().loadMessages(0);
			for (Message msg : msgs) {
				msg.sendMessage(out);
			}

			this.out.println(ControllCalls.END);

			break;
		case ADDUSER:
			try {
				String name = in.readLine();
				String pw = in.readLine();
				String berechtigung = in.readLine();
				User user = new User(name, pw, Integer.valueOf(berechtigung));

				boolean anlegen_erfolgreich = manager.getDatabase().addUser(user);

				this.out.println(anlegen_erfolgreich);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case DELETEMSG:
			try {

				Message msg = Message.stringToMessage(Message.getMessage(in));
				boolean loeschen_erfolgreich = this.manager.getDatabase().deleteMessage(msg);
				this.out.println(loeschen_erfolgreich);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		case SERVER:

			this.out.println(this.manager.getAbteilungsName());
			this.out.println(this.manager.getAbteilungsID());

			break;

		case PUSH:
			try {

				Message msg = Message.stringToMessage(Message.getMessage(in));

				this.manager.getDatabase().editMessage(msg);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case EDITMESSAGE:
			try {
				Message msg = Message.stringToMessage(Message.getMessage(in));

				this.manager.getDatabase().editMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case SERVERPUSH:
			try {
				String abtName = this.in.readLine();
				this.manager.getDatabase().deleteAllMessageFromDeapartment(abtName);

				int marker = 1000000;
				String input;

				in.mark(marker);
				input = in.readLine();

				while (!input.equals(dataOrga.ControllCalls.END.toString())) {
					in.reset();
					this.manager.getDatabase().addMessage(Message.stringToMessage(Message.getMessage(in)));
					in.mark(marker);
					input = in.readLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		default:
			break;

		}
	}

}
