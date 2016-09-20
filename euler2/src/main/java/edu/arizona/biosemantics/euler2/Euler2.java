package edu.arizona.biosemantics.euler2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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

	public String run() throws IOException, EulerException {
		List<String> commands = new LinkedList<String>();
		if (version)
			commands.add("--version");
		if (help)
			commands.add("--help");

		return runCommand(Configuration.eulerPath + File.separator + "euler2 "
				+ StringUtils.join(commands, " "));
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

	private String runCommand(String command) throws EulerException {
		log(LogLevel.DEBUG, "Run: " + command);
		StringBuilder input = new StringBuilder();
		StringBuilder error = new StringBuilder();
		long time = System.currentTimeMillis();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);

			try (BufferedReader stdInput = new BufferedReader(
					new InputStreamReader(p.getInputStream()))) {
				String s = "";
				while ((s = stdInput.readLine()) != null) {
					log(LogLevel.DEBUG,
							s + " at " + (System.currentTimeMillis() - time)
									/ 1000 + " seconds");
					input.append(s + "\n");
				}
			}

			try (BufferedReader errInput = new BufferedReader(
					new InputStreamReader(p.getErrorStream()))) {
				String e = "";
				while ((e = errInput.readLine()) != null) {
					log(LogLevel.DEBUG,
							e + " at " + (System.currentTimeMillis() - time)
									/ 1000 + " seconds");
					error.append(e + "\n");
				}
			}

			int exitStatus = p.waitFor();
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