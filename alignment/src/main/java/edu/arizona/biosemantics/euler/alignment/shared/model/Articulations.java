package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.LinkedList;

public class Articulations extends LinkedList<Articulation> implements Serializable {

	public Articulations getClone() {
		Articulations clone = new Articulations();
		for(Articulation articulation : this)
			clone.add(articulation.getClone());
		return clone;
	}
	
}
