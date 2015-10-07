package com.pulselms.app.framework;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropLoader {
	
	public Properties loadProperties(String fileName) throws Exception {
		Properties prop = new Properties();
		InputStream is = null;
		try {
			is=new FileInputStream(fileName);
			prop.load(is);
		} catch (IOException e) {
			throw new Exception("Unable to load Property File " + fileName,e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new Exception("Unable to close Property File " + fileName,e);
				}
			}
		}
		return prop;
	}

}
