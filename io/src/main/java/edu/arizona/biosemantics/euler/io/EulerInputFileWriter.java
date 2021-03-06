package edu.arizona.biosemantics.euler.io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;

public class EulerInputFileWriter {

	private String outputFile;

	public EulerInputFileWriter(String outputFile) {
		this.outputFile = outputFile;
	}
	
	public void write(Model model) throws IOException {
		Map<Taxon, Taxonomy> taxonTaxonomyMap = new HashMap<Taxon, Taxonomy>();
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"))) {
			String taxonomiesIdString = "";
			//String taxonomiesYearString = "";
			//String taxonomiesNameString = "";
			for(Taxonomy taxonomy : model.getTaxonomies()) {
				bw.append("taxonomy " + taxonomy.getId() + " " + taxonomy.getYear() + " " + taxonomy.getAuthor() + "\n");
				
				for(Taxon taxon : taxonomy.getTaxaDFS()) 
					taxonTaxonomyMap.put(taxon, taxonomy);
				for(Taxon rootTaxon : taxonomy.getRootTaxa()) 
					appendTaxon(rootTaxon, bw);
				
				bw.append("\n");
				taxonomiesIdString += taxonomy.getId() + "-";
				//taxonomiesYearString += taxonomy.getYear() + "-";
				//taxonomiesNameString += taxonomy.getAuthor() + "-";
				
			}
			bw.append("articulation " + 
					taxonomiesIdString.substring(0, taxonomiesIdString.length() - 1) + "\n");/* + 
					taxonomiesYearString.substring(0, taxonomiesYearString.length() - 1) + " "  +
					taxonomiesNameString.substring(0, taxonomiesNameString.length() - 1) + "\n"); */
			
			for(Articulation articuation : model.getArticulations()) 
				appendArticulation(articuation, bw, model, taxonTaxonomyMap);
		}
	}

	private void appendArticulation(Articulation articulation, BufferedWriter bw, Model model, Map<Taxon, Taxonomy> taxonTaxonomyMap) throws IOException {
		Taxon taxonA = articulation.getTaxonA();
		Taxon taxonB = articulation.getTaxonB();
		String relation = getEulerRelationName(articulation.getRelation());
		if(relation != null) {
			bw.append("[" + taxonTaxonomyMap.get(taxonA).getId() + "." + taxonA.getName() + " " + relation + " " + 
					taxonTaxonomyMap.get(taxonB).getId() + "." + taxonB.getName() + "]\n");
		}
	}
	
	private void appendTaxon(Taxon taxon, BufferedWriter bw) throws IOException {
		String line = "(" + taxon.getName();
		for(Taxon child : taxon.getChildren()) {
			line += " " + child.getName();
		}
		line += ")\n";
		bw.append(line);
		for(Taxon child : taxon.getChildren()) {
			appendTaxon(child, bw);
		}
	}
	
	public String getEulerRelationName(Relation relation) {
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
			return null;
		}
	}
	
	public static void main(String[] args) throws IOException {
		Model model = new Model();
		
		List<Taxon> rootTaxaA = new LinkedList<Taxon>();
		Taxon rootA = new Taxon();
		rootA.setName("rootA");
		rootTaxaA.add(rootA);
		Taxon a = new Taxon();
		a.setName("a");
		rootA.addChild(a);
		Taxonomy taxonomyA = new Taxonomy("2001", "2001", "tax a", rootTaxaA);
		
		List<Taxon> rootTaxaB = new LinkedList<Taxon>();
		Taxon rootB = new Taxon();
		rootB.setName("rootB");
		rootTaxaB.add(rootB);
		Taxon b = new Taxon();
		b.setName("b");
		rootB.addChild(b);
		Taxonomy taxonomyB = new Taxonomy("2005", "2005", "tax b", rootTaxaB);
		
		model.getTaxonomies().add(taxonomyA);
		model.getTaxonomies().add(taxonomyB);
		
		model.addArticulation(new Articulation(a, b, Relation.OVERLAP, 1.0, Type.USER));
		model.addArticulation(new Articulation(a, b, Relation.B_INCLUDES_A, 1.0, Type.USER));
		model.addArticulation(new Articulation(rootA, rootB, Relation.DISJOINT, 1.0, Type.USER));
		model.addArticulation(new Articulation(rootB, rootA, Relation.CONGRUENT, 1.0, Type.USER));
		
		
		EulerInputFileWriter fw = new EulerInputFileWriter("out.txt");
		fw.write(model);
	}
	
}
