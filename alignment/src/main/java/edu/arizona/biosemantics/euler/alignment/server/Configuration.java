package edu.arizona.biosemantics.euler.alignment.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import edu.arizona.biosemantics.common.log.Logger;

public class Configuration {

	private final static Logger logger = Logger.getLogger(Configuration.class);
	
	/** Database **/
	public static String databaseName;
	public static String databaseUser;
	public static String databasePassword;
	public static String databaseHost;
	public static String databasePort;
	public static int database_minConnectionsPerPartition;
	public static int database_maxConnectionsPerPartition;
	public static int database_partitionCount;

	public static String wordNetDirectory;
	public static String glossariesDownloadDirectory;
	public static String otoURL;
	
	/** Files **/
	public static String collectionsPath;
	public static double stateWeight;

	private static Properties properties;


	static {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			properties = new Properties(); 
			properties.load(loader.getResourceAsStream("edu/arizona/biosemantics/euler/alignment/config.properties"));
			
			databaseName = properties.getProperty("databaseName");
			databaseUser = properties.getProperty("databaseUser");
			databasePassword = properties.getProperty("databasePassword");
			databaseHost = properties.getProperty("databaseHost");
			databasePort = properties.getProperty("databasePort");
			database_minConnectionsPerPartition = Integer.valueOf(properties.getProperty("database_minConnectionsPerPartition"));
			database_maxConnectionsPerPartition = Integer.valueOf(properties.getProperty("database_maxConnectionsPerPartition"));
			database_partitionCount = Integer.valueOf(properties.getProperty("database_partitionCount"));

			wordNetDirectory = properties.getProperty("wordnet_directory");
			glossariesDownloadDirectory = properties.getProperty("glossariesDownloadDirectory");
			collectionsPath = properties.getProperty("collections_path");
			otoURL = properties.getProperty("oto_url");
			
			collectionsPath = properties.getProperty("collectionsPath");
			stateWeight = Double.valueOf(properties.getProperty("stateWeight"));
		} catch(Exception e) {
			logger.error("Couldn't read configuration", e);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		/*try(CSVReader reader = new CSVReader(new FileReader(new File("src/main/resources/defaultCategories.csv")))) {
			List<String[]> lines = reader.readAll();
			List<String[]> newLines = new ArrayList<String[]>();
			for(String[] line : lines) {
				List<String> newLineList = new ArrayList<String>(Arrays.asList(line));
				if(line[0].startsWith("structure") || line[0].equals("substance") || line[0].equals("taxon_name")) {
					newLineList.add("y");
				} else
					newLineList.add("n");
				newLines.add(newLineList.toArray(new String[newLineList.size()]));
			}
			try(CSVWriter writer = new CSVWriter(new FileWriter(new File("src/main/resources/defCategories.csv")))) {
				writer.writeAll(newLines);
			}
		}*/
	}
	
	public static String asString() {
		try {
			ObjectMapper mapper  = new ObjectMapper();
			ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
			return writer.writeValueAsString(properties);
		} catch (Exception e) {
			//log(LogLevel.ERROR, "Problem writing object as String", e);
			return null;
		}
	}
}