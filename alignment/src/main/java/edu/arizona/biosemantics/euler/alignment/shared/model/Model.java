package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Model implements Serializable {
	
	private Taxonomies taxonomies = new Taxonomies();
	private Articulations articulations = new Articulations();
	private List<Color> colors = new ArrayList<Color>();
	private Map<Object, Color> coloreds = new HashMap<Object, Color>();
	private Map<Object, String> comments = new HashMap<Object, String>();
	
	private LinkedList<Run> runHistory = new LinkedList<Run>();
	
	public Model() { }
	
	public Model(Taxonomies taxonomies) {
		this.taxonomies = taxonomies;
	}
	
	public List<Color> getColors() {
		return colors;
	}

	public boolean hasColors() {
		return !colors.isEmpty();
	}
	
	public boolean hasComment(Object object) {
		return comments.containsKey(object) && !comments.get(object).trim().isEmpty();
	}

	public String getComment(Object object) {
		if (comments.containsKey(object))
			return comments.get(object);
		return "";
	}
	
	public Color getColor(Object object) {
		if (coloreds.containsKey(object))
			return coloreds.get(object);
		return null;
	}

	public Taxonomies getTaxonomies() {
		return taxonomies;
	}

	public boolean isCommented(Object object) {
		return comments.containsKey(object);
	}

	public boolean hasColor(Object object) {
		return coloreds.containsKey(object) && coloreds.get(object) != null;
	}

	public void setComment(Object object, String comment) {
		comments.put(object, comment);
	}

	public void setColor(Object object, Color color) {
		coloreds.put(object, color);
	}	
	
	public Articulations getArticulations() {
		return articulations;
	}
	
	public void clearArticulations() {
		articulations.clear();
		}
	
	public void addArticulation(Articulation articulation) {
		this.articulations.add(articulation);
		}
	
	public boolean containsArticulationEntry(ArticulationEntry entry) {
		for(Articulation articulation : articulations) {
			ArticulationEntry articulationEntry = new ArticulationEntry(articulation);
			if(articulationEntry.equals(entry))
				return true;
		}
		return false;
	}
	
	public void addArticulations(List<Articulation> articulations) {
		for(Articulation articulation : articulations)
			this.addArticulation(articulation);
	}
	
	public void removeArticulations(List<Articulation> articulations) {
		for(Articulation articulation : articulations) 
			this.removeArticulation(articulation);
	}

	private void removeArticulation(Articulation articulation) {
		this.articulations.remove(articulation);
	}

	@Override
	public String toString() {
		String result = taxonomies.toString() + "\n\n";
		result += "Colors: " + colors.toString()  + "\n";
		result += "Coloreds: " +  coloreds.toString()  + "\n";
		result += "Comments: " + comments.toString()  + "\n";
		return result;
	}

	public boolean hasArticulationFor(Taxon taxon) {
		for(Articulation articulation : articulations) {
			if(articulation.getTaxonA().equals(taxon) || articulation.getTaxonB().equals(taxon))
				return true;
		}
		return false;
	}

	public void changeArticulationType(Articulation articulation, ArticulationType newType) {
		articulation.setType(newType);
	}
	
	public void addRun(Run run) {
		this.runHistory.add(run);
	}

	public LinkedList<Run> getRunHistory() {
		return runHistory;
	}
	
	public Map<Object, String> getComments() {
		return comments;
	}
	
	public Map<Object, Color> getColoreds() {
		return coloreds;
	}
	
	
}
