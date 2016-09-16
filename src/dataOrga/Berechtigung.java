package dataOrga;

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

	public String toString() {
		return this.name();
	}
}