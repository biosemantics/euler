package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state;

public class CategoricalReader implements StateReader {

	@Override
	public Categorical read(String value) {
		return new Categorical(value);
	}

}
