package server;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Scanner;

import config.ServerManager;
import dataOrga.User;
import update.ServerConector;

//TODO Close server save with all workerthreads done
//TODO Delete User, edit there "Berechtigung"
//TODO Delete Msg
public class CommandsExcuter extends Thread {

	Scanner scanner = new Scanner(System.in);

	/**
	 * 
	 */
	public void run() {
		System.out.println("Server Command Executer is UP:");
		this.waitForCommand();

	}

	private ServerManager manager;
	private Thread server; // TODO Exit Server

	private boolean exit = false;
	private Scanner reader;
	private EnumSet<Commands> usable;

	private enum Commands {
		help("Shows All Commnads"), show("Show a List of Connections"), fail("fail passing"), add(
				"add a user"), listuser("Shows a list of all Users"), deleteOverMsg(
						"Delete all Messages from over Departments"), setTopServer(
								"Set a Server to that this server send Messages"), forceUpdate(
										"The Server will be forced to do an Update!");

		String info;

		private Commands(String info) {
			this.info = info;
		}

		static Commands stringTOCo(String s) {
			for (Commands c : Commands.values()) {
				if (c.name().equals(s))
					return c;
			}
			return fail;
		}
	}

	public CommandsExcuter(ServerManager manager, Thread server) {
		this.manager = manager;
		this.server = server;
		this.reader = new Scanner(System.in);
		usable = EnumSet.of(Commands.help, Commands.show, Commands.add, Commands.listuser, Commands.deleteOverMsg,
				Commands.setTopServer);
		this.setName("ServerCommandExecuter");
	}

	private void println(String s) {
		System.out.println(s);
	}

	@SuppressWarnings("unused")
	private void print(String s) {
		System.out.print(s);
	}

	public void waitForCommand() {

		while (exit == false) {
			String eingabe = reader.nextLine();
			this.executer(Commands.stringTOCo(eingabe));
		}

		reader.close();
	}

	private void executer(Commands command) {

		switch (command) {
		case help:
			for (Commands c : usable) {
				println(c.name() + " - " + c.info);
			}
			break;
		case show:
			println("Actic Connects: " + manager.getActivConnects());
			println("Finished Connects: " + manager.getFinishConnects());
			break;
		case add:
			println("Create a new User:");
			println("Username: ");
			String name = scanner.nextLine();
			println("Passwort: ");
			String pw = scanner.nextLine();
			println("Berechtigung: ");
			int berechtigung = Integer.valueOf(scanner.nextLine());
			User user = new User(name, pw, berechtigung);
			manager.getDatabase().addUser(user);
			break;
		case listuser:
			println("UserList:");
			ArrayList<User> users = manager.getDatabase().loadUser();
			for (User u : users) {
				println(u.toString());
			}
			break;
		case deleteOverMsg:
			manager.getDatabase().deleteAllMessage();
			println("All Messages from other Deapartments have been deleted");
			break;
		case setTopServer:
			println("Set Top Level Server!");

			println("Server-Ip:");
			String serverIp = scanner.nextLine();
			println("Port:");
			String serverPort = scanner.nextLine();

			try {
				manager.setServerConector(new ServerConector(serverIp, Integer.valueOf(serverPort)));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case forceUpdate:
			if (this.manager.getUpdater() != null) {
				println("Update will be forced:");

				manager.getUpdater().forceUpdate();

			} else {
				println("No Updater is intialed.");
			}
		default:
			println("Use Command: " + Commands.help + " - " + Commands.help.info);
			break;
		}
	}
}
