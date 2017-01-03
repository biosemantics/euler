package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state;

import edu.arizona.biosemantics.common.log.LogLevel;

public class NumericalRange implements State {
	
	private double to;
	private double from;
	private String additional;

	public NumericalRange(double from, double to, String additional) {
		this.from = from;
		this.to = to;
		this.additional = additional;
	}

	public double getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public double getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}

	@Override
	public Type getType() {
		return Type.NUMERICAL_RANGE;
	}


}
