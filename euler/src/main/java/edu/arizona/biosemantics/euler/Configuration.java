package edu.arizona.biosemantics.euler;

import java.util.Properties;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import edu.arizona.biosemantics.common.log.Logger;

public class Configuration {

	private final static Logger logger = Logger.getLogger(Configuration.class);
	
	public static String path;
	private static Properties properties;

	static {		
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			properties = new Properties(); 
			properties.load(loader.getResourceAsStream("edu/arizona/biosemantics/euler/config.properties"));
			
			path = properties.getProperty("path");
		} catch(Exception e) {
			logger.error("Couldn't read configuration", e);
		}
	}
	
	public static String asString() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
			return writer.writeValueAsString(properties);
		} catch (Exception e) {
			logger.error("Couldn't write configuration as string", e);
			return null;
		}
	}
	
}
