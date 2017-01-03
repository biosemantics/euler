package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state;

import java.util.Set;

import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.KnowsModifier;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.KnowsSynonymy;

public class StateSimilarity {

	private KnowsModifier knowsModifier;
	private KnowsSynonymy knowsSynonymy;

	public StateSimilarity(KnowsModifier knowsModifier, KnowsSynonymy knowsSynonymy) {
		this.knowsModifier = knowsModifier;
		this.knowsSynonymy = knowsSynonymy;
	}
	
	public double getSimilarity(State a, State b) {
		if(!a.getType().equals(b.getType()))
			return 0;
		
		double weightA = 1.0;
		double weightB = 1.0;
		if(a instanceof ModifiedState) {
			weightA = getModifierWeight((ModifiedState)a);
			a = ((ModifiedState)a).getRawState();
		}
		if(b instanceof ModifiedState) {
			weightB = getModifierWeight((ModifiedState)b);
			b = ((ModifiedState)b).getRawState();
		}
		
		double modifierFactor = Math.min(weightA/weightB, weightB/weightA);
		switch(a.getType()) {
		case CATEGORICAL:
			return modifierFactor * getCategoricalSimilarity((Categorical)a, (Categorical)b);
		case DISCRETE_NUMERICAL_RANGE:
			return modifierFactor * getDiscreteNumericalRangeSimilarity((DiscreteNumericalRange)a, (DiscreteNumericalRange)b);
		case NUMERICAL_RANGE:
			return modifierFactor * getNumericalRangeSimilarity((NumericalRange)a, (NumericalRange)b);
		}
		return 0;
	}

	private double getModifierWeight(ModifiedState a) {
		double weight = knowsModifier.getWeight(a.getModifier());
		while(a.getState() instanceof ModifiedState) {
			a = (ModifiedState)a.getState();
			weight *= knowsModifier.getWeight(a.getModifier());
		}
		return weight;
	}

	private double getDiscreteNumericalRangeSimilarity(DiscreteNumericalRange a, DiscreteNumericalRange b) {
		double maxLower = Math.max(a.getFrom(), b.getFrom());
		double minUpper = Math.min(a.getTo(), b.getTo());
		double maxUpper = Math.max(a.getTo(), b.getTo());
		double minLower = Math.min(a.getFrom(), b.getFrom());
		
		if(minUpper <= maxLower) {
			//overlap from lower to upper
			return (maxLower - minUpper + 1)/(maxUpper - minLower + 1);
		}else 
			return 0; // no overlap
	}

	private double getNumericalRangeSimilarity(NumericalRange a, NumericalRange b) {
		double maxLower = Math.max(a.getFrom(), b.getFrom());
		double minUpper = Math.min(a.getTo(), b.getTo());
		double maxUpper = Math.max(a.getTo(), b.getTo());
		double minLower = Math.min(a.getFrom(), b.getFrom());
		
		if(minUpper < maxLower)
			//overlap from lower to upper
			return (maxLower - minUpper)/(maxUpper - minLower);
		else 
			return 0; // no overlap
	}

	private double getCategoricalSimilarity(Categorical a, Categorical b) {
		if(a.getValue().equals(b.getValue()))
			return 1.0;
		Set<String> synonymsA = knowsSynonymy.getSynonyms(a.getValue());
		Set<String> synonymsB = knowsSynonymy.getSynonyms(a.getValue());
		if(synonymsA.contains(b.getValue()) || synonymsB.contains(a.getValue())) {
			return 1.0;
		}
		return 0;
	}
	
}
