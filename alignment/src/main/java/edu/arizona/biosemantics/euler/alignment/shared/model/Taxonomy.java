package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Only ModelControlers should make changes to any class part of the model
 * 
 * @author rodenhausen
 */
public class Taxonomy implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;

	public class DFSTaxaList extends ArrayList<Taxon> implements Serializable {
		
		private static final long serialVersionUID = 1L;

		public DFSTaxaList() {
			for (Taxon taxon : rootTaxa) {
				insertDepthFirst(taxon);
			}
		}

		private void insertDepthFirst(Taxon taxon) {
			add(taxon);
			for (Taxon child : taxon.getChildren())
				insertDepthFirst(child);
		}
	}
	
	public class BFSTaxaList extends ArrayList<Taxon> implements Serializable {
		
		private static final long serialVersionUID = 1L;

		public BFSTaxaList() {
			LinkedList<Taxon> toInserts = new LinkedList<Taxon>();
			for (Taxon taxon : rootTaxa) {
				toInserts.add(taxon);
			}
			insert(toInserts);
		}

		private void insert(LinkedList<Taxon> toInserts) {
			Taxon toInsert = toInserts.poll();
			while(toInsert != null) {
				for(Taxon taxon : toInsert.getChildren())
					toInserts.add(taxon);
				add(toInsert);
				toInsert = toInserts.poll();
			}
		}

		private void insertDepthFirst(Taxon taxon) {
			add(taxon);
			for (Taxon child : taxon.getChildren())
				insertDepthFirst(child);
		}
	}
	
	private String id;
	private String year;
	private String author;
	private List<Taxon> rootTaxa = new ArrayList<Taxon>();

	public Taxonomy() {
	}

	public Taxonomy(String id, String year, String author, List<Taxon> rootTaxa) {
		this.id = id;
		this.year = year;
		this.author = author;
		this.rootTaxa = rootTaxa;
		
		for(Taxon taxon : this.getTaxaBFS())
			taxon.setTaxonomy(this);
	}
	
	public DFSTaxaList getTaxaDFS() {
		return new DFSTaxaList();
	}
	
	public BFSTaxaList getTaxaBFS() {
		return new BFSTaxaList();
	}
	
	public List<Taxon> getRootTaxa() {
		return rootTaxa;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getYear() {
		return year;
	}
	
	public String getSecString() {
		return " sec " + author + " " + year;
	}
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		String result = "Taxonomy: " + id + " " + this.getSecString() + "\n";
		String taxa = "";
		for(Taxon taxon : rootTaxa) {
			taxa += taxon.printHierarchy() + "\n";
		}
		result += "Taxa: \n" + taxa  + "\n";
		return result;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	public void setRootTaxa(List<Taxon> rootTaxa) {
		this.rootTaxa = rootTaxa;
	}
}
