package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import config.ClientManager;
import dataOrga.User;

public class Login extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1392377673421616906L;

	private ClientManager manager;
	private JTextField name;
	private JTextField passwort;

	public Login(ClientManager manager) {
		super();
		this.manager = manager;
		this.setTitle("Login");
		this.create();
	}

	private void create() {

		FlowLayout layout = new FlowLayout();
		Container cp = this.getContentPane();
		cp.setLayout(layout);

		// User
		JPanel loginZone = new JPanel();
		loginZone.setLayout(new BorderLayout());

		JPanel n = new JPanel();
		n.add(new JLabel("Benutzername"));
		this.name = new JTextField("Benutzername", 15);
		n.add(this.name);
		loginZone.add(n, BorderLayout.NORTH);

		JPanel p = new JPanel();
		p.add(new JLabel("Passwort"));
		this.passwort = new JPasswordField("Passwort", 15);
		p.add(this.passwort);
		loginZone.add(p, BorderLayout.CENTER);

		JButton LoginButton = new JButton("Login");
		LoginButton.addMouseListener(new LoginButtonEvent(this.manager, this));
		loginZone.add(LoginButton, BorderLayout.SOUTH);

		// Server Change
		JPanel changeServerZone = new JPanel();
		changeServerZone.setLayout(new BorderLayout());
		JLabel serverInfo = new JLabel(this.manager.server.toString());

		JButton changeServerButton = new JButton("Change Server");
		changeServerButton.setToolTipText("Change the Server that will be connected");
		changeServerButton.addMouseListener(new ChangeServerButtonEvent(this.manager, serverInfo));

		changeServerZone.add(serverInfo, BorderLayout.NORTH);
		changeServerZone.add(changeServerButton, BorderLayout.SOUTH);

		cp.add(loginZone);
		cp.add(changeServerZone);

		this.pack();
		this.repaint();
	}

	private class LoginButtonEvent extends MouseAdapter {
		ClientManager manager;
		Login login;

		public LoginButtonEvent(ClientManager manager, Login login) {
			this.manager = manager;
			this.login = login;
		}

		public void mouseClicked(MouseEvent e) {
			String msg = "Benutzername oder Passwort Unbekannt";
			if (login.name.getText() != null || login.passwort.getText() != null) {
				User user = new User(login.name.getText(), login.passwort.getText());

				this.manager.user = user;

				try {
					manager.server.identifyUser(user);
				} catch (IOException e1) {
					msg = "Server nicht Ereichbar: " + this.manager.server.toString();
				}

				if (user.getBerechtigung().getInteger() <= 0) {
					// User Ablehnen
					JOptionPane.showMessageDialog(login.getContentPane(), "Login nicht möglich: " + msg, "Login Failed",
							JOptionPane.ERROR_MESSAGE);

					System.out.println("Login - Failed" + msg);
				} else {
					// User Annehemen
					System.out.println("Login - Worked ");
					System.out.println("User Berechtigung : " + user.getBerechtigung());
					this.login.setVisible(false);

					// TODO Start to Build Main interface

					MainFrame mainFrame = new MainFrame(this.manager);
					mainFrame.setVisible(true);
					this.manager.setMainFrame(mainFrame);

				}
			} else {
				JOptionPane.showMessageDialog(login.getContentPane(),
						"Login nicht möglich: " + "Benutzername oder Passwort sind Leer ", "Login Failed",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private class ChangeServerButtonEvent extends MouseAdapter {
		ClientManager manager;
		JLabel serverInfo;

		public ChangeServerButtonEvent(ClientManager manager, JLabel serverInfo2) {
			this.manager = manager;
			this.serverInfo = serverInfo2;
		}

		public void mouseClicked(MouseEvent e) {
			ChangeServer changeServer = new ChangeServer(this.manager, this.serverInfo);
			changeServer.setVisible(true);
		}

	}

}
