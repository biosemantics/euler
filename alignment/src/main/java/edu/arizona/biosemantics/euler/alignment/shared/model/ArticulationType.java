package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;

public enum ArticulationType implements Serializable {
	//CONGRUENT("is congruent with"), OVERLAP("overlaps with"), A_INCLUDES_B("includes"), B_INCLUDES_A("is included by"), DISJOINT("is disjoint with");
	CONGRUENT("=", "is congruent with"), OVERLAP("><", "overlaps with"), A_INCLUDES_B("<", "includes"), B_INCLUDES_A(">", "is included by"), DISJOINT("!", "is disjoint with");

	private String displayName;
	private String longDisplayName;

	ArticulationType(String displayName, String longDisplayName) {
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
