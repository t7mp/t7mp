package com.googlecode.t7mp;

import org.apache.maven.plugin.MojoExecutionException;

public interface TomcatSetup {
	
	void buildTomcat() throws MojoExecutionException;

}