/**
 *  
 * 
 */

package client;

import java.awt.EventQueue;
import java.net.UnknownHostException;

import config.ClientManager;
import gui.GuiStarterLogin;
import update.ServerConector;

/*
 * TODO Client lagert alle Verbindungen auf externen Thread aus DONE
 * 
 *
 */

public class ClientMain extends Thread {

	private int ServerPort = 4690;
	private String ServerAdress = "localhost";

	public ClientMain() {
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

			manager.changeMainServer(test);

			System.out.println(manager.getServerConector().toString());

			EventQueue.invokeLater(new GuiStarterLogin(manager));

			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			manager.getUpdaterThread().exitUpdater(); // TODO Nicht schliesen
														// wenn MainFrame noch
														// offen ist. momentan
														// unsauber umgesezt.
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
