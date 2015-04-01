package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.arizona.biosemantics.common.taxonomy.Rank;

public class Taxon implements Serializable, Comparable<Taxon> {
		
	public static int ID = 0;	
	private int id = ID++;
	
	/**
	 * Taxon hierarchy
	 */
	private Rank rank;
	private Taxon parent;
	private List<Taxon> children = new LinkedList<Taxon>();
	private Taxonomy taxonomy;
	
	/**
	 * Taxon concept
	 */
	private String name = "";
	private String author = "";
	private String year = "";
	
	/**
	 * Description
	 */
	private String description = "";
	
	public Taxon() { }
	
	public Taxon(Rank rank, String name, String author, String year) {
		this.rank = rank;
		this.name = name;
		this.author = author;
		this.year = year;
	}
	
	public Taxon(Rank rank, String name, String author, String year, String description) {
		this(rank, name, author, year);
		this.description = description;
	}
	
	public Taxonomy getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(Taxonomy taxonomy) {
		this.taxonomy = taxonomy;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addChild(Taxon child) {
		child.setParent(this);
		children.add(child);
	}
	
	public void addChild(int index, Taxon child) {
		child.setParent(this);
		children.add(index, child);
	}

	public void removeChild(Taxon taxon) {
		Iterator<Taxon> it = children.iterator();
		while(it.hasNext()) {
			Taxon child = it.next();
			if(child.equals(taxon)) {
				it.remove();
			}
		}
	}
	
	public void removeDescendantRecursively(Taxon taxon) {
		Iterator<Taxon> it = children.iterator();
		while(it.hasNext()) {
			Taxon child = it.next();
			if(child.equals(taxon)) {
				it.remove();
			}
			child.removeDescendantRecursively(taxon);
		}
	}
	
	public void setParent(Taxon parent) {
		this.parent = parent;
	}
	
	public void setChildren(List<Taxon> children) {
		this.children = children;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setYear(String year) {
		this.year = year;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getId() {
		return id;
	}

	public Rank getRank() {
		return rank;
	}

	public String getAuthor() {
		return author;
	}

	public String getYear() {
		return year;
	}

	
	public List<Taxon> getChildren() {
		return children;
	}
	
	public Taxon getParent() {
		return parent;
	}
	
	public boolean hasParent() {
		return parent != null;
	}
	
	public String getName() {
		//by scientific convention
		if(rank != null && rank.equals(Rank.GENUS)) {
			return java.lang.Character.toUpperCase(name.charAt(0)) + name.substring(1);
		}
		return name;
	}
	
	public String getFullName() {
		if(hasParent()) {
			return getParent().getFullName() + " " + getName();
		} else {
			return getName();
		}
	}
	
	public String getBiologicalName() {
		if(hasParent() && getParent().getRank() != null && Rank.equalOrBelowGenus(getParent().getRank())) {
			return getParent().getBiologicalName() + " " + getName();
		} else {
			return getName();
		}
	}
	
	public String getDescription() {
		return description;
	}
	
	public String printHierarchy() {
		return print(0);
	}
	
	@Override 
	public String toString() {
		return this.getFullName();
	}
	
	public String print(int ident) {
		String identation =  new String(new char[ident]).replace("\0", " ");
		String result = identation + getName();
		for(Taxon child : children) {
			result +=  "\n " + child.print(ident + 1);
		}
		//result = result.substring(0, result.length() - 2);
		return result;
	}
	
	@Override
	public int compareTo(Taxon o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Taxon other = (Taxon) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
