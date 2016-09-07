package config;

import java.util.ArrayList;

import sqlbase.Database;

public class ServerManager {
	
	private int serverPort = 4690;

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public static final boolean debug = false;
	public ArrayList<String> messages;

	private int acticConnects;
	private int finishConnects;
	public Database database;

	public ServerManager() {
		this.messages = new ArrayList<String>();
		this.finishConnects = 0;
		this.acticConnects = 0;
		
		this.database = new Database();
	}

	public synchronized void onConnect() {
		this.acticConnects++;
	}

	public synchronized void onFinish() {
		this.acticConnects--;
		this.finishConnects++;
	}

	public int getActivConnects() {
		return this.acticConnects;
	}

	public int getFinishConnects() {
		return this.finishConnects;
	}
}
