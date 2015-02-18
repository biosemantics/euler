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

	public class DFSTaxaList extends ArrayList<Taxon> implements Serializable {

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
	
	private String year;
	private String name;
	private List<Taxon> rootTaxa = new ArrayList<Taxon>();

	public Taxonomy() {
	}

	public Taxonomy(String year, String name, List<Taxon> rootTaxa) {
		this.year = year;
		this.name = name;
		this.rootTaxa = rootTaxa;
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
	
	public String getName() {
		return name;
	}
	
	public String getYear() {
		return year;
	}
	
	@Override
	public String toString() {
		String result = "Taxonomy: " + name + "\n";
		String taxa = "";
		for(Taxon taxon : rootTaxa) {
			taxa += taxon.printHierarchy() + "\n";
		}
		result += "Taxa: \n" + taxa  + "\n";
		return result;
	}

}
