package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.Date;

public class Articulation implements Serializable {
	
	public static enum Type {
		USER("User"), MACHINE("Machine");
		
		private String displayName;

		private Type(String displayName) {
			this.displayName = displayName;
		}
		
		public String getDisplayName() {
			return displayName;
		}
	}
	
	private static final long serialVersionUID = 1L;
	
	public static int ID = 0;	
	private int id = ID++;
	private Date created = new Date();
	
	private Taxon taxonA;
	private Taxon taxonB;
	private Relation relation;
	private Type type;
	private double confidence = 1.0;

	public Articulation() {}
	
	public Articulation(Taxon taxonA, Taxon taxonB, Relation relation, double confidence, Type type) {
		this.taxonA = taxonA;
		this.taxonB = taxonB;
		this.relation = relation;
		this.type = type;
		this.confidence = confidence;
	}

	public Type getType() {
		return type;
	}

	public Taxon getTaxonA() {
		return taxonA;
	}

	public Taxon getTaxonB() {
		return taxonB;
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}
	
	public String getText() {
		return taxonA.getBiologicalName() + " " + relation.getDisplayName() + " " + taxonB.getBiologicalName();
	}
	
	public int getId() {
		return id;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public Articulation getClone() {
		Articulation clone = new Articulation(taxonA, taxonB, relation, confidence, type); // not necessary to clone taxa, since they 
		//are assumed to be unmodifiable
		clone.created = (Date)created.clone();
		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Articulation other = (Articulation) obj;
		if (taxonA == null) {
			if (other.taxonA != null)
				return false;
		} else if (!taxonA.equals(other.taxonA))
			return false;
		if (taxonB == null) {
			if (other.taxonB != null)
				return false;
		} else if (!taxonB.equals(other.taxonB))
			return false;
		if (relation != other.relation)
			return false;
		return true;
	}
	
}
