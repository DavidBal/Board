package gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import config.ClientManager;
import update.ServerConector;

public class ChangeServer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1920333483900996795L;

	private ClientManager manager;
	private JTextField ip;
	private JTextField port;
	private Login login;

	public ChangeServer(ClientManager manager, Login login) {
		this.manager = manager;
		this.login = login;
		this.setTitle("Server Change");
		this.create();
	}

	public void create() {

		this.setLayout(new BorderLayout());

		JPanel ipZone = new JPanel();
		ipZone.add(new JLabel("IP or ?")); // TODO
		this.ip = new JTextField(manager.getServerConector().getServerIP().toString(), 15);
		ipZone.add(this.ip);
		this.add(ipZone, BorderLayout.NORTH);

		JPanel portZone = new JPanel();
		portZone.add(new JLabel("Port"));
		this.port = new JTextField(Integer.toString(manager.getServerConector().getServerPort()), 15);
		portZone.add(this.port);
		this.add(portZone, BorderLayout.CENTER);

		JButton applyChange = new JButton("Apply Change");
		applyChange.addMouseListener(new applyServerChangeButtonEvent(this.manager, this));
		this.add(applyChange, BorderLayout.SOUTH);

		this.pack();
		this.repaint();

	}

	private class applyServerChangeButtonEvent extends MouseAdapter {

		ClientManager manager;
		ChangeServer changeServer;

		public applyServerChangeButtonEvent(ClientManager manager, ChangeServer changeServer) {
			this.manager = manager;
			this.changeServer = changeServer;
		}

		public void mouseClicked(MouseEvent e) {
			try {
				manager.changeMainServer(new ServerConector(this.changeServer.ip.getText(),
						Integer.valueOf(this.changeServer.port.getText())));

				changeServer.setVisible(false);

				changeServer.login.recreateserverChangePanel();

			} catch (UnknownHostException ex) {
				JOptionPane.showMessageDialog(changeServer.getContentPane(), "Der Server ist nicht ereichbar!",
						"Server Change Failed", JOptionPane.ERROR_MESSAGE);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(changeServer.getContentPane(), "Der Port ist keine Zahl",
						"Server Change Failed", JOptionPane.ERROR_MESSAGE);
			}

		}

	}
}
