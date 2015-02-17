package edu.arizona.biosemantics.euler.io;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomies;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;

public class EulerInputFileWriter {

	private String outputFile;

	public EulerInputFileWriter(String outputFile) {
		this.outputFile = outputFile;
	}
	
	public void write(Model model) throws IOException {
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"))) {
			String taxonomiesString = "";
			for(Taxonomy taxonomy : model.getTaxonomies()) {
				bw.append("taxonomy " + taxonomy.getName() + "\n");
				for(Taxon rootTaxon : taxonomy.getRootTaxa()) {
					appendTaxon(rootTaxon, bw);
				}
				bw.append("\n");
				taxonomiesString += taxonomy.getName() + "-";
			}
			bw.append("articulation " + taxonomiesString.substring(0, taxonomiesString.length() - 1) + "\n");
			for(Articulation articuation : model.getArticulations()) {
				appendArticulation(articuation, bw, model);
			}
		}
	}

	private void appendArticulation(Articulation articulation, BufferedWriter bw, Model model) throws IOException {
		Taxon taxonA = articulation.getTaxonA();
		Taxon taxonB = articulation.getTaxonB();
		int taxonomyIdA = getId(taxonA, model);
		int taxonomyIdB = getId(taxonB, model);
		String relation = getEulerRelationName(articulation.getType());
		if(relation != null) {
			bw.append("[" + taxonomyIdA + "." + taxonA.getName() + " " + relation + " " + taxonomyIdB + "." + taxonB.getName() + "]\n");
		}
	}

	private int getId(Taxon taxon, Model model) {
		for(int i=0; i<model.getTaxonomies().size(); i++) {
			Taxonomy taxonomy = model.getTaxonomies().get(i);
			if(taxonomy.getTaxaDFS().contains(taxon))
				return i;
		}
		return -1;
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
	
	public String getEulerRelationName(Articulation.Type type) {
		switch(type) {
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
		Taxonomy taxonomyA = new Taxonomy("tax a", rootTaxaA);
		
		List<Taxon> rootTaxaB = new LinkedList<Taxon>();
		Taxon rootB = new Taxon();
		rootB.setName("rootB");
		rootTaxaB.add(rootB);
		Taxon b = new Taxon();
		b.setName("b");
		rootB.addChild(b);
		Taxonomy taxonomyB = new Taxonomy("tax b", rootTaxaB);
		
		model.getTaxonomies().add(taxonomyA);
		model.getTaxonomies().add(taxonomyB);
		
		model.addArticulation(new Articulation(a, b, Type.OVERLAP));
		model.addArticulation(new Articulation(a, b, Type.B_INCLUDES_A));
		model.addArticulation(new Articulation(rootA, rootB, Type.DISJOINT));
		model.addArticulation(new Articulation(rootB, rootA, Type.CONGRUENT));
		
		
		EulerInputFileWriter fw = new EulerInputFileWriter("out.txt");
		fw.write(model);
	}
	
}
