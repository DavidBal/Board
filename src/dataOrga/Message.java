package dataOrga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class Message {

	public static String split = " </-/> ";
	private static String enter = " <///>";

	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public long getLastchange() {
		return lastchange;
	}

	String text;
	String username;
	String abteilung;
	long lastchange;

	public Message(int id, String text, String username, String abteilung, long lastchange) {
		this.id = id;
		this.text = text;
		this.username = username;
		this.abteilung = abteilung;
		if (lastchange == 0) {
			this.lastchange = this.createTimeStamp();
		} else {
			this.lastchange = lastchange;
		}
	}

	public String toString() {

		String tmp = this.id + split + this.text + split + this.username + split + this.abteilung + split
				+ this.lastchange;
		tmp.replaceAll("\n", enter);
		// System.err.println(tmp);
		return tmp;
	}

	public static Message stringToMessage(String msg) {

		// System.err.println(msg);
		msg.replaceAll(enter, "\n");

		String[] tmp = msg.split(split);

		return new Message(Integer.valueOf(tmp[0]), tmp[1], tmp[2], tmp[3], Long.valueOf(tmp[4]));
	}

	public void changeText(String text) {
		this.text = text;

	}

	public void sendMessage(PrintWriter out) {
		String msgString = this.toString();

		out.println(msgString.length());

		System.err.println(msgString.length());

		for (char c : msgString.toCharArray()) {
			out.print(c);
			System.err.print(c);
		}
		out.println();
	}

	public static String getMessage(BufferedReader in) throws IOException {
		String msg = "";
		int msgLenght = Integer.valueOf(in.readLine());

		for (int i = 0; i < msgLenght; i++) {
			char c = (char) in.read();
			System.err.println(c);
			msg += c;
		}
		in.readLine();

		return msg;
	}

	public void setAbteilung(String abteilungsName) {
		this.abteilung = abteilungsName;
	}

	private long createTimeStamp() {
		long timeInt = 0;

		Calendar cal = Calendar.getInstance();
		timeInt = cal.get(Calendar.YEAR);
		timeInt = timeInt * 100 + cal.get(Calendar.MONTH);
		timeInt = timeInt * 100 + cal.get(Calendar.DAY_OF_MONTH);
		timeInt = timeInt * 100 + cal.get(Calendar.HOUR_OF_DAY);
		timeInt = timeInt * 100 + cal.get(Calendar.MINUTE);
		timeInt = timeInt * 100 + cal.get(Calendar.SECOND);

		System.out.println(" .- " + timeInt);

		return timeInt;
	}

}
