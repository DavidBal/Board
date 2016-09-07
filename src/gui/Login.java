package gui;

import java.awt.BorderLayout;
import java.awt.Container;
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

		BorderLayout layout = new BorderLayout();

		Container cp = this.getContentPane();
		cp.setLayout(layout);

		JPanel n = new JPanel();

		n.add(new JLabel("Benutzername"));
		this.name = new JTextField("Benutzername", 15);
		n.add(this.name);

		JPanel p = new JPanel();
		p.add(new JLabel("Passwort"));
		this.passwort = new JPasswordField("Passwort", 15);
		p.add(this.passwort);

		cp.add(n, BorderLayout.NORTH);
		cp.add(p, BorderLayout.CENTER);

		JButton LoginButton = new JButton("Login");
		LoginButton.addMouseListener(new PushLogin(this.manager, this));

		cp.add(LoginButton, BorderLayout.SOUTH);
		this.pack();
		this.repaint();
	}

	private class PushLogin extends MouseAdapter {
		ClientManager manager;
		Login login;

		public PushLogin(ClientManager manager, Login login) {
			this.manager = manager;
			this.login = login;
		}

		public void mouseClicked(MouseEvent e) {
			String msg = "Benutzername oder Passwort Unbekannt";

			User user = new User(login.name.getText(), login.passwort.getText());

			this.manager.user = user;

			try {
				manager.server.identifyUser(user);
			} catch (IOException e1) {
				msg = "Server nicht Ereichbar: " + this.manager.server.toString();
			}

			if (user.getBerechtigung() <= 0) {
				//User Ablehnen
				JOptionPane.showMessageDialog(login.getContentPane(), "Login nicht möglich: " + msg, "Login Failed",
						JOptionPane.ERROR_MESSAGE);
				
				System.out.println("Login - Failed" + msg);
			} else {
				//User Annehemen 
				System.out.println("Login - Worked ");
				System.out.println("User Berechtigung : " + user.getBerechtigung());
				this.login.setVisible(false);
				
				//TODO Start to Build Main interface  
			}
		}
	}

}
