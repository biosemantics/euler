package edu.arizona.biosemantics.euler2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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

	public String run() throws IOException, EulerException {
		List<String> commands = new LinkedList<String>();
		if (folder != null)
			commands.add("--run=" + folder);
		if(inputFile != null)
			commands.add(inputFile);
		if (name != null)
			commands.add(name);
		if(outputDirectory != null)
			commands.add("-o " + outputDirectory);
		
		return runCommand(Configuration.path + File.separator + "euler2 show "
				+ StringUtils.join(commands, " "));
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
	
	private class TerminatePythonHook extends Thread {
		final Process process;
		
		public TerminatePythonHook(Process process){
			this.process = process;
		}
		
		@Override
		public void run() {
			try {
				process.getInputStream().close();
				process.getOutputStream().close();
				process.getErrorStream().close();

				Field field = process.getClass().getDeclaredField("pid");
				field.setAccessible(true);
				int pid = field.getInt(process);
				Runtime.getRuntime().exec("kill -9 " + pid);

			} catch (Throwable t) {
				log(LogLevel.ERROR,
						"Could not kill perl process. Running on non-Unix OS?",
						t);
			}
			process.destroy();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				log(LogLevel.ERROR, "Interrupted", e);
			}
		}
	}

	private String runCommand(String command) throws EulerException {
		log(LogLevel.DEBUG, "Run: " + command);
		StringBuilder input = new StringBuilder();
		StringBuilder error = new StringBuilder();
		long time = System.currentTimeMillis();
		Process process;
		try {
			process = Runtime.getRuntime().exec(command, null, workingDir);
			
			TerminatePythonHook terminatePythonHook = new TerminatePythonHook(process);
			Runtime.getRuntime().addShutdownHook(terminatePythonHook);
			
			
			try (BufferedReader stdInput = new BufferedReader(
					new InputStreamReader(process.getInputStream()))) {
				String s = "";
				while ((s = stdInput.readLine()) != null) {
					log(LogLevel.DEBUG,
							s + " at " + (System.currentTimeMillis() - time)
									/ 1000 + " seconds");
					input.append(s + "\n");
				}
			}

			try (BufferedReader errInput = new BufferedReader(
					new InputStreamReader(process.getErrorStream()))) {
				String e = "";
				while ((e = errInput.readLine()) != null) {
					log(LogLevel.DEBUG,
							e + " at " + (System.currentTimeMillis() - time)
									/ 1000 + " seconds");
					error.append(e + "\n");
				}
			}

			int exitStatus = process.waitFor();
			
			//remove shutdown hook
			Runtime.getRuntime().removeShutdownHook(terminatePythonHook);

			if (exitStatus == 0)
				return input.toString() + "\n" + error.toString();
			else
				log(LogLevel.ERROR, "Euler exit status " + exitStatus);
		} catch (IOException | InterruptedException e) {
			log(LogLevel.ERROR, "Couldn't execute euler", e);
			throw new EulerException("Euler execution failed: \n"
					+ error.toString() + "... " + e.toString());
		}
		throw new EulerException("Euler execution failed: \n"
				+ error.toString());
	}
}