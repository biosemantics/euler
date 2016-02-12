package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;

public class Collection implements Serializable {

	private int id = -1;
	private String secret;
	private Model model;
	
	public Collection() {
		
	}
	
	public Collection(int id, String secret, Model model) {
		this.id = id;
		this.secret = secret;
		this.model = model;
	}

	public int getId() {
		return id;
	}

	public String getSecret() {
		return secret;
	}

	public boolean hasId() {
		return id != -1;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
