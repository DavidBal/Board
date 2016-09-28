package config;

import messageSaving.Database;
import update.ServerConector;
import update.UpdaterThread;

public class ServerManager implements Manager {

	private int serverPort = 4690;

	private String abteilungsName;

	private int abteilungsID;

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public static final boolean debug = true;

	private int acticConnects;
	private int finishConnects;
	private Database database;

	public Database getDatabase() {
		return database;
	}

	private ServerConector serverConector;

	public ServerConector getServerConector() {
		return serverConector;
	}

	public void setServerConector(ServerConector serverConector) {
		if (this.serverConector != null) {
			this.updater.exitUpdater();
		}
		this.serverConector = serverConector;
		this.startUpdater();
	}

	private UpdaterThread updater;

	public UpdaterThread getUpdater() {
		return updater;
	}

	public ServerManager(String abteilungsName, int abteilungsID, int serverPort) throws Exception {
		if (abteilungsID < 10 || 9999 < abteilungsID) {
			throw new Exception("Abteilungs id muss min. 2-Stellig und max. 4-Stellig!!");
		}

		this.finishConnects = 0;
		this.acticConnects = 0;

		this.abteilungsName = abteilungsName;
		this.abteilungsID = abteilungsID;
		if (serverPort != 0) {
			this.serverPort = serverPort;
		}

		this.database = new Database(this.abteilungsName, this.abteilungsID);

		this.serverConector = null;
	}

	public ServerManager(String abteilungsName, int abteilungsID, int serverPort, ServerConector serverConector)
			throws Exception {
		this(abteilungsName, abteilungsID, serverPort);
		this.serverConector = serverConector;
		this.startUpdater();
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

	public String getAbteilungsName() {
		return abteilungsName;
	}

	public void setAbteilungsName(String abteilungsName) {
		this.abteilungsName = abteilungsName;
	}

	public int getAbteilungsID() {
		return abteilungsID;
	}

	public void setAbteilungsID(int abteilungsID) {
		this.abteilungsID = abteilungsID;
	}

	public void startUpdater() {
		if (serverConector != null) {
			updater = new UpdaterThread(serverConector, this.database);
			updater.run();
		}
	}
}
