package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.Date;

public class Articulation implements Serializable {
	
	public enum Type {
		CONGRUENT("is congruent with"), OVERLAP("overlaps with"), A_INCLUDES_B("includes"), B_INCLUDES_A("is included by"), DISJOINT("is disjoint with");

		private String displayName;

		Type(String displayName) {
			this.displayName = displayName;
		}
		
		public String displayName() {
			return displayName;
		}
		
		@Override
		public String toString() {
			return displayName;
		}
	}

	public static int ID = 0;	
	private int id = ID++;
	private Date created = new Date();
	
	private Taxon taxonA;
	private Taxon taxonB;
	private Type type;

	public Articulation(Taxon taxonA, Taxon taxonB, Type type) {
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public String getText() {
		return taxonA.getFullName() + " " + type.displayName + " " + taxonB.getFullName();
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
