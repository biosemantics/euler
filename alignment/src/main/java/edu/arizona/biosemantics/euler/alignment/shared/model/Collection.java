package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;

import edu.arizona.biosemantics.common.biology.TaxonGroup;
import edu.arizona.biosemantics.common.ling.know.IGlossary;

public class Collection implements Serializable {

	private int id = -1;
	private String secret;
	private Model model;
	private String ontologyPath;
	private String glossaryPath;
	private TaxonGroup taxonGroup = TaxonGroup.PLANT;
	
	public Collection() {
		
	}
	
	public Collection(int id, String secret, Model model) {
		this.id = id;
		this.secret = secret;
		this.model = model;
	}
	
	public Collection(int id, String secret, TaxonGroup taxonGroup, Model model, String glossaryPath, String ontologyPath) {
		this.id = id;
		this.secret = secret;
		this.taxonGroup = taxonGroup;
		this.model = model;
		this.glossaryPath = glossaryPath;
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

	public String getGlossaryPath() {
		return glossaryPath;
	}

	public void setGlossaryPath(String glossaryPath) {
		this.glossaryPath = glossaryPath;
	}

	public TaxonGroup getTaxonGroup() {
		return taxonGroup;
	}

	public void setTaxonGroup(TaxonGroup taxonGroup) {
		this.taxonGroup = taxonGroup;
	}
}
