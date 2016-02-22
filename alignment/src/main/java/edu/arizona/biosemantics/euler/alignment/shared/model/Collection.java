package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;

import edu.arizona.biosemantics.common.biology.TaxonGroup;
import edu.arizona.biosemantics.common.ling.know.IGlossary;

public class Collection implements Serializable {

	private int id = -1;
	private String secret;
	private Model model;
	private String ontologyPath;
	private String glossaryPath1;
	private String glossaryPath2;
	private TaxonGroup taxonGroup = TaxonGroup.PLANT;
	
	public Collection() {
		
	}
	
	public Collection(int id, String secret, Model model) {
		this.id = id;
		this.secret = secret;
		this.model = model;
	}
	
	public Collection(int id, String secret, TaxonGroup taxonGroup, Model model, String glossaryPath1, String glossaryPath2,
			String ontologyPath) {
		this.id = id;
		this.secret = secret;
		this.taxonGroup = taxonGroup;
		this.model = model;
		this.glossaryPath1 = glossaryPath1;
		this.glossaryPath2 = glossaryPath2;
		this.ontologyPath = ontologyPath;
	}

	public int getId() {
		return id;
	}

	public String getSecret() {
		return secret;
	}

	public boolean hasId() {
		return id != -1;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getOntologyPath() {
		return ontologyPath;
	}

	public void setOntologyPath(String ontologyPath) {
		this.ontologyPath = ontologyPath;
	}

	public String getGlossaryPath1() {
		return glossaryPath1;
	}

	public void setGlossaryPath1(String glossaryPath) {
		this.glossaryPath1 = glossaryPath;
	}
	
	public String getGlossaryPath2() {
		return glossaryPath2;
	}

	public void setGlossaryPath2(String glossaryPath) {
		this.glossaryPath2 = glossaryPath;
	}

	public TaxonGroup getTaxonGroup() {
		return taxonGroup;
	}

	public void setTaxonGroup(TaxonGroup taxonGroup) {
		this.taxonGroup = taxonGroup;
	}

	public boolean hasGlossaryPath1() {
		return this.glossaryPath1 != null && !this.glossaryPath1.isEmpty();
	}
	
	public boolean hasGlossaryPath2() {
		return this.glossaryPath2 != null && !this.glossaryPath2.isEmpty();
	}

	public boolean hasOntologyPath() {
		return this.ontologyPath != null && !this.ontologyPath.isEmpty();
	}
}
