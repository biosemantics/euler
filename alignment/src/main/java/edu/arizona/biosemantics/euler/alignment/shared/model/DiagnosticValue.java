package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;

public enum DiagnosticValue implements Serializable {
	HIGH("High"), MEDIUM("Medium"), LOW("Low");
	
	private String displayName;
	
	private DiagnosticValue() {
		
	}
	
	private DiagnosticValue(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}