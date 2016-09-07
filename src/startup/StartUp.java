package startup;

import client.ClientMain;
import config.ServerManager;
import server.Server;

public class StartUp {

	public static Thread thread;
	public static boolean threadactiv = false;

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println(" -c  : Starting the Client");
			System.out.println(" -s  : Starting the Server");
		} else if (args.length > 0) {
			if (args[0].equals("-s") || args[0].equals("-S")) {
				StartUp.server(args);
			}
			if (args[0].equals("-c") || args[0].equals("-C")) {
				StartUp.client(args);
			}
		} else {
			System.out.println(" -c  : Starting the Client");
			System.out.println(" -s  : Starting the Server");
		}

		// Nur beendet wenn Thread geschlossen
		if (StartUp.threadactiv == true) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
	 */
	public static void server(String[] arg) {

		System.out.println("Starting Server:");

		ServerManager serverManager = new ServerManager();

		if (arg.length == 2) {
			if (arg[1].equals("?")) {
				System.out.println("Change port by: [Port]");
				return;
			}
			serverManager.setServerPort(Integer.valueOf(arg[1]));
			System.out.println("ServerPort Changed: " + arg[1]);
		}

		Server server = new Server(serverManager);

		StartUp.startThread(server);
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
