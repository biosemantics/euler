package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	public boolean containsArticulationEntry(Articulation entry) {
		for(Articulation articulation : articulations) {
			if(articulation.equals(entry))
				return true;
		}
		return false;
	}
	
	public void addArticulations(List<Articulation> articulations) {
		for(Articulation articulation : articulations)
			this.addArticulation(articulation);
	}
	
	public void removeArticulations(Collection<Articulation> articulations) {
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

	public List<Articulation> getArticulations(Taxon taxonA, Taxon taxonB) {
		List<Articulation> result = new LinkedList<Articulation>();
		for(Articulation articulation : articulations) {
			if(articulation.getTaxonA().equals(taxonA) && articulation.getTaxonB().equals(taxonB)) {
				result.add(articulation);
			}
		}
		return result;
	}
	
	public Set<ArticulationType> getArticulationTypes(
			Taxon taxonA, Taxon taxonB) {
		Set<ArticulationType> articulationTypes = new HashSet<ArticulationType>();
		for(Articulation articulation : articulations) {
			if(articulation.getTaxonA().equals(taxonA) && articulation.getTaxonB().equals(taxonB)) {
				articulationTypes.add(articulation.getType());
			}
		}
		return articulationTypes;
	}

	public Articulation getArticulation(Taxon taxonA, Taxon taxonB,
			ArticulationType type) {
		for(Articulation articulation : articulations) {
			if(articulation.getTaxonA().equals(taxonA) && articulation.getTaxonB().equals(taxonB) && articulation.getType().equals(type)) 
				return articulation;
		}
		return null;
	}

	public List<Articulation> getArticulationsFor(Taxon taxon) {
		List<Articulation> result = new LinkedList<Articulation>();
		for(Articulation articulation : articulations) {
			if(articulation.getTaxonA().equals(taxon) || articulation.getTaxonB().equals(taxon))
				result.add(articulation);
		}
		return result;
	}

	public Model getSwaped() {
		Model model = new Model();
		model.colors = colors;
		model.comments = comments;
		model.coloreds = coloreds;
		model.runHistory = swap(runHistory);
		model.articulations = swap(articulations);
		model.taxonomies = swap(taxonomies);
		return model;
	}

	private LinkedList<Run> swap(LinkedList<Run> runHistory) {
		LinkedList<Run> result = new LinkedList<Run>();
		for(Run run : runHistory) {
			result.add(swap(run));
		}
		return result;
	}

	private Run swap(Run run) {
		return new Run(swap(run.getTaxonomies()), swap(run.getArticulations()), run.getRunConfig());
	}

	private Taxonomies swap(Taxonomies taxonomies) {
		Taxonomies result = new Taxonomies();
		result.add(taxonomies.get(1));
		result.add(taxonomies.get(0));
		return result;
	}

	private Articulations swap(Articulations articulations) {
		Articulations result = new Articulations();
		for(Articulation articulation : articulations) {
			result.add(new Articulation(articulation.getTaxonB(), articulation.getTaxonA(), swap(articulation.getType())));
		}
		return result;
	}

	private ArticulationType swap(ArticulationType type) {
		switch(type) {
		case A_INCLUDES_B:
			return ArticulationType.B_INCLUDES_A;
		case B_INCLUDES_A:
			return ArticulationType.A_INCLUDES_B;
		default:
			return type;
		}
	}	
	
}
