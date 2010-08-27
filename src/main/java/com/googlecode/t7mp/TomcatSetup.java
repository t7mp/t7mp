package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
		try{
			//directories and configfiles
			TomcatDirectorySetup directorySetup = new TomcatDirectorySetup(this.t7Mojo.catalinaBase);
			directorySetup.createTomcatDirectories();
			TomcatConfigFilesSetup configFilesSetup = new TomcatConfigFilesSetup(this.t7Mojo.catalinaBase, this.t7Mojo.getLog(), new CommonsSetupUtil());
			configFilesSetup.copyDefaultConfig();
			configFilesSetup.copyUserConfigs(this.t7Mojo.userConfigDir);
			//read property-files for artifacts
			TomcatArtifactDescriptorReader artifactDescriptorReader = new DefaultTomcatArtifactDescriptorReader(this.t7Mojo.getLog());
			List<JarArtifact> tomcatLibs = artifactDescriptorReader.getJarArtifacts(this.t7Mojo.tomcatVersion);
			//resolve and copy
			MyArtifactResolver myArtifactResolver = new MyArtifactResolver(this.t7Mojo.resolver, this.t7Mojo.factory, this.t7Mojo.local, this.t7Mojo.remoteRepos);
			TomcatArtifactDispatcher libDispatcher = new TomcatArtifactDispatcher(myArtifactResolver, this.t7Mojo.catalinaBase);
			libDispatcher.resolveArtifacts(tomcatLibs).copyTo("lib");
			libDispatcher.clear();
			libDispatcher.resolveArtifacts(this.t7Mojo.libs).copyTo("lib");
			libDispatcher.clear();
			libDispatcher.resolveArtifacts(this.t7Mojo.webapps).copyTo("webapps");
		}catch(TomcatSetupException e){
			this.t7Mojo.getLog().error("Error setting up tomcat.");
			this.t7Mojo.getLog().error(e.getMessage(),e);
			throw new MojoExecutionException(e.getMessage(),e);
		}
		
		
		
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