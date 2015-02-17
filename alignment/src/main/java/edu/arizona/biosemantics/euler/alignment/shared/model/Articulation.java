package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.Date;

public class Articulation implements Serializable {
	
	public static int ID = 0;	
	private int id = ID++;
	private Date created = new Date();
	
	private Taxon taxonA;
	private Taxon taxonB;
	private ArticulationType type;

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
		return taxonA.getFullName() + " " + type.getDisplayName() + " " + taxonB.getFullName();
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
	
}
