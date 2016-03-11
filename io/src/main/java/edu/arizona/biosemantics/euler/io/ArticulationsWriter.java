package edu.arizona.biosemantics.euler.io;

import java.util.HashMap;
import java.util.Map;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;

public class ArticulationsWriter {

	public String write(Model model) throws Exception {
		String result = "";
		String idString = "";
		//String yearsString = "";
		//String taxonomyNamesString = "";
		Map<Taxon, Taxonomy> taxonTaxonomyMap = new HashMap<Taxon, Taxonomy>();
		
		
		for(Taxonomy taxonomy : model.getTaxonomies()) {
			idString += taxonomy.getId() + "-";
			//yearsString += taxonomy.getYear() + "-";
			//taxonomyNamesString += taxonomy.getName() + "-";
			for(Taxon taxon : taxonomy.getTaxaDFS())
				taxonTaxonomyMap.put(taxon, taxonomy);
		}
		idString = idString.substring(0, idString.length() - 1);
		//yearsString = yearsString.substring(0, yearsString.length() - 1);
		//taxonomyNamesString = taxonomyNamesString.substring(0, taxonomyNamesString.length() -1);
		
		result += "articulation " +  idString + "\n"; //yearsString + " " + taxonomyNamesString + "\n";
		
		for(Articulation articulation : model.getArticulations()) {
			Taxon taxonA = articulation.getTaxonA();
			Taxon taxonB = articulation.getTaxonB();
			String taxonAString = taxonTaxonomyMap.get(taxonA).getId() + "." + taxonA.getName();
			String taxonBString = taxonTaxonomyMap.get(taxonB).getId() + "." + taxonB.getName();
			String relation = getEulerRelation(articulation.getRelation());
			if(relation != null) 
				result += "[" + taxonAString + " " + relation + " " + taxonBString + "]\n";
		}
		return result;
	}

	private String getEulerRelation(Relation relation) {
		switch(relation) {
		case A_INCLUDES_B:
			return "includes";
		case B_INCLUDES_A:
			return "is_included_in";
		case CONGRUENT:
			return "equals";
		case DISJOINT:
			return "disjoint";
		case OVERLAP:
			return "overlaps";
		default:
			break;
		}
		return null;
	}
	
}
