package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.KnowsModifier;

public class ModifiedStateReader implements StateReader {

	private KnowsModifier knowsModifier;
	private LikelyStateReader likelyStateReader;

	public ModifiedStateReader(LikelyStateReader likelyStateReader, KnowsModifier knowsModifier) {
		this.likelyStateReader = likelyStateReader;
		this.knowsModifier = knowsModifier;
	}
	
	public ModifiedStateReader(KnowsModifier knowsModifier) {
		this.knowsModifier = knowsModifier;
	}
	
	@Override
	public State read(String value) {
		Pattern p = Pattern.compile("^(.*?)\\s+(.*)$");
		Matcher m = p.matcher(value);
		if(m.matches()) {
			String modifierCandidate = m.group(1);
			if(knowsModifier.isModifier(modifierCandidate)) {
				if(likelyStateReader != null) {
					State state = likelyStateReader.read(m.group(2));
					if(state != null) 
						return new ModifiedState(modifierCandidate, state);
				}
			}
		}
		return null;	
	}

	public void setLikelyStateReader(LikelyStateReader likelyStateReader) {
		this.likelyStateReader = likelyStateReader;
	}
}
