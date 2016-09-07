package dataOrga;

public class User {

	private String name;
	private String pw;
	private Berechtigung berechtigung;

	public enum Berechtigung {

		Admin(3), Abteilungsleiter(2), User(1), None(0);

		private int b;

		public int getInteger() {
			return this.b;
		}

		private Berechtigung(int b) {
			this.b = b;
		}

		static public Berechtigung valueOf(int i) {
			for (Berechtigung be : Berechtigung.values()) {
				if (i == be.b) {
					return be;
				}
			}
			return None;
		}
		
		public String toString(){
			return this.name();
		}
	}

	private int id;

	public String getName() {
		return name;
	}

	public String getPw() {
		return pw;
	}

	public Berechtigung getBerechtigung() {
		return berechtigung;
	}

	public int id() {
		return id;
	}

	public User(String name) {
		this.name = name;
		this.pw = null;
		this.berechtigung = Berechtigung.None;
		this.id = 0;
	}

	public User(String name, String pw) {
		this.name = name;
		this.pw = pw;
		this.berechtigung = Berechtigung.None;
		this.id = 0;
	}

	public User(String name, String pw, int berechtigung) {
		this.name = name;
		this.pw = pw;
		this.berechtigung = Berechtigung.valueOf(berechtigung);
		this.id = 0;
	}

	public User(String name, String pw, int berechtigung, int id) {
		this.name = name;
		this.pw = pw;
		this.berechtigung = Berechtigung.valueOf(berechtigung);
		this.id = id;
	}

	public String toString() {
		return id + " - " + name + " - " + berechtigung;
	}

	public void setBerechtigung(int berechtigung) {
		this.berechtigung = Berechtigung.valueOf(berechtigung);
	}

}
