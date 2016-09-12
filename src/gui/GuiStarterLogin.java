package gui;

import config.ClientManager;

public class GuiStarterLogin implements Runnable {

	ClientManager manager;

	public GuiStarterLogin(ClientManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		Login window = new Login(manager);
		window.setVisible(true);
	}

}
