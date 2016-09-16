package messageSaving;

import java.util.ArrayList;

import dataOrga.Message;

public class MessageList implements MessageSaver {

	private ArrayList<Message> messageList;

	public MessageList() {
		this.messageList = new ArrayList<Message>();
	}

	/**
	 * Fügt eine Nachricht hinzu.
	 * 
	 * @param msg
	 */
	public synchronized void addMessage(Message msg) {
		synchronized (messageList) {
			Message oldMsg = this.findMessage(msg);
			if (oldMsg == null) {
				messageList.add(msg);
			} else {
				oldMsg.changeText(msg.getText());
			}
		}
	}

	/**
	 * Löscht alle Nachrichten
	 */
	public synchronized void deleteAllMessage(String info) {
		synchronized (messageList) {
			this.messageList.clear();
		}
	}

	/**
	 * Get the raw list.
	 * 
	 * @return
	 */
	public synchronized ArrayList<Message> getAllMessages() {
		synchronized (messageList) {
			return messageList;
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
		synchronized (messageList) {
			for (Message msg : this.messageList) {
				if (msg.getId() == id) {
					return msg;
				}
			}
			return null;
		}
	}

	public synchronized Message findMessage(Message msg) {
		synchronized (messageList) {
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
		synchronized (messageList) {
			return this.messageList.remove(msg);
		}
	}

}
