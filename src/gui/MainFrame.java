package gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import config.ClientManager;
import dataOrga.Message;
import dataOrga.User;
import update.SendeBuffer;
import update.UpdaterThread;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3562098913564430249L;

	UserPanel userInfo;

	ClientManager manager;
	JPanel messageList;

	public MainFrame(ClientManager manager) {
		this.manager = manager;
		this.messageList = new JPanel();

		this.create();
	}

	private void create() {

		this.setLayout(new BorderLayout());

		messageList.setLayout(new BoxLayout(messageList, BoxLayout.Y_AXIS));
		JScrollPane messageScrollPane = new JScrollPane(this.messageList);

		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

		this.userInfo = new UserPanel(this.manager.getUser());
		eastPanel.add(userInfo);

		JButton createNewMessage = new JButton("New Message"); // TODO viellecht
																// Icon
		createNewMessage.addMouseListener(new newMessageEvent(manager.getUpdaterThread().getBuffer(), manager.getUser()));
		createNewMessage.setToolTipText("Create New Message");

		JButton forceUpdate = new JButton("Update");
		forceUpdate.addMouseListener(new ForceUpdate(manager.getUpdaterThread()));

		eastPanel.add(createNewMessage);
		eastPanel.add(forceUpdate);

		this.add(eastPanel, BorderLayout.EAST);
		this.add(messageScrollPane, BorderLayout.CENTER);
		// TODO ActionListener

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		for(Message msg : this.manager.getMessages()){
			this.addMessageFrame(msg);
		}

		this.repaint();
		this.pack();

	}

	public void addMessageFrame(Message msg) {

		System.out.println("Message Frame : " + msg.getText());

		MessagePanel msgPanel = new MessagePanel(msg, this.manager);

		messageList.add(msgPanel);
		messageList.revalidate();
	}

	public void removeAllMessagePanel() {

		messageList.removeAll();

	}

	private class ForceUpdate extends MouseAdapter {

		UpdaterThread updater;

		public ForceUpdate(UpdaterThread updater) {
			this.updater = updater;
		}

		public void mouseClicked(MouseEvent e) {
			updater.forceUpdate();
		}

	}

	private class newMessageEvent extends MouseAdapter {

		SendeBuffer buffer;
		User user;

		public newMessageEvent(SendeBuffer buffer, User user) {
			this.buffer = buffer;
			this.user = user;
		}

		public void mouseClicked(MouseEvent e) {
			MessageNewAndEditFrame tmp = new MessageNewAndEditFrame(buffer, user);
			tmp.setVisible(true);
		}
	}
}
