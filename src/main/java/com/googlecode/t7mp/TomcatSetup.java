package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;

public class TomcatSetup {
	
	private AbstractT7Mojo t7Mojo = null;
	
	public TomcatSetup withMojo(AbstractT7Mojo t7Mojo){
		TomcatSetupException.notNull(t7Mojo, "t7Mojo");
		this.t7Mojo = t7Mojo;
		return this;
	}
	
	public void setUp() throws MojoExecutionException{
		
		// TODO make it runs :-)
		
//		TomcatConfigFilesSetup configurator = new TomcatConfigFilesSetup(catalinaBase, getLog(), null);
////		configurator.createTomcatDirectories()
////					.copyDefaultConfig()
////					.copyUserConfigs(userConfigDir);
//		
//		MyArtifactResolver myArtifactResolver = new MyArtifactResolver(resolver, factory, local, remoteRepos);
//		TomcatArtifactDispatcher libDispatcher = new TomcatArtifactDispatcher(myArtifactResolver, catalinaBase);
//		libDispatcher.resolveArtifacts(new TomcatJarArtifactHelper().getTomcatArtifacts(this.tomcatVersion)).copyTo("lib");
//		libDispatcher.clear();
//		libDispatcher.resolveArtifacts(libs).copyTo("lib");
//		libDispatcher.clear();
//		libDispatcher.resolveArtifacts(webapps).copyTo("webapps");
		
		copyWebapp();
	}
	
	private void copyWebapp() throws MojoExecutionException {
		if(!this.t7Mojo.webappOutputDirectory.exists()){
			return;
		}
		try {
			FileUtils.copyDirectory(this.t7Mojo.webappOutputDirectory, new File(this.t7Mojo.catalinaBase, "/webapps/" + this.t7Mojo.webappOutputDirectory.getName()));
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}