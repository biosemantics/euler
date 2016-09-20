package edu.arizona.biosemantics.euler2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.python.core.Py;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import edu.arizona.biosemantics.common.log.LogLevel;

public class EulerShow {

	private String name;
	private String inputFile;
	private String outputDirectory;
	private String folder;
	
	private File workingDir;
	
	public EulerShow() {

	}

	public EulerShow(String name, String inputFile, String outputDirectory, String folder) {
		this.name = name;
		this.inputFile = inputFile;
		this.outputDirectory = outputDirectory;
		this.folder = folder;
	}

	public String run() throws EulerException {
		log(LogLevel.INFO, "Run euler2 show");
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
			if (folder != null)
				addCommand(commands, "--run=" + folder);
			if(inputFile != null)
				addCommand(commands, inputFile);
			if (name != null)
				addCommand(commands, name);
			if(outputDirectory != null)
				addCommand(commands, "-o " + outputDirectory);
			
			String joinedCommands = StringUtils.join(commands, ", ");
			interpreter.exec("import subprocess");
			String pythonCommand = "subprocess.call(['" + Configuration.eulerPath + File.separator + "src-el" + File.separator + "euler2', 'show', " + joinedCommands + "])";
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
		log(LogLevel.INFO, "Done running euler2 show");
		return outWriter.toString() + "\n" + errWriter.toString();	
	}
	
	private void addCommand(List<String> commands, String command) {
		commands.add("'" + command + "'");
	}
	
	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public File getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(File workingDir) {
		this.workingDir = workingDir;
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public String getFolder(){
		return folder;
	}
	
	public void setFolder(String folder){
		this.folder = folder;
	}
	
	public void setWorkingDir(String workingDir){
		this.workingDir = new File(workingDir);
	}
	
}