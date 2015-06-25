package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;

public class PossibleWorld implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static int ID = 0;	
	private int id = ID++;
	
	private String url;
	private String displayName;
	
	public PossibleWorld() { }
	
	public PossibleWorld(String url, String displayName) {
		super();
		this.url = url;
		this.displayName = displayName;
	}
	
	public String getUrl() {
		return url;
	}
	
	
	
	public void setUrl(String url) {
		this.url = url;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public int getId() {
		return id;
	}
}
