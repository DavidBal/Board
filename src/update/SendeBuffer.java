package update;

import java.util.HashMap;

import dataOrga.ControllCalls;
import dataOrga.Message;
import dataOrga.Pair;

/**
 * 
 * Klasse um Nachtichten zwischen zu speichern.
 * 
 */
public class SendeBuffer {

	HashMap<Message, ControllCalls> queue;

	UpdaterThread updater;

	public SendeBuffer(UpdaterThread updater) {
		this.updater = updater;
		this.queue = new HashMap<Message, ControllCalls>();
	}

	public void addNewMessage(Message msg) {
		synchronized (queue) {
			queue.put(msg, ControllCalls.NEWMESSAGE);
		}
		updater.forceUpdate();
	}

	public void addEditMessage(Message msg) {
		synchronized (queue) {
			queue.put(msg, ControllCalls.EDITMESSAGE);
		}
		updater.forceUpdate();
	}

	public void addDeleteMessage(Message msg) {
		synchronized (queue) {
			queue.put(msg, ControllCalls.DELETEMSG);
		}
		updater.forceUpdate();
	}

	public void addPushMessage(Message msg) {
		synchronized (queue) {
			queue.put(msg, ControllCalls.PUSH);
		}
		updater.forceUpdate();
	}

	/**
	 * Gibt eine Nachricht aus dem Buffer zurück
	 * 
	 * @return Null oder Data
	 */
	public Pair<Message, ControllCalls> getMessage() {
		Pair<Message, ControllCalls> tmp;
		synchronized (queue) {
			if (this.queue.isEmpty() == false) {
				Message msg = queue.keySet().iterator().next();
				tmp = new Pair<Message, ControllCalls>(msg, queue.get(msg));
			} else {
				tmp = null;
			}
		}

		return tmp;
	}

	/**
	 * Entfernt Übergebene Nachricht aus dem Buffer
	 * 
	 * @param msg
	 */
	public void removeMessageFromBuffer(Message msg) {
		synchronized (queue) {
			this.queue.remove(msg);
		}
	}

}
