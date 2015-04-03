package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.util.LinkedList;

public class ModelSwapper {
	
	public Model swap(Model model) {
		Model result = new Model();
		model.setColors(model.getColors());
		model.setComments(model.getComments());
		model.setColoreds(model.getColoreds());
		model.setRunHistory(swap(model.getRunHistory()));
		model.setArticulations(swap(model.getArticulations()));
		model.setTaxonomies(swap(model.getTaxonomies()));
		return model;
	}

	public LinkedList<Run> swap(LinkedList<Run> runHistory) {
		LinkedList<Run> result = new LinkedList<Run>();
		for(Run run : runHistory) {
			result.add(swap(run));
		}
		return result;
	}

	public Run swap(Run run) {
		return new Run(swap(run.getTaxonomies()), swap(run.getArticulations()), run.getRunConfig());
	}

	public Taxonomies swap(Taxonomies taxonomies) {
		Taxonomies result = new Taxonomies();
		result.add(taxonomies.get(1));
		result.add(taxonomies.get(0));
		return result;
	}

	public Articulations swap(Articulations articulations) {
		Articulations result = new Articulations();
		for(Articulation articulation : articulations) {
			result.add(swap(articulation));
		}
		return result;
	}

	public Articulation swap(Articulation articulation) {
		return new Articulation(articulation.getTaxonB(), articulation.getTaxonA(), swap(articulation.getType()));
	}

	public ArticulationType swap(ArticulationType type) {
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
