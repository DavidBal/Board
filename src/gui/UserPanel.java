package gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dataOrga.User;

public class UserPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7201478777347979515L;

	User user;

	JLabel name, id, berechtigung;

	public UserPanel(User user) {
		this.user = user;
		this.create();
	}

	private void create() {

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.name = new JLabel(this.user.getName());
		this.name.setPreferredSize(new Dimension(100, 10));
		this.name.setToolTipText("Benutzername");

		this.id = new JLabel(this.user.id() + "");
		this.id.setPreferredSize(new Dimension(100, 10));
		this.id.setToolTipText("Benutzer ID");

		this.berechtigung = new JLabel(this.user.getBerechtigung().toString());
		this.berechtigung.setPreferredSize(new Dimension(100, 10));
		this.berechtigung.setToolTipText("Berechtigung");

		this.add(name);
		this.add(id);
		this.add(berechtigung);

		this.repaint();
	}

	/**
	 * Funktion bietet die Möglichkeit die Label up zu daten.
	 */
	public void update() {
		this.name.setText(this.user.getName());
		this.id.setText(this.user.id() + "");
		this.berechtigung.setText(this.user.getBerechtigung().toString());

		this.repaint();
	}

}
