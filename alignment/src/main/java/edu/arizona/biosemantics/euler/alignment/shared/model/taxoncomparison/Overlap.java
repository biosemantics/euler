package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison;

import java.io.Serializable;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;

public class Overlap implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static int ID = 0;	
	private int id = ID++;
	
	private CharacterState taxonACharacter;
	private CharacterState taxonBCharacter;
	private double similarity;
	private double uniqueness;
	private DiagnosticValue diagnosticValue;
	private Rank diagnosticScope;
	
	public Overlap() { }

	public Overlap(CharacterState taxonACharacter, CharacterState taxonBCharacter,
			double similarity, double uniqueness,
			DiagnosticValue diagnosticValue, Rank diagnosticScope) {
		super();
		this.taxonACharacter = taxonACharacter;
		this.taxonBCharacter = taxonBCharacter;
		this.similarity = similarity;
		this.uniqueness = uniqueness;
		this.diagnosticValue = diagnosticValue;
		this.diagnosticScope = diagnosticScope;
	}

	public CharacterState getTaxonACharacter() {
		return taxonACharacter;
	}

	public void setTaxonACharacter(CharacterState taxonACharacter) {
		this.taxonACharacter = taxonACharacter;
	}

	public CharacterState getTaxonBCharacter() {
		return taxonBCharacter;
	}

	public void setTaxonBCharacter(CharacterState taxonBCharacter) {
		this.taxonBCharacter = taxonBCharacter;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	public double getUniqueness() {
		return uniqueness;
	}

	public void setUniqueness(double uniqueness) {
		this.uniqueness = uniqueness;
	}

	public DiagnosticValue getDiagnosticValue() {
		return diagnosticValue;
	}

	public void setDiagnosticValue(DiagnosticValue diagnosticValue) {
		this.diagnosticValue = diagnosticValue;
	}

	public Rank getDiagnosticScope() {
		return diagnosticScope;
	}

	public void setDiagnosticScope(Rank diagnosticScope) {
		this.diagnosticScope = diagnosticScope;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
