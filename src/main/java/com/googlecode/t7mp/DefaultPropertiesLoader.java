package com.googlecode.t7mp;

import java.io.IOException;
import java.util.Properties;

public class DefaultPropertiesLoader implements PropertiesLoader {

	@Override
	public Properties load(Class<?> clazz, String propertiesFilename) throws IOException {
		Properties p = new Properties();
		p.load(clazz.getResourceAsStream(propertiesFilename));
		return p;
	}

}
