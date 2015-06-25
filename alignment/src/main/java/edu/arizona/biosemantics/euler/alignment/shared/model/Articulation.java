package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.Date;

public class Articulation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static int ID = 0;	
	private int id = ID++;
	private Date created = new Date();
	
	private Taxon taxonA;
	private Taxon taxonB;
	private ArticulationType type;

	public Articulation() {}
	
	public Articulation(Taxon taxonA, Taxon taxonB, ArticulationType type) {
		this.taxonA = taxonA;
		this.taxonB = taxonB;
		this.type = type;
	}

	public Taxon getTaxonA() {
		return taxonA;
	}

	public Taxon getTaxonB() {
		return taxonB;
	}

	public ArticulationType getType() {
		return type;
	}

	public void setType(ArticulationType type) {
		this.type = type;
	}
	
	public String getText() {
		return taxonA.getBiologicalName() + " " + type.getDisplayName() + " " + taxonB.getBiologicalName();
	}
	
	public int getId() {
		return id;
	}
	
	public Date getCreated() {
		return created;
	}

	public Articulation getClone() {
		Articulation clone = new Articulation(taxonA, taxonB, type); // not necessary to clone taxa, since they 
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
		if (type != other.type)
			return false;
		return true;
	}
	
}
