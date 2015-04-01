package edu.arizona.biosemantics.euler.alignment.client.articulate;

import java.util.LinkedList;
import java.util.List;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.ModelSwapper;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class TaxonDetailsProvider {

	private ModelSwapper modelSwapper = new ModelSwapper();
	
	public String getTaxonDetails(Taxon taxon, Model model) {
		List<Taxon> ancestors = new LinkedList<Taxon>();
		Taxon parent = taxon.getParent();
		while(parent != null) {
			ancestors.add(parent);
			parent = parent.getParent();
		}

		String taxonomy = "";
		String prefix = "";
		for(int i=ancestors.size() - 1; i >= 0; i--) {
			Taxon ancestor = ancestors.get(i);
			prefix += "-";
			taxonomy += "<p>" + prefix + " " + 
					(ancestor.getRank() == null ? "" : ancestor.getRank().name()) + " " + 
					ancestor.getName() + " " + 
					ancestor.getAuthor() + " " + 
					ancestor.getYear() + 
					"</p>";
		}
					
		String infoText = "<p><b>Rank:&nbsp;</b>" + (taxon.getRank() == null ? "" : taxon.getRank().name()) + "</p>" +
				"<p><b>Name:&nbsp;</b>" + taxon.getName() + "</p>" +
				"<p><b>Author:&nbsp;</b>" + taxon.getAuthor() + "</p>" +
				"<p><b>Year:&nbsp;</b>" + taxon.getYear() + "</p>" +
				"<p><b>Taxonomy:&nbsp;</b>" + taxonomy + "</p>" +
				"<p><b>Description:&nbsp;</b>" + taxon.getDescription().replaceAll("\n", "</br>") + "</p>";
		if(model.hasComment(taxon))
			infoText +=	"<p><b>Comment:&nbsp;</b>" + model.getComment(taxon) + "</p>";
		if(model.hasColor(taxon))
			infoText += "<p><b>Color:&nbsp;</b>" + model.getColor(taxon).getUse() + "</p>";
		
		String articulationsText = "";
		List<Articulation> articulations = model.getArticulationsFor(taxon);
		for(Articulation articulation : articulations) {
			if(articulation.getTaxonA().equals(taxon))
				articulationsText += articulation.getText() + "</br>";
			else
				articulationsText += modelSwapper.swap(articulation).getText() + "</br>";
		}
		if(!articulations.isEmpty()) 
			infoText += "<p><b>Articulations:&nbsp;</b></br>" + articulationsText + "</p>";
		return infoText;
	}
	
}
