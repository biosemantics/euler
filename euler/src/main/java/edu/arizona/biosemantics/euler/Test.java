package edu.arizona.biosemantics.euler;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException, EulerException {
		Euler euler = new Euler();
		euler.setInputFile("/home/thomas/EulerX/example/abstract4/abstract4.txt");
		euler.setEncoding(Encoding.VRPW);
		String result = euler.run();
		System.out.println("result " + result);
	}

}
