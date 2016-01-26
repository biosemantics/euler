package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;

import edu.arizona.biosemantics.common.taxonomy.Rank;

public class Evidence implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static int ID = 0;	
	private int id = ID++;
	
	private String taxonACharacter; // character + value!
	private String taxonBCharacter;
	private double similarity;
	private double uniqueness;
	private DiagnosticValue diagnosticValue;
	private Rank diagnosticScope;
	
	public Evidence() { 
		
	}
	
	public Evidence(String taxonACharacter, String taxonBCharacter, double similarity, double uniqueness, 
			DiagnosticValue diagnosticValue, Rank diagnosticScope) {
		this.taxonACharacter = taxonACharacter;
		this.taxonBCharacter = taxonBCharacter;
		this.similarity = similarity;
		this.uniqueness = uniqueness;
		this.diagnosticValue = diagnosticValue;
		this.diagnosticScope = diagnosticScope;
	}

	public String getTaxonACharacter() {
		return taxonACharacter;
	}

	public String getTaxonBCharacter() {
		return taxonBCharacter;
	}

	public DiagnosticValue getDiagnosticValue() {
		return diagnosticValue;
	}

	public void setTaxonACharacter(String taxonACharacter) {
		this.taxonACharacter = taxonACharacter;
	}

	public void setTaxonBCharacter(String taxonBCharacter) {
		this.taxonBCharacter = taxonBCharacter;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	public void setUniqueness(double uniqueness) {
		this.uniqueness = uniqueness;
	}

	public void setDiagnosticValue(DiagnosticValue diagnosticValue) {
		this.diagnosticValue = diagnosticValue;
	}

	public double getSimilarity() {
		return similarity;
	}

	public double getUniqueness() {
		return uniqueness;
	}

	public int getId() {
		return id;
	}

	public Rank getDiagnosticScope() {
		return diagnosticScope;
	}

	public void setDiagnosticScope(Rank diagnosticScope) {
		this.diagnosticScope = diagnosticScope;
	}	
}
