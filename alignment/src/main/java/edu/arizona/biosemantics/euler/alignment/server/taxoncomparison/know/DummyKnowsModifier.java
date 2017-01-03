package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know;

import java.util.HashMap;
import java.util.Map;

public class DummyKnowsModifier implements KnowsModifier {
	
	private Map<String, Double> modifierWeights = new HashMap<String, Double>();

	public DummyKnowsModifier() {
		modifierWeights .put("always", 1.0);
		modifierWeights.put("usually", 1.0);
		modifierWeights.put("generally", 1.0);
		modifierWeights.put("often", 1.0);
		modifierWeights.put("sometimes", 1.0);
		modifierWeights.put("less frequently", 1.0);
		modifierWeights.put("not", 1.0);
		modifierWeights.put("never", 1.0);
	}
	
	@Override
	public boolean isModifier(String candidate) {
		return modifierWeights.containsKey(candidate);
	}

	@Override
	public double getWeight(String modifier) {
		if(!isModifier(modifier))
			return -1;
		return modifierWeights.get(modifier);
	}

}
