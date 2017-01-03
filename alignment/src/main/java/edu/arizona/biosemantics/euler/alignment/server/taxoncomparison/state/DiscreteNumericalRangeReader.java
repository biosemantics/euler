package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscreteNumericalRangeReader implements StateReader {

	@Override
	public State read(String value) {
		Pattern rangePattern = Pattern.compile("^(.*)(\\d+)\\s*-\\s*(\\d+)(.*)$");
		Matcher m = rangePattern.matcher(value);
		if(m.matches()) {
			return new DiscreteNumericalRange(Integer.valueOf(m.group(2)), Integer.valueOf(m.group(3)), m.group(1) + m.group(4));
		}
		Pattern singleValuePattern = Pattern.compile("^(.*)(\\d+)(.*)$");
		m = singleValuePattern.matcher(value);
		if(m.matches()) {
			return new DiscreteNumericalRange(Integer.valueOf(m.group(2)), Integer.valueOf(m.group(2)), m.group(1) + m.group(3));
		}
		return null;
	}
	
	public static void main(String[] args) {
		String test = "abc 3  - 6 foliate";
		DiscreteNumericalRangeReader d = new DiscreteNumericalRangeReader();
		State s = d.read(test);
		System.out.println(s);
		
		test = "abc 3 foliate";
		s = d.read(test);
		System.out.println(s);
	}

}
