package edu.arizona.biosemantics.euler.io;

import java.io.BufferedReader;
import java.io.StringReader;
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
					yearsString = yearsNames[0];
					namesString = yearsNames[1];
					firstLine = true;
					
					for(String year : yearsString.split("-")) {
						Taxonomy taxonomy = getTaxonomy(year, model);
						if(taxonomy != null)
							yearTaxonomyMap.put(year, getTaxonomy(year, model));
					}
				} else {
					result.add(readArticulationLine(line, yearTaxonomyMap));
				}
			}
			return result;
		} catch (Exception e) {
			log(LogLevel.ERROR, "Couldn't read input", e);
			throw e;
		}
	}

	private Articulation readArticulationLine(String line, Map<String, Taxonomy> yearTaxonomyMap) {
		String[] parts = line.trim().substring(1, line.length() - 1).split(" ");
		String taxonA = parts[0];
		String yearTaxonA = taxonA.split("\\.")[0];
		String nameTaxonA = taxonA.split("\\.")[1];
		String relation = parts[1];
		String taxonB = parts[2];
		String yearTaxonB = taxonB.split("\\.")[0];
		String nameTaxonB = taxonB.split("\\.")[1];
		
		Taxonomy taxonomyA = yearTaxonomyMap.get(yearTaxonA);
		Taxonomy taxonomyB = yearTaxonomyMap.get(yearTaxonB);
		if(taxonomyA != null && taxonomyB != null) {
			Taxon a = getTaxon(taxonomyA, nameTaxonA);
			Taxon b = getTaxon(taxonomyB, nameTaxonB);
			ArticulationType articulationType = getArticulationTypeFromEuler(relation);
			if(a != null && b != null && articulationType != null) {
				return new Articulation(a, b, articulationType);
			}
		}
		return null;
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
	
	public static void main(String[] args) throws Exception {
		ArticulationsReader reader = new ArticulationsReader();
		
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
