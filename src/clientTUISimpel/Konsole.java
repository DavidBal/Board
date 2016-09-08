package clientTUISimpel;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Scanner;

import client.ServerConector;
import config.ClientManager;
import dataOrga.Message;
import dataOrga.User;

/**
 * Implementiert auf Basis der Konsole einfache Testmoeglichkeiten des Clients!
 * 
 * @author David
 *
 */
public class Konsole {
	ClientManager manager;

	public Konsole(ClientManager manager) {
		this.manager = manager;
	}

	/**
	 * Hauptmenue
	 */
	public void hauptMenue() {
		boolean exit = false;
		Scanner leser = new Scanner(System.in);
		do {
			this.showMenue();
			try {
				exit = this.useMenue(leser);
			} catch (ConnectException e) {
				System.out.println("Server Verbindung konnte nicht hergestellt werden.");
			}
		} while (exit == false);
		leser.close();
	}

	/**
	 * Gibt die verfuegbaren Funktionen aus
	 */
	private void showMenue() {
		int i = 1;
		System.out.println("--------------------------------------------------");
		System.out.println("   -    Test   -    Hauptmenue   -    Test   -    ");
		System.out.println("--------------------------------------------------");
		// Sind nutzbar:
		System.out.println(i++ + " - Ping Server");
		System.out.println(i++ + " - Send Msg");
		System.out.println(i++ + " - Login ");
		System.out.println(i++ + " - Show All Server ");
		System.out.println(i++ + " - Add a Server");
		System.out.println(i++ + " - Change Main-Server ");
		System.out.println(i++ + " - Force Uppdate");// TODO Resetet den Auto
														// Update
		System.out.println(i++ + " - Show Msgs");
		System.out.println(i++ + " - Add User");	// TODO Abt. Leiter ; Admin
		System.out.println(i++ + " - Delete Msg"); // TODO nur eigene (Abt.Leiter
												// alle seiner Abt.)
		// Folgen:
		System.out.println("--------------------------------------------------");
		
		System.out.println("? - Edit Msg");// TODO nur eigene (Abt.Leiter alle
											// seiner Abt.)
		
		System.out.println("? - Delete User");// TODO Abt. Leiter ; Admin
		System.out.println("? - Change User Permission");// TODO Admin
		System.out.println("---------------------------------------------------");

		// --
		System.out.println(0 + " - Exit");
		System.out.println("---------------------------------------------------");

	}

	/**
	 * Liest die Auswahl des User ein und ruft die Funktionen auf
	 * 
	 * @param leser
	 * @return
	 * @throws ConnectException
	 */
	private boolean useMenue(Scanner leser) throws ConnectException {
		int auswahl = Integer.valueOf(leser.nextLine());
		boolean exit = false;

		switch (auswahl) {
		case 0:
			exit = true;
			System.out.println("-Exit-");
			break;
		case 1:
			try {
				this.manager.server.ping();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;
		case 2:
			System.out.println("Msg(Press-Enter to Send): ");
			String msg = "";
			msg = leser.nextLine();
			this.manager.buffer.addNewMessage(new Message(-1, msg, "Unknown", "Unknown", 1));// TODO
			break;
		case 3:
			System.out.println("Benutzer: ");
			String userName = leser.nextLine();
			System.out.println("Passwort: ");
			String pw = leser.nextLine();
			User user = new User(userName, pw, 0, 0);
			try {
				this.manager.server.identifyUser(user);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.println("User Berechtigung : " + user.getBerechtigung() );
			
			break;
		case 4:
			System.out.println("-----------Server List-----------");
			// int i = 0;
			for (ServerConector sc : manager.knownServer) {
				System.out.println(manager.knownServer.indexOf(sc) + " - " + sc.toString());
			}
			System.out.println("---------------END---------------");
			break;
		case 5:
			System.out.println("Name:");
			String name = leser.nextLine();
			System.out.println("ServerIP:");
			String serverIP = leser.nextLine();
			System.out.println("ServerPort:");
			String serverPort = leser.nextLine();

			try {
				manager.addAServer(new ServerConector(serverIP, Integer.valueOf(serverPort), name));
			} catch (NumberFormatException | UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 6:
			System.out.println("Change Main Server:");
			String position = leser.nextLine();

			int in = Integer.valueOf(position);

			if (in < manager.knownServer.size() && -1 < in) {
				manager.changeMainServer(manager.knownServer.get(in));
			} else {
				System.out.println("Out of Bound!!");
			}

			break;
		case 7:
			System.out.println("Update Forced: ");
			manager.forceUpdate();
			break;
		case 8:
			System.out.println("Messages: ");
			for (Message s : manager.getMessages()) {
				System.out.println(s.toString().replace(Message.split, "\n"));
			}
			break;
		case 9:
			System.out.println("Create a new User:");
			System.out.println("Username: ");
			String username = leser.nextLine();
			System.out.println("Passwort: ");
			String passwort = leser.nextLine();
			System.out.println("Berechtigung: ");
			int berechtigung = Integer.valueOf(leser.nextLine());
			User u = new User(username, passwort, berechtigung);
			try {
				if(manager.server.addUser(u)){
					System.out.println("User anlegen erfolgreich");
				}else{
					System.out.println("User anlegen NICHT erfolgreich");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 10:
			int delete = 0;
			System.out.println("Delete Msg Id? :");
			String id = leser.nextLine();
			for (Message message : manager.getMessages()){
				if(message.getId() == Integer.valueOf(id)){
					try {
						if(manager.server.deleteMessage(message) == true){
							delete++;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
			}
			System.out.println("Delete Msg  = " + delete);
			break;
		default:
			System.out.println("Pls try again!!");
			break;
		}
		return exit;
	}
}
