package edu.arizona.biosemantics.euler2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.python.core.Py;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import edu.arizona.biosemantics.common.log.LogLevel;

public class EulerCheck {

	private String inputFile;
	
	public EulerCheck() {

	}

	public EulerCheck(String inputFile) {
		this.inputFile = inputFile;
	}

	public String run() throws EulerException {
		log(LogLevel.INFO, "Run euler2 check");
		StringWriter outWriter = new StringWriter();
		StringWriter errWriter = new StringWriter();
		PythonInterpreter.initialize(System.getProperties(), System.getProperties(), new String[0]);
		try(PythonInterpreter interpreter = new PythonInterpreter()) {
			interpreter.setOut(outWriter);
			interpreter.setErr(outWriter);
			PySystemState sys = Py.getSystemState();
			sys.path.append(new PyString(Configuration.pythonModulesPath));
			sys.path.append(new PyString(Configuration.eulerPath));
			
			List<String> commands = new LinkedList<String>();
			if(inputFile != null)
				addCommand(commands, inputFile);
			
			String joinedCommands = StringUtils.join(commands, ", ");
			interpreter.exec("import subprocess");
			String pythonCommand = "subprocess.call(['" + Configuration.eulerPath + File.separator + "src-el" + File.separator + "euler2', 'check', " + joinedCommands + "])";
			log(LogLevel.INFO, "Run: " + pythonCommand);
			interpreter.exec(pythonCommand);
			interpreter.cleanup();
			interpreter.close();
		}
		
		String outString = outWriter.toString().trim();
		String errorString = errWriter.toString().trim();
		if(!outString.isEmpty())
			log(LogLevel.INFO, outWriter.toString());
		if(!errorString.isEmpty()) {
			log(LogLevel.ERROR, errWriter.toString());
			throw new EulerException(errorString);
		}
		log(LogLevel.INFO, "Done running euler2 check");
		return outWriter.toString() + "\n" + errWriter.toString();
	}
	
	private void addCommand(List<String> commands, String command) {
		commands.add("'" + command + "'");
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}


}