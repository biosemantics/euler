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
	private DiagnosticValue diagnosticValue;
	
	public Overlap() { }

	public Overlap(CharacterState taxonACharacter, CharacterState taxonBCharacter,
			double similarity, DiagnosticValue diagnosticValue) {
		super();
		this.taxonACharacter = taxonACharacter;
		this.taxonBCharacter = taxonBCharacter;
		this.similarity = similarity;
		this.diagnosticValue = diagnosticValue;
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

	public DiagnosticValue getDiagnosticValue() {
		return diagnosticValue;
	}

	public void setDiagnosticValue(DiagnosticValue diagnosticValue) {
		this.diagnosticValue = diagnosticValue;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
