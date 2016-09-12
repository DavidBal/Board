package gui;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import config.ClientManager;
import dataOrga.Message;
import dataOrga.User.Berechtigung;
import update.Buffer;

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

		JTextArea msg = new JTextArea(this.message.getText(), 4, 40);
		msg.setLineWrap(true);
		msg.setEditable(false);

		JButton edit = new JButton("Edit");
		JButton delete = new JButton("Delete");
		delete.addMouseListener(new DeleteMessage(this.manager.getUpdaterThread().buffer, this.message));
		JButton push = new JButton("Push");

		// Disabel Buttons für unberächtigte
		if (!manager.getUser().getName().equals(message.getUsername())) {
			edit.setEnabled(false);
			delete.setEnabled(false);
			push.setEnabled(false);
		}

		if (Berechtigung.Abteilungsleiter.getInteger() <= manager.getUser().getBerechtigung().getInteger()) {
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

	private class DeleteMessage extends MouseAdapter {

		Buffer buffer;
		Message message;

		public DeleteMessage(Buffer buffer, Message message) {
			this.buffer = buffer;
			this.message = message;
		}

		public void mouseClicked(MouseEvent e) {
			// TODO nachfrage

			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null, "Wollen sie die Nachricht wircklich loeschen?",
					"Abfrage", dialogButton);

			if (dialogResult == JOptionPane.YES_OPTION) {
				this.buffer.addDeleteMessage(message);
			}
		}

	}

}
