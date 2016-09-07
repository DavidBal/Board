/**
 *  
 * 
 */

package client;

import java.io.IOException;
import java.net.UnknownHostException;

import clientTUISimpel.Konsole;
import config.ClientManager;
import dataOrga.Message;
import gui.Login;

//TODO Client lagert alle Verbindungen auf externen Thread aus

public class ClientMain extends Thread {

	private int ServerPort = 4690;
	private String ServerAdress = "localhost";

	public ClientMain(){
		this.setName("ClientMain");
	}
	
	/**
	 *
	 * @param args
	 */
	public void run() {

		ClientManager manager = new ClientManager();

		ServerConector test;
		try {
			test = new ServerConector(this.ServerAdress, this.ServerPort);

			manager.server = test;
			// TODO Thread der das Auto-Uppdate der Daten �bernimmt!

			System.out.println(manager.server.toString());

			// ------ Testfaelle - Start------

			try {
				manager.server.ping();
				manager.server.sendNewMessage(new Message(-1, "Hallo Welt!!", "Unknown", "Unknown", 1));
			} catch (IOException e) {
				
				
				e.printStackTrace();
			}

			// manager.server.auth("test", "test"); // Alles richtig
			// manager.server.auth("Nein", "test"); // User falsch
			// manager.server.auth("test", "Nein"); // PW falsch
			// manager.server.auth("nein", "nein"); // Alles falsch
			// ------ Testfaelle - Ende ------

			manager.startUpdater();
			// Ruft das Test Menue auf !!Konsole!!
			Konsole c = new Konsole(manager);
			Login l = new Login(manager);
			l.setVisible(true);
			c.hauptMenue();

			manager.exitUpdater();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
