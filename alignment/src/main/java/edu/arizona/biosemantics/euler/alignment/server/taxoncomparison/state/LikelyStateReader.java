package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state;

public class LikelyStateReader implements StateReader {

	private CategoricalReader categoricalReader;
	private DiscreteNumericalRangeReader discreteNumericalRangeReader;
	private ModifiedStateReader modifiedStateReader;
	private NumericalRangeReader numericalRangeReader;

	public LikelyStateReader(CategoricalReader categoricalReader, 
			DiscreteNumericalRangeReader discreteNumericalRangeReader, 
			ModifiedStateReader modifiedStateReader, 
			NumericalRangeReader numericalRangeReader) {
		this.categoricalReader = categoricalReader;
		this.discreteNumericalRangeReader = discreteNumericalRangeReader;
		this.modifiedStateReader = modifiedStateReader;
		this.numericalRangeReader = numericalRangeReader;
	}
	
	@Override
	public State read(String value) {		
		State state = null;
		for(StateReader reader : new StateReader[] { 
				modifiedStateReader,
				numericalRangeReader,
				discreteNumericalRangeReader,
				categoricalReader,}) {
			state = reader.read(value);
			if(state != null) {
				break;	
			}
		}
		return state;
	}

}
