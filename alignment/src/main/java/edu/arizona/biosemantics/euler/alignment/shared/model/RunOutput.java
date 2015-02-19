package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class RunOutput implements Serializable {
	//what form of output to consider all?
	
	private RunOutputType type;
	private List<PossibleWorld> possibleWorlds = new LinkedList<PossibleWorld>();

	public RunOutput() { }
	
	public RunOutput(RunOutputType type) {
		this.type = type;
	}
	
	public RunOutput(RunOutputType type, List<PossibleWorld> possibleWorlds) {
		super();
		this.type = type;
		this.possibleWorlds = possibleWorlds;
	}

	public List<PossibleWorld> getPossibleWorlds() {
		return possibleWorlds;
	}	
	
	public RunOutputType getType() {
		return type;
	}
}
