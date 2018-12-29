package com.analyzer.html.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;


public class PropertyValuesProvider {
	
	private InputStream inputStream;

	private static Logger logger = Logger.getLogger(PropertyValuesProvider.class);
	
	public Properties getPropertiesValues(String fileProperties) throws IOException {
		
		logger.info("Obtiendo valores de: "+ fileProperties);

		Properties prop = new Properties();

		try {

			String propFileName = fileProperties;

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				return null;
			}

		} catch (Exception e) {	
			return null;
		} finally {
			if(inputStream != null) {
				inputStream.close();
			}
		}
		return prop;
	}
}