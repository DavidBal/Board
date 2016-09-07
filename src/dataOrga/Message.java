package dataOrga;

public class Message {

	public static String split = " </-/> ";

	int id;
	public int getId() {
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public String getUsername() {
		return username;
	}

	public String getAbteilung() {
		return abteilung;
	}

	public int getLastchange() {
		return lastchange;
	}

	String text;
	String username;
	String abteilung;
	int lastchange;

	public Message(int id, String text, String username, String abteilung, int lastchange) {
		this.id = id;
		this.text = text;
		this.username = username;
		this.abteilung = abteilung;
		this.lastchange = lastchange;
	}

	public String toString() {
		String tmp = this.id + split + this.text + split + this.username + split + this.abteilung + split
				+ this.lastchange;
		return tmp;
	}
	
	public static Message stringToMessage(String msg){
		
		String[]tmp = msg.split(split);
		
		return new Message(Integer.valueOf(tmp[0]),tmp[1],tmp[2],tmp[3],Integer.valueOf(tmp[4]));
	}

}
