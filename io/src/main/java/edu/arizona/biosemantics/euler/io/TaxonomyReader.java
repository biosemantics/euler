package edu.arizona.biosemantics.euler.io;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;

public class TaxonomyReader {

	public Taxonomy read(String text) throws Exception {
		try (BufferedReader in = new BufferedReader(new StringReader(text))) {
			String year = "";
			String name = "";
			List<Taxon> rootTaxa = new LinkedList<Taxon>();
			Map<String, Taxon> nameTaxonMap = new HashMap<String, Taxon>();

			boolean firstLine = false;
			String line;
			while ((line = in.readLine()) != null) {
				if(line.trim().startsWith("#"))
					continue;
				Taxonomy taxonomy = new Taxonomy();
				if (!firstLine) {
					String[] yearName = extractYearName(line, taxonomy);
					year = yearName[0];
					name = yearName[1];
					firstLine = true;
				} else {
					handleEdgeline(line, rootTaxa, nameTaxonMap);
				}
			}
			return new Taxonomy(year, name, rootTaxa);
		} catch (Exception e) {
			log(LogLevel.ERROR, "Couldn't read input file", e);
			throw e;
		}
	}

	private void handleEdgeline(String line, List<Taxon> rootTaxa,
			Map<String, Taxon> nameTaxonMap) throws Exception {
		line = line.trim();
        if(line.startsWith("(") && line.endsWith(")")) {
        	line = line.substring(1, line.length() - 1);
        	String[] parts = line.split(" ");
        	String parentName = parts[0];
        	
        	Taxon parent = null;
        	if(nameTaxonMap.containsKey(parentName))
        		parent = nameTaxonMap.get(parentName);
        	else {
        		parent = new Taxon();
        		parent.setName(parentName);
        		rootTaxa.add(parent);
            	nameTaxonMap.put(parentName, parent);
        	}
         	
    		for(int i=1; i<parts.length; i++) {
    			String childName  = parts[i];
    			Taxon child = new Taxon();
    			child.setName(childName);
    			child.setParent(parent);
	    		parent.addChild(child);
	    		nameTaxonMap.put(childName, child);
    		}
        } else {
        	throw new Exception("Invalid format");
        }
	}

	private String[] extractYearName(String line, Taxonomy taxonomy) {
		return line.trim().replaceFirst("taxonomy", "").trim().split(" ");
	}
	
	public static void main(String[] args) throws Exception {
		TaxonomyReader reader = new TaxonomyReader();
		Taxonomy taxonomy = reader
				.read("taxonomy 1993 Groves_MSW2"
						+ "(Cheirogaleidae Cheirogaleinae Phanerinae)"
						+ "(Cheirogaleinae Allocebus Cheirogaleus Microcebus)"
						+ "	(Allocebus Allocebus_trichotis)"
						+ "	(Cheirogaleus Cheirogaleus_major Cheirogaleus_medius)"
						+ "	(Microcebus Microcebus_coquereli Microcebus_murinus Microcebus_rufus)"
						+ "(Phanerinae Phaner)" + "	(Phaner Phaner_furcifer)");
	}

}
