package startup;

import client.ClientMain;
import config.ServerManager;
import server.Server;

public class StartUp {

	public static Thread thread;
	public static boolean threadactiv = false;

	public static void main(String[] args) {

		try {
			if (args.length == 0) {
				System.out.println(" -c  : Starting the Client");
				System.out.println(" -s [Abteilungs-Name] [Abteilungs-ID 10-9999] [Port] : Starting the Server");
			} else if (args.length > 0) {
				if (args[0].equals("-s") || args[0].equals("-S")) {
					StartUp.server(args);
				}
				if (args[0].equals("-c") || args[0].equals("-C")) {
					StartUp.client(args);
				}
			} else {
				System.out.println(" -c  : Starting the Client");
				System.out.println(" -s [Abteilungs-Name] [Abteilungs-ID 10-9999] [Port] : Starting the Server");
			}

			// Nur beendet wenn Thread geschlossen
			if (StartUp.threadactiv == true) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("StartUp - Closed - Process End");
	}

	/**
	 * Startet den Thread Client
	 * 
	 * @param arg
	 */
	public static void client(String[] arg) {

		ClientMain client = new ClientMain();

		StartUp.startThread(client);
	}

	/**
	 * Startet den Thread Server und bereitet diesen auch vor
	 * 
	 * @param arg
	 * @throws Exception
	 */
	public static void server(String[] arg) throws Exception {

		System.out.println("Starting Server:");

		String abtName = "";
		int abtID = 0;
		int port = 0;

		if (arg.length >= 3) {
			// Abteilungs Name NEED
			if (arg[1].equals("?")) {
				System.out.println("Ändere Abteilungs-Name");
				return;
			}
			abtName = arg[1];

			// Abteilungs Name Need min 2 stellig
			if (arg[2].equals("?")) {
				System.out.println("Abteilungs Nummer (Muss einmalig sein!!) Zwischen 10 - 9999");
			}
			abtID = Integer.valueOf(arg[2]);

			// Port
			if (arg.length == 4) {
				if (arg[3].equals("?")) {
					System.out.println("Change port by: [Port]");
					return;
				}
				port = Integer.valueOf(arg[3]);
				System.out.println("ServerPort Changed: " + arg[3]);
			}

			Server server = new Server(new ServerManager(abtName, abtID, port));

			StartUp.startThread(server);
		} else {
			System.out.println(" -s [Abteilungs-Name] [Abteilungs-ID 10-9999] [Port] : Starting the Server");
		}
	}

	/**
	 * Startet einen Thread.
	 * 
	 * @param thread
	 */
	public static void startThread(Thread thread) {
		StartUp.threadactiv = true;
		StartUp.thread = thread;
		StartUp.thread.start();
	}
}
