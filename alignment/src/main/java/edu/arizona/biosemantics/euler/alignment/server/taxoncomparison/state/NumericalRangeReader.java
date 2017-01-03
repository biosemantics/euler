package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumericalRangeReader implements StateReader {

	private static String units = "(?:(?:pm|cm|mm|dm|ft|m|meters|meter|micro_m|micro-m|microns|micron|unes|µm|μm|um|centimeters|"
			+ "centimeter|millimeters|millimeter|transdiameters|transdiameter)[23]?)"; //squared or cubed
	
	//ignore unit conversion for now
	@Override
	public State read(String value) {
		Pattern p = Pattern.compile("^(.*?)(\\d?\\.\\d+)\\s*" + units + "?-\\s*(\\d?\\.\\d+)" + units + "?(.*)$");
		Matcher m = p.matcher(value);
		if(m.matches()) {
			return new NumericalRange(Double.valueOf(m.group(2)), Double.valueOf(m.group(3)), m.group(1) + m.group(4));
		}
		Pattern singleValuePattern = Pattern.compile("^(.*?)(\\d?\\.\\d+)" + units + "?(.*)$");
		m = singleValuePattern.matcher(value);
		if(m.matches()) {
			return new NumericalRange(Double.valueOf(m.group(2)), Double.valueOf(m.group(2)), m.group(1) + m.group(3));
		}
		return null;
	}
	
	public static void main(String[] args) {
		String test = "abc 0.25  - 0.3 foliate";
		NumericalRangeReader d = new NumericalRangeReader();
		State s = d.read(test);
		System.out.println(s);
		
		String test2 = "abc .3 - .6 foliate";
		State s2 = d.read(test2);
		System.out.println(s2);
		
		String test3 = "abc .4 aoe";
		State s3 = d.read(test3);
		System.out.println(s3);
		
	}

}
