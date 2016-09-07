package server;

import java.util.EnumSet;
import java.util.Scanner;

import config.ServerManager;
import dataOrga.User;

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
		help("Shows All Commnads"), show("Show a List of Connections"), fail("fail passing"), add("add a user");

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
		usable = EnumSet.of(Commands.help, Commands.show,Commands.add);
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
			manager.database.addUser(user);
			break;
		default:
			println("Use Command: " + Commands.help + " - " + Commands.help.info);
			break;
		}
	}
}
