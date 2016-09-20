package edu.arizona.biosemantics.euler2;

import java.io.File;
import java.io.IOException;

public class Test2 {
	
	public static void main(String[] args) throws IOException, EulerException {
		Euler2 euler = new Euler2();
		euler.setVersion(true);
		euler.setHelp(true);
		String result = euler.run();
		System.out.println("=================  HELP  =================");
		System.out.println(result);
		
		EulerCheck eulerCheck = new EulerCheck();
		System.out.println("\n=================  CHECK  =================");
		eulerCheck.setInputFile(Configuration.eulerPath + File.separator + "../example/abstract4/abstract4.txt");
		result = eulerCheck.run();
		System.out.println(result);
		
		System.out.println("\n=================  ALIGN  =================");
		EulerAlign eulerAlign = new EulerAlign();
		eulerAlign.setInputFile(Configuration.eulerPath + File.separator + "../example/abstract4/abstract4.txt");
		eulerAlign.setReasoner(Reasoner.GRINGO);
		result = eulerAlign.run();
		System.out.println(result);
		
		System.out.println("\n=================  SHOW IV  =================");
		EulerShow eulerShow = new EulerShow();
		eulerShow.setInputFile(Configuration.eulerPath + File.separator + "../example/abstract4/abstract4.txt");
		eulerShow.setName("iv");
		result = eulerShow.run();
		System.out.println(result);
		
		System.out.println("\n=================  SHOW PW  =================");
		eulerShow.setName("pw");
		result = eulerShow.run();
		System.out.println(result);
		
		System.out.println("\n=================  SHOW SV  =================");
		eulerShow.setName("sv");
		result = eulerShow.run();
		System.out.println(result);
	}

}
