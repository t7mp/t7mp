package com.googlecode.t7mp;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

class PreConditions {
	
	static void checkConfiguredTomcatVersion(Log log, String tomcatVersion) throws MojoExecutionException{
		if(!tomcatVersion.startsWith("7.")){
			log.info("");
			log.info("");
			log.error("==== MAVEN-T7-PLUGIN ====");
			log.error("This plugin supports only Version 7 of Tomcat. You configured: " + tomcatVersion + ". Cancel the Build.");
			log.error("=========================");
			log.info("");
			log.info("");
			throw new MojoExecutionException("This plugin supports only Version 7 of Tomcat. You configured " + tomcatVersion);
		}
	}
}