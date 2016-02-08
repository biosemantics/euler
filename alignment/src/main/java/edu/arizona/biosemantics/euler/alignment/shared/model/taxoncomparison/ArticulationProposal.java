package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison;

import java.util.Collection;
import java.util.List;

import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class ArticulationProposal {

	private Taxon taxonA;
	private Taxon taxonB;
	private Collection<RelationProposal> relationProposals;
	private List<Evidence> evidence;
	
	public ArticulationProposal(Taxon taxonA, Taxon taxonB, 
			List<Evidence> evidence,
			Collection<RelationProposal> relationProposals) {
		this.taxonA = taxonA;
		this.taxonB = taxonB;
		this.relationProposals = relationProposals;
		this.evidence = evidence;
	}
		
	public Taxon getCharacterizedTaxonA() {
		return taxonA;
	}

	public Taxon getCharacterizedTaxonB() {
		return taxonB;
	}

	public Collection<RelationProposal> getRelationProposals() {
		return relationProposals;
	}

	public String toString() {
		return taxonA.getTaxonIdentification().getDisplayName() + " " + taxonB.getTaxonIdentification().getDisplayName()
				+ " " + relationProposals;
	}

	public List<Evidence> getEvidence() {
		return evidence;
	}
	
}
