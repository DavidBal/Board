package gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dataOrga.Message;
import dataOrga.User;
import update.Buffer;

public class MessageNewAndEditFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4316350965827664233L;

	Buffer buffer;
	Message message;
	JTextArea msgText;
	User user;

	public MessageNewAndEditFrame(Buffer buffer, User user, Message message) {
		this.buffer = buffer;
		this.message = message;
		this.user = user;
		this.create();
	}

	public MessageNewAndEditFrame(Buffer buffer, User user) {
		this.buffer = buffer;
		this.user = user;
		this.message = null;

		this.create();
	}

	private void create() {
		if (this.message == null) {
			this.setTitle("New Message");
		} else {
			this.setTitle("Edit Message");
		}

		this.setLayout(new BorderLayout());

		this.msgText = new JTextArea(20, 60);
		JScrollPane msgTextScrollPane = new JScrollPane(msgText);
		this.add(msgTextScrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton send = new JButton("Senden");
		send.addMouseListener(new SendMessageEvent(this, buffer, msgText, message, user));
		buttonPanel.add(send);

		this.add(buttonPanel, BorderLayout.SOUTH);

		this.pack();
	}

	private class SendMessageEvent extends MouseAdapter {

		Buffer buffer;
		JTextArea msgText;
		Message msg;
		User user;
		JFrame mother;

		public SendMessageEvent(JFrame mother, Buffer buffer, JTextArea msgText, Message msg, User user) {
			this.mother = mother;
			this.buffer = buffer;
			this.msgText = msgText;
			this.msg = msg;
			this.user = user;
		}

		public void mouseClicked(MouseEvent e) {

			System.out.println("Begin:");
			System.out.println(msgText.getText());
			System.out.println("END");

			if (msg == null) {
				buffer.addNewMessage(new Message(-1, msgText.getText(), user.getName(), "Unknown", 1));
			} else {
				msg.changeText(msgText.getText());
				buffer.addEditMessage(msg);
			}

			mother.setVisible(false);

		}

	}

}
