package edu.arizona.biosemantics.euler.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.ArticulationType;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomies;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;

public class ArticulationsReader {

	public Articulations read(String text, Model model) throws Exception {
		Articulations result = new Articulations();
		try (BufferedReader in = new BufferedReader(new StringReader(text))) {
			String yearsString = "";
			String namesString = "";
			Map<String, Taxonomy> yearTaxonomyMap = new HashMap<String, Taxonomy>();

			boolean firstLine = false;
			String line;
			while ((line = in.readLine()) != null) {
				if(line.trim().startsWith("#"))
					continue;
				if (!firstLine) {
					String[] yearsNames = extractYearsNames(line);
					if(yearsNames.length != 2) 
						throw new Exception("First line format invalid. Separate taxonomy year and author by space.");
					yearsString = yearsNames[0];
					namesString = yearsNames[1];
					firstLine = true;
					
					String[] years = yearsString.split("-");
					if(years.length != 2) 
						throw new Exception("First line format invalid. Separate years by -");
					for(String year : years) {
						Taxonomy taxonomy = getTaxonomy(year, model);
						if(taxonomy == null)
							throw new Exception("Taxonomy of " + year + " could not be found");
						yearTaxonomyMap.put(year, taxonomy);
					}
				} else {
					Articulation articulation = readArticulationLine(line, yearTaxonomyMap);
					result.add(articulation);
				}
			}
			return result;
		} catch (Exception e) {
			log(LogLevel.ERROR, "Couldn't read input", e);
			throw e;
		}
	}

	private Articulation readArticulationLine(String line, Map<String, Taxonomy> yearTaxonomyMap) throws Exception {
		String[] parts = null;
		try {
			parts = line.trim().substring(1, line.length() - 1).split(" ");
		} catch(Exception e) {
			throw new Exception("Articulation line format invalid. Line: " + line);
		}
		if(parts.length != 3) 
			throw new Exception("Articulation line format invalid. Separate taxon-A relation taxon-B by space. Example: 2015.Rubus equals 1982.Rubus. Line: " + line);
		String taxonA = parts[0];
		
		String[] taxonAParts = taxonA.split("\\.");
		if(taxonAParts.length != 2) 
			throw new Exception("Articulation line format invalid. Separate year and taxon-A by a dot. Example: 2015.Rubus. Line: " + line);
		String yearTaxonA = taxonAParts[0];
		String nameTaxonA = taxonAParts[1];
		String relation = parts[1];
		String taxonB = parts[2];
		String[] taxonBParts = taxonB.split("\\.");
		if(taxonBParts.length != 2) 
			throw new Exception("Articulation line format invalid. Separate year and taxon-A by a dot. Example: 2015.Rubus. Line: " + line);
		String yearTaxonB = taxonBParts[0];
		String nameTaxonB = taxonBParts[1];
		
		Taxonomy taxonomyA = yearTaxonomyMap.get(yearTaxonA);
		Taxonomy taxonomyB = yearTaxonomyMap.get(yearTaxonB);
		if(taxonomyA == null)
			throw new Exception("Could not find taxonomy for the year: " + yearTaxonB + ". Line: " + line);
		if(taxonomyB == null)
			throw new Exception("Could not find taxonomy for the year: " + yearTaxonB + ". Line: " + line);
		
		Taxon a = getTaxon(taxonomyA, nameTaxonA);
		if(a == null)
			throw new Exception("Could not find taxon with name: " + nameTaxonA + ". Line: " + line);
		Taxon b = getTaxon(taxonomyB, nameTaxonB);
		if(b == null)
			throw new Exception("Could not find taxon with name: " + nameTaxonB + ". Line: " + line);
		ArticulationType articulationType = getArticulationTypeFromEuler(relation);
		if(articulationType == null)
			throw new Exception("Relation not recognized: " + relation + ". Line: " + line);
		return new Articulation(a, b, articulationType);
	}
	
	private Taxon getTaxon(Taxonomy taxonomy, String name) {
		for(Taxon taxon : taxonomy.getTaxaDFS()) {
			if(taxon.getName().equals(name)) {
				return taxon;
			}
		}
		return null;
	}

	private ArticulationType getArticulationTypeFromEuler(String text) {
		switch(text) {
		case "includes": 
			return ArticulationType.A_INCLUDES_B;
		case "equals": 
			return ArticulationType.CONGRUENT;
		case "disjoint": 
			return ArticulationType.DISJOINT;
		case "is_included_in": 
			return ArticulationType.B_INCLUDES_A;
		case "overlaps": 
			return ArticulationType.OVERLAP;
		default:
			return null;
		}
	}


	private Taxonomy getTaxonomy(String year, Model model) {
		for(Taxonomy taxonomy : model.getTaxonomies()) {
			if(taxonomy.getYear().equals(year))
				return taxonomy;
		}
		return null;
	}

	private String[] extractYearsNames(String line) {
		return line.replaceFirst("articulation", "").trim().split(" ");
	}
	
	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	
	public static void main(String[] args) throws Exception {
		/*File dir = new File("in");
		
		Taxonomies taxonomies = new Taxonomies();
		for(File file : dir.listFiles()) {
			TaxonomyFileReader reader = new TaxonomyFileReader(file.getAbsolutePath());
			Taxonomy taxonomy;
			try {
				taxonomy = reader.read();
			} catch (Exception e) {
				throw e;
			}
			taxonomies.add(taxonomy);
		}
		Model model = new Model(taxonomies);
		*/
		ArticulationsReader reader = new ArticulationsReader();
		
		//reader.read(readFile("articulations", StandardCharsets.UTF_8), model);
		
		TaxonomyReader taxonomyReader = new TaxonomyReader();
		Taxonomy a = taxonomyReader.read("taxonomy 1993 Groves_MSW2\n"
				+ "(Cheirogaleidae Cheirogaleinae Phanerinae)\n"
				+ "(Cheirogaleinae Allocebus Cheirogaleus Microcebus)\n"
				+ "(Allocebus Allocebus_trichotis)\n"
				+ "(Cheirogaleus Cheirogaleus_major Cheirogaleus_medius)\n"
				+ "(Microcebus Microcebus_coquereli Microcebus_murinus Microcebus_rufus)\n"
				+ "(Phanerinae Phaner)\n"
				+ "(Phaner Phaner_furcifer)");
		
		Taxonomy b = taxonomyReader.read("taxonomy 2005 Groves_MSW3\n"
				+ "(Cheirogaleoidae Allocebus Cheirogaleus Microcebus Mirza Phaner)\n"
				+ "(Allocebus Allocebus_trichotis)\n"
				+ "(Cheirogaleus Cheirogaleus_adipicaudatus Cheirogaleus_crossleyi Cheirogaleus_major Cheirogaleus_medius Cheirogaleus_minusculus Cheirogaleus_ravus Cheirogaleus_sibreei)\n"
				+ "(Microcebus Microcebus_berthae Microcebus_griseorufus Microcebus_murinus Microcebus_myoxinus Microcebus_ravelobensis Microcebus_rufus Microcebus_sambiranensis Microcebus_tavaratra)\n"
				+ "(Mirza Mirza_coquereli)\n"
				+ "(Phaner Phaner_electromontis Phaner_furcifer Phaner_pallescens Phaner_parienti)");
		
		Taxonomies taxonomies = new Taxonomies();
		taxonomies.add(a);
		taxonomies.add(b);
		Model model = new Model(taxonomies);
		Articulations articulations = reader.read(
				"articulation 2005-1993 Groves_MSW3-Groves_MSW2\n"
				+ "[2005.Cheirogaleoidae includes 1993.Cheirogaleidae]\n"
				+ "[2005.Allocebus_trichotis equals 1993.Allocebus_trichotis]\n"
				+ "[2005.Cheirogaleus_adipicaudatus is_included_in 1993.Cheirogaleus_major]\n"
				+ "[2005.Cheirogaleus_crossleyi is_included_in 1993.Cheirogaleus_major]\n"
				+ "[2005.Cheirogaleus_major is_included_in 1993.Cheirogaleus_major]\n"
				+ "[2005.Cheirogaleus_medius is_included_in 1993.Cheirogaleus_medius]\n"
				+ "[2005.Cheirogaleus_minusculus is_included_in 1993.Cheirogaleus_medius]\n"
				+ "[2005.Cheirogaleus_ravus is_included_in 1993.Cheirogaleus_medius]\n"
				+ "[2005.Cheirogaleus_sibreei is_included_in 1993.Cheirogaleus_major]\n"
				+ "[2005.Microcebus_berthae disjoint 1993.Cheirogaleidae]\n"
				+ "[2005.Microcebus_griseorufus is_included_in 1993.Microcebus_murinus]\n"
				+ "[2005.Microcebus_murinus is_included_in 1993.Microcebus_murinus]\n"
				+ "[2005.Microcebus_myoxinus is_included_in 1993.Microcebus_murinus]\n"
				+ "[2005.Microcebus_ravelobensis disjoint 1993.Cheirogaleidae]\n"
				+ "[2005.Microcebus_rufus equals 1993.Microcebus_rufus]\n"
				+ "[2005.Microcebus_sambiranensis disjoint 1993.Cheirogaleidae]\n"
				+ "[2005.Microcebus_tavaratra disjoint 1993.Cheirogaleidae]\n"
				+ "[2005.Mirza_coquereli equals 1993.Microcebus_coquereli]\n"
				+ "[2005.Phaner_electromontis is_included_in 1993.Phaner_furcifer]\n"
				+ "[2005.Phaner_furcifer is_included_in 1993.Phaner_furcifer]\n"
				+ "[2005.Phaner_pallescens is_included_in 1993.Phaner_furcifer]\n"
				+ "[2005.Phaner_parienti is_included_in 1993.Phaner_furcifer]\n", 
				model);
		
		System.out.println(articulations);
	}

}
