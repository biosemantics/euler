package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know;

public interface KnowsModifier {

	public boolean isModifier(String candidate);
	
	public double getWeight(String modifier);
	
}
