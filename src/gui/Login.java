package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BoxLayout;
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
	private JPanel serverChangePanel;

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
		JPanel loginZone = createUserLoginPanel();

		// Server Change
		serverChangePanel = createServerChangePanel();

		cp.add(loginZone);
		cp.add(serverChangePanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.pack();
		this.repaint();
	}

	private JPanel createUserLoginPanel() {
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
		return loginZone;
	}

	private JPanel createServerChangePanel() {
		JPanel changeServerZone = new JPanel();
		JLabel serverEreichbar = new JLabel("");
		serverEreichbar.setToolTipText("Erreichbarkeit des Servers!!");
		if (this.manager.getServerConector().callServerData()) {
			serverEreichbar.setText("JA");
			serverEreichbar.setForeground(Color.GREEN);
		} else {
			serverEreichbar.setText("Nein");
			serverEreichbar.setForeground(Color.RED);
		}
		changeServerZone.setLayout(new BoxLayout(changeServerZone, BoxLayout.Y_AXIS));
		JLabel serverData = new JLabel(this.manager.getServerConector().getAbteilungsName() + " ; "
				+ this.manager.getServerConector().getAbtID());
		serverData.setToolTipText("[Abteilungs-Name] ; [Abteilungs-ID]");
		JLabel serverAdress = new JLabel(this.manager.getServerConector().getServerIP().getHostAddress() + " ; "
				+ this.manager.getServerConector().getServerPort());
		serverAdress.setToolTipText("[IP - Adresse] ; [Port]");
		JButton changeServerButton = new JButton("Change Server");
		changeServerButton.setToolTipText("Change the Server that will be connected");
		changeServerButton.addMouseListener(new ChangeServerButtonEvent(this.manager, this));

		changeServerZone.add(serverEreichbar);
		changeServerZone.add(serverData);
		changeServerZone.add(serverAdress);
		changeServerZone.add(changeServerButton);
		return changeServerZone;
	}

	protected void recreateserverChangePanel() {
		this.remove(this.serverChangePanel);
		this.serverChangePanel = this.createServerChangePanel();
		this.add(this.serverChangePanel);
		this.revalidate();
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

				this.manager.setUser(user);

				try {
					manager.getServerConector().identifyUser(user);
				} catch (IOException e1) {
					msg = "Server nicht Ereichbar: " + this.manager.getServerConector().toString();
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
		Login login;

		public ChangeServerButtonEvent(ClientManager manager, Login login) {
			this.manager = manager;
			this.login = login;
		}

		public void mouseClicked(MouseEvent e) {
			ChangeServer changeServer = new ChangeServer(this.manager, login);
			changeServer.setVisible(true);
		}
	}

}
