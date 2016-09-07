package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import client.Buffer;
import client.ServerConector;
import dataOrga.Message;
import dataOrga.User;
import update.UpdaterThread;
/**
 * 
 * @author davidbaldauf
 *	Speichert Wichtige Daten für den Client. 
 */
public class ClientManager {

	/**
	 * Datei Pfad
	 */
	private final static String filePath = "ServerList.txt";

	/**
	 * Alle bekannten Server
	 */
	public ArrayList<ServerConector> knownServer = new ArrayList<ServerConector>();

	/**
	 * Server der momentan angesprochen wird
	 */
	public ServerConector server;
	
	public User user;

	Thread update;

	public ArrayList<Message> Messages;
	public Buffer buffer;

	/**
	 * Legt einen neue Manager fuer den Client an
	 */
	public ClientManager() {
		this.readInServer();
		this.buffer = new Buffer(this);
		this.Messages = new ArrayList<Message>();
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

					// Ueberpruefung ob der Server schon vorhanden ist um Dopplung
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

	/**
	 * Singalisiert dem UpdaterThread jetzt ein Update durch zuführen
	 */
	public void forceUpdate() {
		synchronized (this.update) {
			this.update.notify();
		}
	}

	/**
	 * Der UpdaterThread soll beendet werden.
	 */
	public void exitUpdater() {
		synchronized (this.update) {
			((UpdaterThread)this.update).exit = true;
			this.update.notify();
		}
	}
}
