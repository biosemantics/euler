package edu.arizona.biosemantics.euler.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;

public class TaxonomyFileReader {

	private String file;

	public TaxonomyFileReader(String file) {
		this.file = file;
	}
	
	public Taxonomy read() throws Exception {
		try(BufferedReader in = new BufferedReader(new InputStreamReader(
                      new FileInputStream(file), "UTF8"))) {
			String name = "";
			List<Taxon> rootTaxa = new LinkedList<Taxon>();
			Map<String, Taxon> nameTaxonMap = new HashMap<String, Taxon>();
			
			boolean firstLine = false;
			String line;
	        while ((line = in.readLine()) != null) {
	        	Taxonomy taxonomy = new Taxonomy();
	        	if(!firstLine) {
	        		name = extractName(line, taxonomy);
	        		firstLine = true;
	        	} else {
	        		handleEdgeline(line, rootTaxa, nameTaxonMap);
	        	}
	        }
			return new Taxonomy(name, rootTaxa);
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

	private String extractName(String line, Taxonomy taxonomy) {
		return line.trim().replaceFirst("taxonomy", "").trim();
	}
	
	public static void main(String[] args) throws Exception {
		TaxonomyFileReader fileTaxonomyReader = new TaxonomyFileReader("C:/Users/rodenhausen/etcsite/users/1068/euler/1.txt");
		Taxonomy t = fileTaxonomyReader.read();
	}
	
}
