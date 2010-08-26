package com.googlecode.t7mp;

import java.io.IOException;
import java.util.Properties;

public interface PropertiesLoader {
	
	Properties load(Class<?> clazz, String propertiesFilename) throws IOException;

}