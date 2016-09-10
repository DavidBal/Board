package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import dataOrga.Message;
import dataOrga.User;
import gui.MainFrame;
import update.ServerConector;
import update.UpdaterThread;

/**
 * 
 * @author davidbaldauf Speichert Wichtige Daten für den Client.
 */
public class ClientManager {

	/**
	 * Datei Pfad
	 */
	private final static String filePath = "ServerList.txt";

	/**
	 * TODO - Entfernen Alle bekannten Server
	 */
	public ArrayList<ServerConector> knownServer = new ArrayList<ServerConector>();

	/**
	 * Server der momentan angesprochen wird
	 */
	private ServerConector server;

	public ServerConector getServerConector() {
		return this.server;
	}

	/**
	 * User Daten des angemeldet Nutzers
	 */
	private User user;

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private UpdaterThread update;

	public UpdaterThread getUpdaterThread() {
		return this.update;
	}

	private MainFrame mainFrame;

	public MainFrame getMainFrame() {
		return this.mainFrame;
	}

	public void setMainFrame(MainFrame mf) {
		this.mainFrame = mf;
	}

	private ArrayList<Message> Messages;

	public ArrayList<Message> getMessages() {
		return this.Messages;
	}

	public void addMessage(Message msg) {
		this.Messages.add(msg);
		if (this.mainFrame != null) {
			mainFrame.addMessage(msg);
		}
	}

	public void deleteAllMessage() {
		this.Messages.clear();
		if (this.mainFrame != null) {
			mainFrame.removeAllMessagePanel();
		}
	}

	/**
	 * Legt einen neue Manager fuer den Client an
	 */
	public ClientManager() {
		this.readInServer();

		this.Messages = new ArrayList<Message>();
		this.mainFrame = null;
	}

	/**
	 * Liest die Datei ein ServerList.txt ein und Added Server die noch nicht da
	 * sind falls datei nicht exestiert wird createServerListFile() aufgerufen
	 */
	private void readInServer() {
		try {
			BufferedReader file = new BufferedReader(new FileReader(new File(filePath)));

			String serverIP = "";
			int serverPort = 0;
			String name = "";

			String[] data = new String[3];

			data[0] = file.readLine();
			while (data[0] != null) {
				if (!data[0].startsWith("//")) {

					data = data[0].split(":");

					name = data[0];
					serverIP = data[1];
					serverPort = Integer.valueOf(data[2]);

					ServerConector newServer = new ServerConector(serverIP, serverPort, name);

					// Ueberpruefung ob der Server schon vorhanden ist um
					// Dopplung
					// zu verhindern.
					if (!knownServer.contains(newServer)) {
						knownServer.add(newServer);
						System.out.println(knownServer.get(knownServer.size() - 1).toString());
					}

				}
				data[0] = file.readLine();
			}

			file.close();
		} catch (FileNotFoundException e) {
			// Neue File wird angelegt
			this.createServerListFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Legt eine neue ServerList.txt an
	 */
	private void createServerListFile() {
		File file = new File(ClientManager.filePath);

		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Addet einen Neuen Server zur ServerList.txt und Addet ihn zu den
	 * knownServer Falls der Server schon exestiert wird er nicht geaddet
	 * 
	 * @param newServer
	 */
	public void addAServer(ServerConector newServer) {

		// Ueberpruefen ob der Server schon vorhanden ist um Dopplung zu
		// vermeiden.
		if (this.knownServer.contains(newServer))
			return;

		this.knownServer.add(newServer);

		try {
			FileWriter writer = new FileWriter(new File(ClientManager.filePath), true);
			writer.write(newServer.toString() + "\n");

			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Veraendert der Server wo die Daten hingesandt werden.
	 * 
	 * @param server
	 */
	public void changeMainServer(ServerConector server) {
		this.server = server;
	}

	/**
	 * Startet den UpdaterThread
	 */
	public void startUpdater() {
		this.update = new UpdaterThread(this);
		this.update.start();
	}
}
