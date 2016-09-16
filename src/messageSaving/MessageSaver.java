package messageSaving;

import java.util.ArrayList;

import dataOrga.Message;

public interface MessageSaver {

	public void addMessage(Message msg);

	public boolean deleteMessage(Message msg);

	public void deleteAllMessage(String info);
	
	public ArrayList<Message> getAllMessages();

}
