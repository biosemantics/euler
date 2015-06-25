package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.LinkedList;

public class Taxonomies extends LinkedList<Taxonomy> implements Serializable /*implements Cloneable*/ {
	
	private static final long serialVersionUID = 1L;

	
	/*@Override
	public Taxonomies clone() {
		Taxonomies clone = new Taxonomies();
		for(Taxonomy taxonomy : this)
			clone.add(taxonomy.clone());
		return clone;
	}*/
	
}