package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class RunOutput implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//what form of output to consider all?
	
	private RunOutputType type;
	private List<PossibleWorld> possibleWorlds = new LinkedList<PossibleWorld>();
	private String aggregateUrl;
	private String diagnosisUrl;
	
	public RunOutput() { }
	
	public RunOutput(RunOutputType type) {
		this.type = type;
	}
		
	public RunOutput(RunOutputType type, List<PossibleWorld> possibleWorlds,
			String aggregateUrl, String diagnosisUrl) {
		super();
		this.type = type;
		this.possibleWorlds = possibleWorlds;
		this.aggregateUrl = aggregateUrl;
		this.diagnosisUrl = diagnosisUrl;
	}

	public List<PossibleWorld> getPossibleWorlds() {
		return possibleWorlds;
	}	
	
	public RunOutputType getType() {
		return type;
	}

	public String getAggregateUrl() {
		return aggregateUrl;
	}
	
	public String getDiagnosisUrl() {
		return diagnosisUrl;
	}
	
}
