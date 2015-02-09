package edu.arizona.biosemantics.euler;

public enum Encoding {
	MN("Use polynomial encoding, get mir only"),
	MNPW("Use polynomial encoding, get possible worlds"),
	MNVE("Use polynomial encoding, get valid euler regions"),
	MNCB("Use polynomial encoding, get possible worlds with combined concepts"),
	VR("Use binary encoding, get mir only"),
	VRPW("Use binary encoding, get possible worlds"),
	VRVE("use binary encoding, get valid euler regions");
	
	/*
	VR, VRPW, VRVE, VRCB, MN, MNPW, MNVE, MNCB, DR, 
	DRPW, DRVE, DRCB, DL, DLPW, DLVE, DLCB */
	
	private String explanation;
	
	Encoding(String explanation) {
		this.explanation = explanation;
	}
	
	public String getExplanation() {
		return this.explanation;
	}
}
