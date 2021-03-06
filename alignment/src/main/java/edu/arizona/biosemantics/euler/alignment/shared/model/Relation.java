package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;

public enum Relation implements Serializable {
	//CONGRUENT("is congruent with"), OVERLAP("overlaps with"), A_INCLUDES_B("includes"), B_INCLUDES_A("is included by"), DISJOINT("is disjoint with");
	CONGRUENT("==", "is congruent with"), A_INCLUDES_B(">", "is included by"), B_INCLUDES_A("<", "includes"), OVERLAP("><", "overlaps with"), DISJOINT("!", "is disjoint with");

	private static final long serialVersionUID = 1L;
	
	private String displayName;
	private String longDisplayName;

	Relation(String displayName, String longDisplayName) {
		this.displayName = displayName;
		this.longDisplayName = longDisplayName;
	}
	
	public String displayName() {
		return displayName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public String getLongDisplayName() {
		return longDisplayName;
	}
}
