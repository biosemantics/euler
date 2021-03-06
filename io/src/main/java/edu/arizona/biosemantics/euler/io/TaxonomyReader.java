package edu.arizona.biosemantics.euler.io;

import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigInteger;
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
			String id = "";
			String year = "";
			String author = "";
			List<Taxon> rootTaxa = new LinkedList<Taxon>();
			Map<String, Taxon> nameTaxonMap = new HashMap<String, Taxon>();

			boolean firstLine = false;
			String line;
			while ((line = in.readLine()) != null) {
				if(line.trim().startsWith("#"))
					continue;
				Taxonomy taxonomy = new Taxonomy();
				if (!firstLine) {
					String[] idYearName = extractYearName(line, taxonomy);
					id = idYearName[0];
					year = idYearName[1];
					author = idYearName[2];
					firstLine = true;
				} else {
					handleEdgeline(line, rootTaxa, nameTaxonMap);
				}
			}
			return new Taxonomy(id, year, author, rootTaxa);
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
		//System.out.println(toHex(line));
		line = line.replaceAll("\\s+"," ");
		line = line.trim();
		line = line.replaceFirst("taxonomy", "");
		line = line.trim();
		return line.split(" ");
		//return line.trim().replaceFirst("taxonomy", "").trim().split(" ");
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

	private String toHex(String arg) {
	    return String.format("%040x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
	}
}
