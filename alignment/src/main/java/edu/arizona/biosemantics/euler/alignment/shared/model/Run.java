package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.List;

public class Run implements Serializable {

	private Taxonomies taxonomies;
	private Articulations articulations;
	private RunConfig runConfig;
	private Output output;
	
	public Run() { }
	
	public Run(Taxonomies taxonomies, Articulations articulations,
			RunConfig runConfig) {
		super();
		this.taxonomies = taxonomies; //taxonomies.clone(); not necessary to clone if taxonomies can not be editted
		this.articulations = (Articulations)articulations.getClone();
		this.runConfig = runConfig;
	}
	public void setOutput(Output output) {
		this.output = output;
	}
	public List<Taxonomy> getTaxonomies() {
		return taxonomies;
	}
	public List<Articulation> getArticulations() {
		return articulations;
	}
	public RunConfig getRunConfig() {
		return runConfig;
	}
	public Output getOutput() {
		return output;
	}
	
}
