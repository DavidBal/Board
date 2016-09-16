package config;

import java.util.ArrayList;

import dataOrga.Message;

public class MessageList {

	private ArrayList<Message> Messages;

	public MessageList() {
		this.Messages = new ArrayList<Message>();
	}

	/**
	 * Fügt eine Nachricht hinzu.
	 * 
	 * @param msg
	 */
	public synchronized void addMessage(Message msg) {
		synchronized (Messages) {
			this.Messages.add(msg);
		}
	}

	/**
	 * Löscht alle Nachrichten
	 */
	public synchronized void deleteAllMessage() {
		synchronized (Messages) {
			this.Messages.clear();
		}
	}

	/**
	 * Get the raw list.
	 * @return
	 */
	public synchronized ArrayList<Message> getMessages() {
		synchronized (Messages) {
			return Messages;
		}
	}

	/**
	 * Findet eine Nachricht mit der gegebnen ID.
	 * 
	 * @param id
	 * @return null if not on the list or the Message Object
	 * 
	 */
	public synchronized Message findID(int id) {
		synchronized (Messages) {
			for (Message msg : this.Messages) {
				if (msg.getId() == id) {
					return msg;
				}
			}
			return null;
		}
	}

	public synchronized Message findMessage(Message msg) {
		synchronized (Messages) {
			this.findID(msg.getId());
			return null;
		}
	}

	/**
	 * Loescht eine bestimmte Message
	 * 
	 * @param msg
	 * @return
	 */
	public synchronized boolean deleteMessage(Message msg) {
		synchronized (Messages) {
			return this.Messages.remove(msg);
		}
	}
	
	

}
