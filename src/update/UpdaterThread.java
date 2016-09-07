package update;

import java.io.IOException;

import config.ClientManager;
import dataOrga.ControllCalls;
import dataOrga.Message;
import dataOrga.Pair;

public class UpdaterThread extends Thread {

	/**
	 * Update-Intervall 2,5 Minuten.
	 */
	private static final long UPDATE_INTERVALL = 150000;
	private long updateIntervallMod = 1;
	public boolean exit;

	ClientManager manager;

	/**
	 * 
	 */
	@Override
	public void run() {
		update();
		this.exit = false;
	}

	/**
	 * 
	 * @param manager
	 */
	public UpdaterThread(ClientManager manager) {
		this.manager = manager;
		this.setName("UpdaterThread");
		this.exit = false;

	}

	/**
	 * 
	 */
	private synchronized void update() {
		while (true) {
			try {
				this.sendBuffer();
				this.manager.server.update(this.manager);
			} catch (IOException e1) {
				System.err.println(" Übertragung nicht möglich !!!");
			}
			try {
				this.wait(UPDATE_INTERVALL * this.updateIntervallMod);
			} catch (InterruptedException e) {
			}
			if (this.exit) {
				return;
			}
		}
	}

	private void sendBuffer() throws IOException {
		while (manager.buffer.getMessage() != null) {
			Pair<Message, ControllCalls> tmp = manager.buffer.getMessage();
			switch(tmp.getValue()){
			case NEWMESSAGE:
				this.manager.server.sendNewMessage(tmp.getKey());
				break;
			case EDITMESSAGE:
				//TODO
				this.manager.server.deleteMessage(tmp.getKey());
				this.manager.server.sendNewMessage(tmp.getKey());
				break;
			case DELETEMSG:
				this.manager.server.deleteMessage(tmp.getKey());
				break;
			default:
				break;	
			}
			manager.buffer.removeMessageFromBuffer(tmp.getKey());
		}
	}
}
