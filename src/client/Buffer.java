package client;

import java.util.HashMap;

import config.ClientManager;
import dataOrga.ControllCalls;
import dataOrga.Message;
import dataOrga.Pair;

/**
 * 
 *Klasse um Nachtichten zwischen zu speichern.
 * 
 */
public class Buffer {
	
	
	HashMap<Message, ControllCalls> queue;

	ClientManager manager;

	public Buffer(ClientManager manager) {
		this.manager = manager;
		this.queue = new HashMap<Message, ControllCalls>();
	}

	
	public void addNewMessage(Message msg) {
		synchronized (queue) {
			queue.put(msg, ControllCalls.NEWMESSAGE);
		}
		manager.forceUpdate();
	}

	public void addEditMessage(Message msg) {
		synchronized (queue) {
			queue.put(msg, ControllCalls.EDITMESSAGE);
		}
		manager.forceUpdate();
	}

	public void addDeleteMessage(Message msg) {
		synchronized (queue) {
			queue.put(msg, ControllCalls.DELETEMSG);
		}
		manager.forceUpdate();
	}

	/**
	 * Gibt eine Nachricht aus dem Buffer zurück
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
	 * @param msg
	 */
	public void removeMessageFromBuffer(Message msg) {
		synchronized (queue) {
			this.queue.remove(msg);
		}
	}

}
