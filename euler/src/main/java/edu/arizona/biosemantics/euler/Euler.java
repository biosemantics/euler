package edu.arizona.biosemantics.euler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.arizona.biosemantics.common.log.LogLevel;

public class Euler {

	private boolean version;
	private boolean help;
	private boolean verboseMode;
	private String projectName;
	private String inputFile;
	private String inputDir;
	private String workingDir;
	private String outputDir;
	private Encoding encoding;
	private String dl;
	private Reasoner reasoner;
	private Repair repair;
	private boolean generateAllRelations;
	private boolean consistencyCheckOnly;
	private boolean disableSiblingDisjointnessGlobally;
	private boolean disableSiblingDisjointnessPartially;
	private boolean disableCoverage;
	private boolean uncertaintyReduction;
	private boolean hideConceptsInvolvedInOverlapping;
	private boolean inconsitencyExplanation;
	private boolean whiteBoxProvenance;
	private boolean countMir;
	private boolean reducedContainmentGraphsWithOverlaps;
	private boolean outputPossibleWorldDistances;
	private boolean simplifyPossibleWorldCluster;
	private boolean hierarchicalAggregateView;
	private boolean noOutput;
	private boolean artificialExampleGeneration;
	private Integer artificalExampleGenerationNary;
	private Integer artificalExampleGenerationNodeCount;
	private Integer artificalExampleGenerationDepth;
	private String artificalExampleGenerationRelation;
	private boolean artificalExampleGenerationInconsistent;
	private boolean inputVisualization;
	private boolean simplifyPwAggregateView;
	private boolean articulationRemover;
	private boolean extractInputArticulations;
	private String diagnoticLatticeForInconsistentExample;
	private String addId;
	private String addIsa;
	private String p2c;
	private String p2ct;
	private String addArt;
	private String c2csv;
	private String addArtT;
	private String addRank;
	private String mirStats;
	
	public Euler() {
		
	}
	
	public Euler(String inputFile, Encoding encoding) {
		super();
		this.inputFile = inputFile;
		this.encoding = encoding;
	}

	public Euler(boolean version, boolean help, 
			boolean verboseMode, String projectName, String inputFile,
			String inputDir, String workingDir, String outputDir, Encoding encoding, String dl,
			Reasoner reasoner, Repair repair, boolean generateAllRelations,
			boolean consistencyCheckOnly,
			boolean disableSiblingDisjointnessGlobally,
			boolean disableSiblingDisjointnessPartially,
			boolean disableCoverage, boolean uncertaintyReduction,
			boolean hideConceptsInvolvedInOverlapping,
			boolean inconsitencyExplanation, boolean whiteBoxProvenance,
			boolean countMir, boolean reducedContainmentGraphsWithOverlaps,
			boolean outputPossibleWorldDistances,
			boolean simplifyPossibleWorldCluster,
			boolean hierarchicalAggregateView, boolean noOutput,
			boolean artificialExampleGeneration,
			Integer artificalExampleGenerationNary,
			Integer artificalExampleGenerationNodeCount,
			Integer artificalExampleGenerationDepth,
			String artificalExampleGenerationRelation,
			boolean artificalExampleGenerationInconsistent,
			boolean inputVisualization, boolean simplifyPwAggregateView,
			boolean articulationRemover, boolean extractInputArticulations,
			String diagnoticLatticeForInconsistentExample, String addId,
			String addIsa, String p2c, String p2ct, String addArt,
			String c2csv, String addArtT, String addRank, String mirStats) {
		super();
		this.version = version;
		this.help = help;
		this.verboseMode = verboseMode;
		this.projectName = projectName;
		this.inputFile = inputFile;
		this.inputDir = inputDir;
		this.workingDir = workingDir;
		this.outputDir = outputDir;
		this.encoding = encoding;
		this.dl = dl;
		this.reasoner = reasoner;
		this.repair = repair;
		this.generateAllRelations = generateAllRelations;
		this.consistencyCheckOnly = consistencyCheckOnly;
		this.disableSiblingDisjointnessGlobally = disableSiblingDisjointnessGlobally;
		this.disableSiblingDisjointnessPartially = disableSiblingDisjointnessPartially;
		this.disableCoverage = disableCoverage;
		this.uncertaintyReduction = uncertaintyReduction;
		this.hideConceptsInvolvedInOverlapping = hideConceptsInvolvedInOverlapping;
		this.inconsitencyExplanation = inconsitencyExplanation;
		this.whiteBoxProvenance = whiteBoxProvenance;
		this.countMir = countMir;
		this.reducedContainmentGraphsWithOverlaps = reducedContainmentGraphsWithOverlaps;
		this.outputPossibleWorldDistances = outputPossibleWorldDistances;
		this.simplifyPossibleWorldCluster = simplifyPossibleWorldCluster;
		this.hierarchicalAggregateView = hierarchicalAggregateView;
		this.noOutput = noOutput;
		this.artificialExampleGeneration = artificialExampleGeneration;
		this.artificalExampleGenerationNary = artificalExampleGenerationNary;
		this.artificalExampleGenerationNodeCount = artificalExampleGenerationNodeCount;
		this.artificalExampleGenerationDepth = artificalExampleGenerationDepth;
		this.artificalExampleGenerationRelation = artificalExampleGenerationRelation;
		this.artificalExampleGenerationInconsistent = artificalExampleGenerationInconsistent;
		this.inputVisualization = inputVisualization;
		this.simplifyPwAggregateView = simplifyPwAggregateView;
		this.articulationRemover = articulationRemover;
		this.extractInputArticulations = extractInputArticulations;
		this.diagnoticLatticeForInconsistentExample = diagnoticLatticeForInconsistentExample;
		this.addId = addId;
		this.addIsa = addIsa;
		this.p2c = p2c;
		this.p2ct = p2ct;
		this.addArt = addArt;
		this.c2csv = c2csv;
		this.addArtT = addArtT;
		this.addRank = addRank;
		this.mirStats = mirStats;
	}
	
	public String run() throws IOException, EulerException {
		//StringBuilder command = new StringBuilder();
		List<String> commands = new LinkedList<String>();
		//commands.add("euler");
		if(version)
			commands.add("--version");
		if(help)
			commands.add("--help");
		if(verboseMode)
			commands.add("-v");
		if(projectName != null)
			commands.add("-p " + projectName);
		if(inputFile != null)
			commands.add("-i " + inputFile);
		if(inputDir != null)
			commands.add("-r " + inputDir);
		if(workingDir != null)
			commands.add("-w " + workingDir);
		if(outputDir != null)
			commands.add("-o " + outputDir);
		if(encoding != null)
			commands.add("-e " + encoding.toString().toLowerCase());
		if(dl != null)
			commands.add("-b " + dl);
		if(reasoner != null)
			commands.add("--reasoner " + reasoner.toString().toLowerCase());
		if(repair != null)
			commands.add("--repair " + repair.toString().toLowerCase());
		if(generateAllRelations)
			commands.add("--mirall");
		if(consistencyCheckOnly)
			commands.add("-cc");
		if(disableSiblingDisjointnessGlobally)
			commands.add("--dd");
		if(disableSiblingDisjointnessPartially)
			commands.add("-pdd " + workingDir);
		if(disableCoverage)
			commands.add("--dc");
		if(uncertaintyReduction)
			commands.add("--ur");
		if(hideConceptsInvolvedInOverlapping)
			commands.add("--ho");
		if(inconsitencyExplanation)
			commands.add("--ie");
		if(whiteBoxProvenance)
			commands.add("--ieo");
		if(countMir)
			commands.add("--countmir");
		if(reducedContainmentGraphsWithOverlaps)
			commands.add("--rcgo");
		if(outputPossibleWorldDistances)
			commands.add("--pwcluster");
		if(simplifyPossibleWorldCluster)
			commands.add("--simpcluster");
		if(hierarchicalAggregateView)
			commands.add("--hierarchy");
		if(noOutput)
			commands.add("-N");
		if(this.artificialExampleGeneration)
			commands.add("-g");
		if(this.artificalExampleGenerationNary != null)
			commands.add("-n " + this.artificalExampleGenerationNary);
		if(this.artificalExampleGenerationNodeCount != null)
			commands.add("-m " + this.artificalExampleGenerationNodeCount);
		if(this.artificalExampleGenerationDepth != null)
			commands.add("-d " + this.artificalExampleGenerationDepth);
		if(this.artificalExampleGenerationRelation != null)
			commands.add("-t " + this.artificalExampleGenerationRelation);
		if(this.artificalExampleGenerationInconsistent)
			commands.add("-I");
		if(this.inputVisualization)
			commands.add("--iv");
		if(this.simplifyPwAggregateView)
			commands.add("--simpall");
		if(this.articulationRemover)
			commands.add("--artRem");
		if(this.extractInputArticulations)
			commands.add("--xia");
		if(this.addId != null)
			commands.add("--addID " + addId);
		if(this.addIsa != null)
			commands.add("--addISA " + addIsa);
		if(this.p2c != null)
			commands.add("--p2c " + p2c);
		if(this.p2ct != null)
			commands.add("--p2ct " + p2ct);
		if(this.addArt != null)
			commands.add("--addArt " + addArt);
		if(this.c2csv != null)
			commands.add("--c2csv " + c2csv);
		if(this.addArtT != null)
			commands.add("--addArtT " + addArtT);
		if(this.addRank != null)
			commands.add("--addRank " + addRank);
		if(this.mirStats != null)
			commands.add("--mirStats " + mirStats);
		
		return runCommand("euler " + StringUtils.join(commands, " "));
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

	public boolean isVerboseMode() {
		return verboseMode;
	}

	public void setVerboseMode(boolean verboseMode) {
		this.verboseMode = verboseMode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public String getInputDir() {
		return inputDir;
	}

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}

	public String getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public Encoding getEncoding() {
		return encoding;
	}

	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}

	public String getDl() {
		return dl;
	}

	public void setDl(String dl) {
		this.dl = dl;
	}

	public Reasoner getReasoner() {
		return reasoner;
	}

	public void setReasoner(Reasoner reasoner) {
		this.reasoner = reasoner;
	}

	public Repair getRepair() {
		return repair;
	}

	public void setRepair(Repair repair) {
		this.repair = repair;
	}

	public boolean isGenerateAllRelations() {
		return generateAllRelations;
	}

	public void setGenerateAllRelations(boolean generateAllRelations) {
		this.generateAllRelations = generateAllRelations;
	}

	public boolean isConsistencyCheckOnly() {
		return consistencyCheckOnly;
	}

	public void setConsistencyCheckOnly(boolean consistencyCheckOnly) {
		this.consistencyCheckOnly = consistencyCheckOnly;
	}

	public boolean isDisableSiblingDisjointnessGlobally() {
		return disableSiblingDisjointnessGlobally;
	}

	public void setDisableSiblingDisjointnessGlobally(
			boolean disableSiblingDisjointnessGlobally) {
		this.disableSiblingDisjointnessGlobally = disableSiblingDisjointnessGlobally;
	}

	public boolean isDisableSiblingDisjointnessPartially() {
		return disableSiblingDisjointnessPartially;
	}

	public void setDisableSiblingDisjointnessPartially(
			boolean disableSiblingDisjointnessPartially) {
		this.disableSiblingDisjointnessPartially = disableSiblingDisjointnessPartially;
	}

	public boolean isDisableCoverage() {
		return disableCoverage;
	}

	public void setDisableCoverage(boolean disableCoverage) {
		this.disableCoverage = disableCoverage;
	}

	public boolean isUncertaintyReduction() {
		return uncertaintyReduction;
	}

	public void setUncertaintyReduction(boolean uncertaintyReduction) {
		this.uncertaintyReduction = uncertaintyReduction;
	}

	public boolean isHideConceptsInvolvedInOverlapping() {
		return hideConceptsInvolvedInOverlapping;
	}

	public void setHideConceptsInvolvedInOverlapping(
			boolean hideConceptsInvolvedInOverlapping) {
		this.hideConceptsInvolvedInOverlapping = hideConceptsInvolvedInOverlapping;
	}

	public boolean isInconsitencyExplanation() {
		return inconsitencyExplanation;
	}

	public void setInconsitencyExplanation(boolean inconsitencyExplanation) {
		this.inconsitencyExplanation = inconsitencyExplanation;
	}

	public boolean isWhiteBoxProvenance() {
		return whiteBoxProvenance;
	}

	public void setWhiteBoxProvenance(boolean whiteBoxProvenance) {
		this.whiteBoxProvenance = whiteBoxProvenance;
	}

	public boolean isCountMir() {
		return countMir;
	}

	public void setCountMir(boolean countMir) {
		this.countMir = countMir;
	}

	public boolean isReducedContainmentGraphsWithOverlaps() {
		return reducedContainmentGraphsWithOverlaps;
	}

	public void setReducedContainmentGraphsWithOverlaps(
			boolean reducedContainmentGraphsWithOverlaps) {
		this.reducedContainmentGraphsWithOverlaps = reducedContainmentGraphsWithOverlaps;
	}

	public boolean isOutputPossibleWorldDistances() {
		return outputPossibleWorldDistances;
	}

	public void setOutputPossibleWorldDistances(boolean outputPossibleWorldDistances) {
		this.outputPossibleWorldDistances = outputPossibleWorldDistances;
	}

	public boolean isSimplifyPossibleWorldCluster() {
		return simplifyPossibleWorldCluster;
	}

	public void setSimplifyPossibleWorldCluster(boolean simplifyPossibleWorldCluster) {
		this.simplifyPossibleWorldCluster = simplifyPossibleWorldCluster;
	}

	public boolean isHierarchicalAggregateView() {
		return hierarchicalAggregateView;
	}

	public void setHierarchicalAggregateView(boolean hierarchicalAggregateView) {
		this.hierarchicalAggregateView = hierarchicalAggregateView;
	}

	public boolean isNoOutput() {
		return noOutput;
	}

	public void setNoOutput(boolean noOutput) {
		this.noOutput = noOutput;
	}

	public boolean isArtificialExampleGeneration() {
		return artificialExampleGeneration;
	}

	public void setArtificialExampleGeneration(boolean artificialExampleGeneration) {
		this.artificialExampleGeneration = artificialExampleGeneration;
	}

	public Integer getArtificalExampleGenerationNary() {
		return artificalExampleGenerationNary;
	}

	public void setArtificalExampleGenerationNary(Integer artificalExampleGenerationNary) {
		this.artificalExampleGenerationNary = artificalExampleGenerationNary;
	}

	public Integer getArtificalExampleGenerationNodeCount() {
		return artificalExampleGenerationNodeCount;
	}

	public void setArtificalExampleGenerationNodeCount(
			Integer artificalExampleGenerationNodeCount) {
		this.artificalExampleGenerationNodeCount = artificalExampleGenerationNodeCount;
	}

	public Integer getArtificalExampleGenerationDepth() {
		return artificalExampleGenerationDepth;
	}

	public void setArtificalExampleGenerationDepth(
			Integer artificalExampleGenerationDepth) {
		this.artificalExampleGenerationDepth = artificalExampleGenerationDepth;
	}

	public String getArtificalExampleGenerationRelation() {
		return artificalExampleGenerationRelation;
	}

	public void setArtificalExampleGenerationRelation(
			String artificalExampleGenerationRelation) {
		this.artificalExampleGenerationRelation = artificalExampleGenerationRelation;
	}

	public boolean isArtificalExampleGenerationInconsistent() {
		return artificalExampleGenerationInconsistent;
	}

	public void setArtificalExampleGenerationInconsistent(
			boolean artificalExampleGenerationInconsistent) {
		this.artificalExampleGenerationInconsistent = artificalExampleGenerationInconsistent;
	}

	public boolean isInputVisualization() {
		return inputVisualization;
	}

	public void setInputVisualization(boolean inputVisualization) {
		this.inputVisualization = inputVisualization;
	}

	public boolean isSimplifyPwAggregateView() {
		return simplifyPwAggregateView;
	}

	public void setSimplifyPwAggregateView(boolean simplifyPwAggregateView) {
		this.simplifyPwAggregateView = simplifyPwAggregateView;
	}

	public boolean isArticulationRemover() {
		return articulationRemover;
	}

	public void setArticulationRemover(boolean articulationRemover) {
		this.articulationRemover = articulationRemover;
	}

	public boolean isExtractInputArticulations() {
		return extractInputArticulations;
	}

	public void setExtractInputArticulations(boolean extractInputArticulations) {
		this.extractInputArticulations = extractInputArticulations;
	}

	public String getDiagnoticLatticeForInconsistentExample() {
		return diagnoticLatticeForInconsistentExample;
	}

	public void setDiagnoticLatticeForInconsistentExample(
			String diagnoticLatticeForInconsistentExample) {
		this.diagnoticLatticeForInconsistentExample = diagnoticLatticeForInconsistentExample;
	}

	public String getAddId() {
		return addId;
	}

	public void setAddId(String addId) {
		this.addId = addId;
	}

	public String getAddIsa() {
		return addIsa;
	}

	public void setAddIsa(String addIsa) {
		this.addIsa = addIsa;
	}

	public String getP2c() {
		return p2c;
	}

	public void setP2c(String p2c) {
		this.p2c = p2c;
	}

	public String getP2ct() {
		return p2ct;
	}

	public void setP2ct(String p2ct) {
		this.p2ct = p2ct;
	}

	public String getAddArt() {
		return addArt;
	}

	public void setAddArt(String addArt) {
		this.addArt = addArt;
	}

	public String getC2csv() {
		return c2csv;
	}

	public void setC2csv(String c2csv) {
		this.c2csv = c2csv;
	}

	public String getAddArtT() {
		return addArtT;
	}

	public void setAddArtT(String addArtT) {
		this.addArtT = addArtT;
	}

	public String getAddRank() {
		return addRank;
	}

	public void setAddRank(String addRank) {
		this.addRank = addRank;
	}

	public String getMirStats() {
		return mirStats;
	}

	public void setMirStats(String mirStats) {
		this.mirStats = mirStats;
	}

	private String runCommand(String command) throws EulerException {
		StringBuilder input = new StringBuilder();
		StringBuilder error = new StringBuilder();
		long time = System.currentTimeMillis();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			
			try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()))) {
				String s = "";
				int i = 0;
				while ((s = stdInput.readLine()) != null) {
					log(LogLevel.DEBUG, s + " at "
							+ (System.currentTimeMillis() - time) / 1000 + " seconds");
					input.append(s + "\n");
				}
			}
			
			try (BufferedReader errInput = new BufferedReader(new InputStreamReader(
						p.getErrorStream()))) {
				String e = "";
				while ((e = errInput.readLine()) != null) {
					log(LogLevel.DEBUG, e + " at "
							+ (System.currentTimeMillis() - time) / 1000 + " seconds");
					error.append(e + "\n");
				}
			}
			
			int exitStatus = p.waitFor();
			if(exitStatus == 0)
				return input.toString() + "\n" + error.toString();
		} catch (IOException | InterruptedException e) {
			log(LogLevel.ERROR, "Couldn't execute euler", e);
			throw new EulerException("Euler exeuction failed: \n" + error.toString());
		}
		throw new EulerException("Euler exeuction failed: \n" + error.toString());
	}
	
}
