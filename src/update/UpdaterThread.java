package update;

import java.io.IOException;

import javax.swing.JOptionPane;

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
	private boolean exit;

	private SendeBuffer buffer;

	public SendeBuffer getBuffer() {
		return buffer;
	}

	ClientManager manager;

	/**
	 * 
	 */
	@Override
	public void run() {
		update();
	}

	/**
	 * 
	 * @param manager
	 */
	public UpdaterThread(ClientManager manager) {
		this.manager = manager;
		this.setName("UpdaterThread");
		this.exit = false;
		this.buffer = new SendeBuffer(this);

	}

	/**
	 * @throws IOException
	 * 
	 */
	private synchronized void update() {
		while (this.exit == false) {
			try {
				this.sendBuffer();

				this.manager.getServerConector().update(this.manager);
			} catch (IOException e1) {
				System.out.println("Übertragung nicht möglich");
				if (manager.getMainFrame() != null) {
					JOptionPane.showMessageDialog(manager.getMainFrame().getContentPane(),
							"Update nicht möglich: Server nicht ereichbar", "Update Failed", JOptionPane.ERROR_MESSAGE);
				}
			}
			try {
				this.wait(UPDATE_INTERVALL * this.updateIntervallMod);
			} catch (InterruptedException e) {
			}
		}
	}

	private void sendBuffer() throws IOException {
		while (buffer.getMessage() != null) {
			Pair<Message, ControllCalls> tmp = buffer.getMessage();
			switch (tmp.getValue()) {
			case NEWMESSAGE:
				this.manager.getServerConector().sendNewMessage(tmp.getKey());
				break;
			case EDITMESSAGE:
				// TODO 
				this.manager.getServerConector().deleteMessage(tmp.getKey());
				this.manager.getServerConector().sendNewMessage(tmp.getKey());
				break;
			case DELETEMSG:
				this.manager.getServerConector().deleteMessage(tmp.getKey());
				break;
			default:
				break;
			}
			buffer.removeMessageFromBuffer(tmp.getKey());
		}
	}

	/**
	 * Updater soll jetzt ein Update machen
	 */
	public void forceUpdate() {
		synchronized (this) {
			this.notify();
		}
	}

	/**
	 * Updater soll beendet werden.
	 */
	public void exitUpdater() {
		synchronized (this) {
			this.exit = true;
			this.notify();
		}
	}
}
