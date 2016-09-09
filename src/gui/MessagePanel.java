package gui;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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

		JPanel button1 = new JPanel();
		button1.setLayout(new BoxLayout(button1, BoxLayout.Y_AXIS));
		
		
		
		JTextArea msg = new JTextArea(this.message.getText(),4,20);
		msg.setLineWrap(true);
		msg.setEditable(false);
		
		
		
		JButton edit = new JButton("Edit");
		JButton delete = new JButton("Delete");
		JButton push = new JButton("Push");
		
		//Disabel Buttons für  unberächtigte
		if(!manager.user.getName().equals(message.getUsername())){
			edit.setEnabled(false);
			delete.setEnabled(false);
			push.setEnabled(false);
		}
		
		if(1 < manager.user.getBerechtigung().getInteger() ){
			edit.setEnabled(true);
			delete.setEnabled(true);
			push.setEnabled(true);
		}
		
		edit.setSize(delete.getSize());
		push.setSize(delete.getSize());
		this.add(new JScrollPane(msg));
		button1.add(edit);
		button1.add(delete);
		this.add(button1);
		this.add(push);
		this.repaint();

	}
	
	

}
