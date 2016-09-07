package dataOrga;

public class User {

	private String name;
	private String pw;
	private int berechtigung;
	private int id;

	public String getName() {
		return name;
	}

	public String getPw() {
		return pw;
	}

	public int getBerechtigung() {
		return berechtigung;
	}
	
	public int id()	{
		return id;
	}
	
	public User(String name, String pw, int berechtigung){
		this.name = name;
		this.pw = pw;
		this.berechtigung = berechtigung;
		this.id = 0;
	}
	
	public User(String name, String pw, int berechtigung, int id){
		this.name = name;
		this.pw = pw;
		this.berechtigung = berechtigung;
		this.id = id;
	}
	
	public User(String name){
		this.name = name;
		this.pw = null;
		this.berechtigung = 0;
		this.id = 0;
	}
	
	public User(String name , String pw){
		this.name = name;
		this.pw = pw;
		this.berechtigung = 0;
		this.id = 0;
	}

	public void setBerechtigung(int berechtigung) {
		this.berechtigung = berechtigung;
	}
	
}
