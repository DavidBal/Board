package client;

import java.util.HashMap;
import java.util.PriorityQueue;
import config.ClientManager;
import dataOrga.ControllCalls;
import dataOrga.Message;
import dataOrga.Pair;

public class Buffer {

	HashMap<Message, ControllCalls> queue = new HashMap<Message, ControllCalls>();

	PriorityQueue<Message> Messages = new PriorityQueue<Message>();
	ClientManager manager;

	public Buffer(ClientManager manager) {
		this.manager = manager;
	}

	public void addNewMessage(Message msg) {
		synchronized (Messages) {
			queue.put(msg, ControllCalls.NEWMESSAGE);
		}
		manager.forceUpdate();
	}

	public void addEditMessage(Message msg) {
		synchronized (Messages) {
			queue.put(msg, ControllCalls.EDITMESSAGE);
		}
		manager.forceUpdate();
	}

	public void addDeleteMessage(Message msg) {
		synchronized (Messages) {
			queue.put(msg, ControllCalls.DELETEMSG);
		}
		manager.forceUpdate();
	}

	public Pair<Message, ControllCalls> getMessage() {
		Pair<Message, ControllCalls> tmp;
		synchronized (Messages) {
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
	 * Entfernt die Erste Nachticht aus dem Buffer
	 */
	public void removeMessageFromBuffer(Message msg) {
		synchronized (Messages) {
			this.queue.remove(msg);
		}
	}

}
