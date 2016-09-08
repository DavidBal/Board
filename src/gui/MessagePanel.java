package gui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import config.ClientManager;
import dataOrga.Message;

public class MessagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ClientManager manager;
	Message message;

	public MessagePanel(Message message, ClientManager manager) {
		this.message = message;
		this.manager = manager;
		this.create();
	}

	private void create() {

		this.setLayout(new FlowLayout());

		JTextField msg = new JTextField(this.message.getText());
		msg.setEditable(false);

		JButton edit = new JButton("Edit");

		JButton delete = new JButton("Delete");

		JButton push = new JButton("Push");

		this.add(msg);
		this.add(edit);
		this.add(delete);
		this.add(push);
		
		this.repaint();

	}

}
