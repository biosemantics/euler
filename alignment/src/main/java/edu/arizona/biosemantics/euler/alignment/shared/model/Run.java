package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Run implements Serializable {

	public static int ID = 0;	
	private int id = ID++;
	private Date created = new Date();
	
	private Taxonomies taxonomies;
	private Articulations articulations;
	private RunConfig runConfig;
	private RunOutput output;
	
	public Run() { }
	
	public Run(Taxonomies taxonomies, Articulations articulations,
			RunConfig runConfig) {
		super();
		this.taxonomies = taxonomies; //taxonomies.clone(); not necessary to clone if taxonomies can not be editted
		this.articulations = articulations;
		this.runConfig = runConfig;
	}
	public void setOutput(RunOutput output) {
		this.output = output;
	}
	public Taxonomies getTaxonomies() {
		return taxonomies;
	}
	public Articulations getArticulations() {
		return articulations;
	}
	public RunConfig getRunConfig() {
		return runConfig;
	}
	public RunOutput getOutput() {
		return output;
	}
	
	public boolean hasOutput() {
		return output != null;
	}
	
	public int getId() {
		return id;
	}
	
	public Date getCreated() {
		return created;
	}
	
}
