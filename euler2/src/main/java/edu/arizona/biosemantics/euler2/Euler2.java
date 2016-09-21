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

public class Euler2 {

	private boolean version;
	private boolean help;

	public Euler2() {

	}

	public Euler2(boolean version, boolean help) {
		super();
		this.version = version;
		this.help = help;
	}

	public String run() throws EulerException {
		log(LogLevel.INFO, "Run euler2");
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
			if (version)
				addCommand(commands, "--version");
			if (help)
				addCommand(commands, "--help");
			
			String joinedCommands = StringUtils.join(commands, ", ");
			interpreter.exec("import subprocess");
			String pythonCommand = "subprocess.call(['" + Configuration.eulerPath + File.separator + "src-el" + File.separator + "euler2', " + joinedCommands + "])";
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
		log(LogLevel.INFO, "Done running euler2");
		return outWriter.toString() + "\n" + errWriter.toString();
	}
	
	private void addCommand(List<String> commands, String command) {
		commands.add("'" + command.trim() + "'");
	}

	public boolean isVersion() {
		return version;
	}

	public void setVersion(boolean version) {
		this.version = version;
	}

	public boolean isHelp() {
		return help;
	}

	public void setHelp(boolean help) {
		this.help = help;
	}
}