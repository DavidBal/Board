package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import client.ClientMain;
import config.ClientManager;
import dataOrga.Message;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3562098913564430249L;

	UserPanel userInfo;

	ClientManager manager;
	ClientMain clientMain;
	JScrollPane messageZone;

	public MainFrame(ClientManager manager, ClientMain clientMain) {
		this.manager = manager;
		this.messageZone = new JScrollPane(new JPanel());
		this.clientMain = clientMain;
		if(clientMain == null){
			System.out.println("Fehler clientMain");
		}

		this.create();
	}

	private void create() {

		this.setLayout(new BorderLayout());

		((JPanel) this.messageZone.getViewport().getView())
				.setLayout(new BoxLayout((JPanel) this.messageZone.getViewport().getView(), BoxLayout.Y_AXIS));

		JPanel northZone = new JPanel();
		northZone.setLayout(new FlowLayout());

		this.userInfo = new UserPanel(this.manager.getUser());
		northZone.add(userInfo);

		JButton createNewMessage = new JButton("New Message"); // TODO viellecht
																// Icon
		createNewMessage.setToolTipText("Create New Message");

		JButton forceUpdate = new JButton("Update");
		forceUpdate.addMouseListener(new ForceUpdate(manager));

		northZone.add(createNewMessage);
		northZone.add(forceUpdate);

		this.add(northZone, BorderLayout.NORTH);
		this.add(messageZone, BorderLayout.CENTER);
		// TODO ActionListener

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.repaint();
		this.pack();

	}

	public void addMessage(Message msg) {

		System.out.println("Message Frame : " + msg.getText());

		MessagePanel msgPanel = new MessagePanel(msg, this.manager);

		((JPanel) this.messageZone.getViewport().getView()).add(msgPanel);
		this.messageZone.getViewport().getView().revalidate();

		this.messageZone.repaint();

	}

	public void removeAllMessagePanel() {

		((JPanel) this.messageZone.getViewport().getView()).removeAll();

	}

	private class ForceUpdate extends MouseAdapter {

		ClientManager manager;

		public ForceUpdate(ClientManager manager) {
			this.manager = manager;
		}

		public void mouseClicked(MouseEvent e) {
			manager.getUpdaterThread().forceUpdate();
		}

	}
}
