package edu.arizona.biosemantics.euler2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.python.core.Py;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.euler2.Reasoner;
import edu.arizona.biosemantics.euler2.Encoding;

public class EulerAlign {
	
	public static void main(String[] args) throws IOException, EulerException {
		EulerAlign align = new EulerAlign();
		align.setInputFile("abstract.txt");
		align.run();
	}
	
	private String inputFile;
	private String outputDirectory;
	private File workingDir;
	private Encoding encoding;
	private Reasoner reasoner;
	private boolean consistency;
	private boolean hidemirdisjoint;
	private boolean disablecov;
	private boolean disablesib;
	private String repairWay;
	private boolean artRem;
	private boolean fourinone;
	private boolean xia;
	private boolean ur;
	private boolean ie;
	private boolean ieo;
	
	public EulerAlign() { }

	public EulerAlign(String inputFile, String outputDirectory, Encoding encoding, Reasoner reasoner,
			boolean consistency, boolean hidemirdisjoint, boolean disablecov,
			boolean disablesib, String repairWay, boolean artRem,
			boolean fourinone, boolean xia, boolean ur, boolean ie, boolean ieo) {
		this.inputFile = inputFile;
		this.outputDirectory = outputDirectory;
		this.encoding = encoding;
		this.reasoner = reasoner;
		this.consistency = consistency;
		this.hidemirdisjoint = hidemirdisjoint;
		this.disablecov = disablecov;
		this.disablesib = disablesib;
		this.repairWay = repairWay;
		this.artRem = artRem;
		this.fourinone = fourinone;
		this.xia = xia;
		this.ur = ur;
		this.ie = ie;
		this.ieo = ieo;
	}
	
	public String run() throws EulerException {
		log(LogLevel.INFO, "Run euler2 align");
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
			if(encoding != null) {
				addCommand(commands, "-e");
				addCommand(commands, encoding.toString().toLowerCase());
			}
			if(reasoner != null) {
				addCommand(commands, "-r");
				addCommand(commands, reasoner.toString().toLowerCase());
			}
			if (consistency)
				addCommand(commands, "--consistency");
			if (hidemirdisjoint)
				addCommand(commands, "--hidemirdisjoint");
			if (disablecov)
				addCommand(commands, "--disablecov");
			if (disablesib)
				addCommand(commands, "--disablesib");
			if (repairWay != null)
				addCommand(commands, "--repair=" + repairWay);
			if (artRem)
				addCommand(commands, "--artRem");
			if (fourinone)
				addCommand(commands, "--fourinone");
			if (xia)
				addCommand(commands, "--xia");
			if (ur)
				addCommand(commands, "--ur");
			if (ie)
				addCommand(commands, "--ie");
			if (ieo)
				addCommand(commands, "--ieo");
			if(outputDirectory != null) {
				addCommand(commands, "-o");
				addCommand(commands, outputDirectory);
			}
			String joinedCommands = StringUtils.join(commands, ", ");
			//use subprocess.call() or execfile()
			interpreter.exec("import subprocess");
			String pythonCommand = "subprocess.call(['" + Configuration.eulerPath + File.separator + "src-el" + File.separator + "euler2', 'align', " + joinedCommands + "])";
			//interpreter.exec("import sys");
			//String args = "'align'" + ", " + joinedCommands;
			//interpreter.exec("sys.argv = [" + args + "]");
			//interpreter.exec("execfile('" + Configuration.eulerPath + File.separator + "src-el" + File.separator + "euler2')");
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
		log(LogLevel.INFO, "Done running euler2 align");
		return outWriter.toString() + "\n" + errWriter.toString();
	}
	
	private void addCommand(List<String> commands, String command) {
		commands.add("'" + command.trim() + "'");
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public Encoding getEncoding() {
		return encoding;
	}

	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}
	
	public Reasoner getReasoner() {
		return reasoner;
	}

	public void setReasoner(Reasoner reasoner) {
		this.reasoner = reasoner;
	}

	public boolean isConsistency() {
		return consistency;
	}

	public void setConsistency(boolean consistency) {
		this.consistency = consistency;
	}

	public boolean isHidemirdisjoint() {
		return hidemirdisjoint;
	}

	public void setHidemirdisjoint(boolean hidemirdisjoint) {
		this.hidemirdisjoint = hidemirdisjoint;
	}

	public boolean isDisablecov() {
		return disablecov;
	}

	public void setDisablecov(boolean disablecov) {
		this.disablecov = disablecov;
	}

	public boolean isDisablesib() {
		return disablesib;
	}

	public void setDisablesib(boolean disablesib) {
		this.disablesib = disablesib;
	}

	public String getRepairWay() {
		return repairWay;
	}

	public void setRepairWay(String repairWay) {
		this.repairWay = repairWay;
	}

	public boolean isArtRem() {
		return artRem;
	}

	public void setArtRem(boolean artRem) {
		this.artRem = artRem;
	}

	public boolean isFourinone() {
		return fourinone;
	}

	public void setFourinone(boolean fourinone) {
		this.fourinone = fourinone;
	}

	public boolean isXia() {
		return xia;
	}

	public void setXia(boolean xia) {
		this.xia = xia;
	}

	public boolean isUr() {
		return ur;
	}

	public void setUr(boolean ur) {
		this.ur = ur;
	}

	public boolean isIe() {
		return ie;
	}

	public void setIe(boolean ie) {
		this.ie = ie;
	}

	public boolean isIeo() {
		return ieo;
	}

	public void setIeo(boolean ieo) {
		this.ieo = ieo;
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
	
	public void setWorkingDir(String workingDir) {
		this.workingDir = new File(workingDir);
	}
}