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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		manager.onFinish();

		if (ServerManager.debug)
			System.out.println("Worker - " + this.id + " END!!");
	}

	WorkerThread(Socket client, int id, ServerManager manager) {
		this.client = client;
		this.id = id;
		this.manager = manager;

		this.setName("WorkerThread" + this.id);

		try {
			this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			this.out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void controll(String controllCall) {

		/**
		 * 
		 */
		/**
		 * TODO ------ * CC -> One Line Every Msg -> one Line END -> One Line
		 */

		switch (ControllCalls.stringToControllCall(controllCall)) {
		case Ping:
			this.out.println("Pong");
			break;
		case NEWMESSAGE:
			try {
				String msg = this.in.readLine();
				if (ServerManager.debug)
					System.out.println(msg);
				this.manager.database.addMessage(Message.stringToMessage(msg));
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
				this.out.println(this.manager.database.getUserBerechtigung(new User(userName, passwort, 0, 0)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case UPDATE:
			ArrayList<Message> msgs = this.manager.database.loadMessages(0);
			for (Message msg : msgs) {
				this.out.println(msg.toString());
			}

			this.out.println(ControllCalls.END);

			break;
		case ADDUSER:
			try {
				String name = in.readLine();
				String pw = in.readLine();
				String berechtigung = in.readLine();
				User user = new User(name, pw, Integer.valueOf(berechtigung));

				boolean anlegen_erfolgreich = manager.database.addUser(user);

				this.out.println(anlegen_erfolgreich);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case DELETEMSG:
			try {
				
				Message msg = Message.stringToMessage(in.readLine());
				boolean loeschen_erfolgreich = this.manager.database.deleteMessage(msg);
				this.out.println(loeschen_erfolgreich);
				

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		default:
			break;

		}
	}

}
