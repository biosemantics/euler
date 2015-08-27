package edu.arizona.biosemantics.euler2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.euler2.Reasoner;
import edu.arizona.biosemantics.euler2.Encoding;

public class EulerAlign {

	/*	euler2 align <inputfile>... [-e METHOD] [-r TOOL] 
boolean consistency,
boolean hidemirdisjoint,
boolean disablecov,
boolean disablesib,
String repairWay,
boolean artRem,
boolean fourinone,
boolean xia,
boolean ur,
boolean ie,
boolean ieo
	 */
	
	private String inputFile;
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
	
	public EulerAlign() {

	}

	public EulerAlign(String inputFile, Encoding encoding, Reasoner reasoner,
			boolean consistency, boolean hidemirdisjoint, boolean disablecov,
			boolean disablesib, String repairWay, boolean artRem,
			boolean fourinone, boolean xia, boolean ur, boolean ie, boolean ieo) {
		this.inputFile = inputFile;
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
/*euler2 align <inputfile>... [-e METHOD] [-r TOOL] [--consistency] [--hidemirdisjoint] 
 * [--disablecov] [--disablesib] [--repair=WAY] [--artRem] [--fourinone] [--xia] [--ur] 
 * [--ie] [--ieo]
*/
	public String run() throws IOException, EulerException {
		List<String> commands = new LinkedList<String>();
		if(inputFile != null)
			commands.add(inputFile);
		if(encoding != null)
			commands.add("-e " + encoding.toString().toLowerCase());
		if(reasoner != null)
			commands.add("-r " + reasoner.toString().toLowerCase());
		if (consistency)
			commands.add("--consistency");
		if (hidemirdisjoint)
			commands.add("--hidemirdisjoint");
		if (disablecov)
			commands.add("--disablecov");
		if (disablesib)
			commands.add("--disablesib");
		if (repairWay != null)
			commands.add("--repair=" + repairWay);
		if (artRem)
			commands.add("--artRem");
		if (fourinone)
			commands.add("--fourinone");
		if (xia)
			commands.add("--xia");
		if (ur)
			commands.add("--ur");
		if (ie)
			commands.add("--ie");
		if (ieo)
			commands.add("--ieo");

		return runCommand(Configuration.path + File.separator + "euler2 align "
				+ StringUtils.join(commands, " "));
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