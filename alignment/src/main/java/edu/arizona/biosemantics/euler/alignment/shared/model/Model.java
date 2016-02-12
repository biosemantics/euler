package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree.Node;

public class Model implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Taxonomies taxonomies = new Taxonomies();
	private Articulations articulations = new Articulations();
	private List<Color> colors = new ArrayList<Color>();
	private Map<Object, Color> coloreds = new HashMap<Object, Color>();
	private Map<Object, String> comments = new HashMap<Object, String>();
	private Map<Node, DiagnosticValue> diagnosticValues = new HashMap<Node, DiagnosticValue>();
	
	private Map<Taxon, Map<Taxon, List<Evidence>>> evidenceMap = new HashMap<Taxon, Map<Taxon, List<Evidence>>>();
	
	private LinkedList<Run> runHistory = new LinkedList<Run>();
	
	public Model() { }
	
	public Model(Taxonomies taxonomies, Map<Taxon, Map<Taxon, List<Evidence>>> evidenceMap) {
		this.taxonomies = taxonomies;
		this.evidenceMap = evidenceMap;
	}
	
	public void setDiagnosticValue(Node node, DiagnosticValue diagnosticValue) {
		this.diagnosticValues.put(node, diagnosticValue);
	}
	
	public DiagnosticValue getDiagnosticValue(Node node) {
		if(!diagnosticValues.containsKey(node))
			return DiagnosticValue.MEDIUM;
		return this.diagnosticValues.get(node);
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

	public void changeArticulationRelation(Articulation articulation, Relation newRelation) {
		articulation.setRelation(newRelation);
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
	
	public Set<Relation> getArticulationTypes(
			Taxon taxonA, Taxon taxonB) {
		Set<Relation> articulationTypes = new HashSet<Relation>();
		for(Articulation articulation : articulations) {
			if(articulation.getTaxonA().equals(taxonA) && articulation.getTaxonB().equals(taxonB)) {
				articulationTypes.add(articulation.getRelation());
			}
		}
		return articulationTypes;
	}

	public Articulation getArticulation(Taxon taxonA, Taxon taxonB,
			Relation type) {
		for(Articulation articulation : articulations) {
			if(articulation.getTaxonA().equals(taxonA) && articulation.getTaxonB().equals(taxonB) && articulation.getRelation().equals(type)) 
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

	public void setColors(List<Color> colors) {
		this.colors = colors;
	}

	public void setComments(Map<Object, String> comments) {
		this.comments = comments;
	}

	public void setColoreds(Map<Object, Color> coloreds) {
		this.coloreds = coloreds;
	}

	public void setArticulations(Articulations articulations) {
		this.articulations = articulations;
	}

	public void setRunHistory(LinkedList<Run> runHistory) {
		this.runHistory = runHistory;
	}

	public void setTaxonomies(Taxonomies taxonomies) {
		this.taxonomies = taxonomies;
	}

	public List<Articulation> getArticulations(Type type) {
		List<Articulation> result = new LinkedList<Articulation>();
		for(Articulation articulation : this.articulations) {
			if(articulation.getType().equals(type))
				result.add(articulation);
		}
		return result;
	}

	public List<Articulation> getArticulations(Taxon taxonA, Taxon taxonB, Type type) {
		List<Articulation> result = new LinkedList<Articulation>();
		for(Articulation articulation : this.articulations) 
			if(articulation.getTaxonA().equals(taxonA) && articulation.getTaxonB().equals(taxonB) && articulation.getType().equals(type)) 
				result.add(articulation);
		return result;
	}

	public Map<Taxon, Map<Taxon, List<Evidence>>> getEvidenceMap() {
		return evidenceMap;
	}

	public void setEvidenceMap(Map<Taxon, Map<Taxon, List<Evidence>>> evidenceMap) {
		this.evidenceMap = evidenceMap;
	}

	public Collection<Relation> getAvailableRelations(Taxon taxonA, Taxon taxonB, Type type) {	
		Set<Relation> all = new HashSet<Relation>(Arrays.asList(Relation.values()));
		for(Articulation articulation : this.getArticulations(type)) {
			if(articulation.getTaxonA().equals(taxonA) && articulation.getTaxonB().equals(taxonB)) {
				all.remove(articulation.getRelation());
			}
		}
		return all;
	}

}
