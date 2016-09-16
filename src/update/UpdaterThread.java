package update;

import java.io.IOException;

import javax.swing.JOptionPane;

import dataOrga.ControllCalls;
import dataOrga.Message;
import dataOrga.Pair;
import gui.MainFrame;
import messageSaving.MessageSaver;

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

	private ServerConector serverConector;
	private MainFrame mainFrame;
	private MessageSaver messageSaver;

	/**
	 * 
	 */
	@Override
	public void run() {
		update();
	}

	/**
	 * Client
	 * 
	 * @param serverConector
	 * @param messageSaver
	 * @param mainFrame
	 */
	public UpdaterThread(ServerConector serverConector, MessageSaver messageSaver, MainFrame mainFrame) {
		this.setName("UpdaterThread");
		this.exit = false;
		this.buffer = new SendeBuffer(this);
		this.serverConector = serverConector;
		this.messageSaver = messageSaver;

		// Client Spezifisch
		this.mainFrame = mainFrame;
	}

	/**
	 * Server
	 * 
	 * @param serverConector
	 * @param messageSaver
	 */
	public UpdaterThread(ServerConector serverConector, MessageSaver messageSaver) {
		this.setName("UpdaterThread");
		this.exit = false;
		this.buffer = new SendeBuffer(this);
		this.serverConector = serverConector;
		this.messageSaver = messageSaver;

		// Client Spezifisch
		this.mainFrame = null;
	}

	/**
	 * @throws IOException
	 * 
	 */
	private synchronized void update() {
		while (this.exit == false) {
			try {
				this.sendBuffer();

				this.serverConector.update(this.messageSaver);

				this.updateUI();
			} catch (IOException e1) {
				System.out.println("Übertragung nicht möglich");
				if (mainFrame != null) {
					JOptionPane.showMessageDialog(this.mainFrame.getContentPane(),
							"Update nicht möglich: Server nicht ereichbar", "Update Failed", JOptionPane.ERROR_MESSAGE);
				}
			}
			try {
				this.wait(UPDATE_INTERVALL * this.updateIntervallMod);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Sendet alle im Buffer gespeicherten Nachrichten
	 * 
	 * @throws IOException
	 */
	private synchronized void sendBuffer() throws IOException {
		while (buffer.getMessage() != null) {
			Pair<Message, ControllCalls> tmp = buffer.getMessage();
			switch (tmp.getValue()) {
			case NEWMESSAGE:
				this.serverConector.sendNewMessage(tmp.getKey());
				break;
			case EDITMESSAGE:
				// TODO
				this.serverConector.deleteMessage(tmp.getKey());
				this.serverConector.sendNewMessage(tmp.getKey());
				break;
			case DELETEMSG:
				this.serverConector.deleteMessage(tmp.getKey());
				break;
			default:
				break;
			}
			buffer.removeMessageFromBuffer(tmp.getKey());
		}
	}

	private synchronized void updateUI() {
		if (this.mainFrame != null) {
			mainFrame.removeAllMessagePanel();
			for (Message msg : messageSaver.getAllMessages()) {
				this.mainFrame.addMessageFrame(msg);
			}
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
