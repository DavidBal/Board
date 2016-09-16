package dataOrga;

public class User {

	private String name;
	public String getName() {
		return name;
	}

	private String pw;
	public String getPw() {
		return pw;
	}

	private Berechtigung berechtigung;

	public Berechtigung getBerechtigung() {
		return berechtigung;
	}

	public void setBerechtigung(int berechtigung) {
		this.berechtigung = Berechtigung.valueOf(berechtigung);
	}

	private int id;

	public int getId() {
		return id;
	}

	

	public User(String name) {
		this.name = name;
		this.pw = null;
		this.berechtigung = Berechtigung.None;
		this.id = 0;
	}

	public User(String name, String pw) {
		this(name);
		this.pw = pw;
	}

	public User(String name, String pw, int berechtigung) {
		this(name, pw);
		this.berechtigung = Berechtigung.valueOf(berechtigung);
	}

	public User(String name, String pw, int berechtigung, int id) {
		this(name, pw, berechtigung);
		this.id = id;
	}

	public String toString() {
		return id + " - " + name + " - " + berechtigung;
	}

}
