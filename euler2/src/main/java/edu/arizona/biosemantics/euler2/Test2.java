package edu.arizona.biosemantics.euler2;

import java.io.File;
import java.io.IOException;

public class Test2 {

	/*##### 1. Show the available commands
List all usage commands.

    $ euler2 -h

##### 2. Check the input file
Check the validity of input file, including “multiple roots per taxonomy”, “multiple$

    $ euler2 check abstract4.txt

##### 3. Run taxonomic alignment problem
The main task for Euler/X, you can suggest the encoding method, the reasoning tool, $

    $ euler2 align abstract4.txt

##### 4. Show the input visualization
TO show the input visualization of the last run (the same as the following **show** $

    $ euler2 show iv, pw, av, cv, hv

##### 5. Show the possible worlds
The result will be in `4-PWs/` folder

    $ euler2 show pw

##### 6. Show the aggregate view
The result will be in `5-Aggregates/` folder

    $ euler2 show av
    
##### 7. Show the cluster view
The result will be in `5-Aggregates/` folder

    $ euler2 show cv

##### 8. Show the hierarchy view
The result will be in `5-Aggregates/` folder

    $ euler2 show hv

*/
	
	
	
	public static void main(String[] args) throws IOException, EulerException {
		System.out.println(System.getenv("PATH"));
		
		Euler2 euler = new Euler2();
		euler.setVersion(true);
		euler.setHelp(true);
		String result = euler.run();
		System.out.println("=================  HELP  =================");
		System.out.println(result);
		
		EulerCheck eulerCheck = new EulerCheck();
		System.out.println("\n=================  CHECK  =================");
		eulerCheck.setInputFile(Configuration.path + File.separator + "../example/abstract4/abstract4.txt");
		result = eulerCheck.run();
		System.out.println(result);
		
		System.out.println("\n=================  ALIGN  =================");
		EulerAlign eulerAlign = new EulerAlign();
		eulerAlign.setInputFile(Configuration.path + File.separator + "../example/abstract4/abstract4.txt");
		eulerAlign.setReasoner(Reasoner.GRINGO);
		result = eulerAlign.run();
		System.out.println(result);
		
		System.out.println("\n=================  SHOW IV  =================");
		EulerShow eulerShow = new EulerShow();
		eulerShow.setInputFile(Configuration.path + File.separator + "../example/abstract4/abstract4.txt");
		eulerShow.setName("iv");
		result = eulerShow.run();
		System.out.println(result);
		
		/*System.out.println("\n=================  SHOW PW  =================");
		eulerShow.setName("pw");
		result = eulerShow.run();
		System.out.println(result);
		
		System.out.println("\n=================  SHOW SV  =================");
		eulerShow.setName("sv");
		result = eulerShow.run();
		System.out.println(result);*/
		
		
	}

}
