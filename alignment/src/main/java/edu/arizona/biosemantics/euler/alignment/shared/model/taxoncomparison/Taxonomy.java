package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class Taxonomy {

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
	
	private List<Taxon> rootTaxa;
		
	public Taxonomy(List<Taxon> rootTaxa) {
		super();
		this.rootTaxa = rootTaxa;
	}

	public DFSTaxaList getTaxaDFS() {
		return new DFSTaxaList();
	}
	
	public List<Taxon> getRootTaxa() {
		return rootTaxa;
	}
}
