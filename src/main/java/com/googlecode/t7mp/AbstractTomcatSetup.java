package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * 
 * 
 *
 */
public abstract class AbstractTomcatSetup implements TomcatSetup {
	
	protected AbstractT7Mojo t7Mojo = null;
	protected SetupUtil setupUtil = new CommonsSetupUtil();
	
	protected Log log;
	
	protected TomcatDirectorySetup directorySetup;
	protected TomcatConfigFilesSetup configFilesSetup;
	protected TomcatArtifactDescriptorReader artifactDescriptorReader;
	protected MyArtifactResolver myArtifactResolver;
	protected TomcatArtifactDispatcher libDispatcher;
	
	public AbstractTomcatSetup(AbstractT7Mojo t7Mojo){
		this.t7Mojo = t7Mojo;
	}
	
	protected abstract void configure() throws TomcatSetupException;
	
	/**
	 * Validates that all needed Dependencies are set.
	 * 
	 * @throws TomcatSetupException
	 */
	protected void validateConfiguration() throws TomcatSetupException {
		TomcatSetupException.notNull(t7Mojo, "t7Mojo");
		TomcatSetupException.notNull(log, "log");
		TomcatSetupException.notNull(setupUtil, "setupUtil");
		TomcatSetupException.notNull(directorySetup, "directorySetup");
		TomcatSetupException.notNull(configFilesSetup, "configFilesSetup");
		TomcatSetupException.notNull(artifactDescriptorReader, "artifactDescriptorReader");
		TomcatSetupException.notNull(libDispatcher, "libDispatcher");
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void buildTomcat() throws MojoExecutionException {
		try {
			configure();
			validateConfiguration();
			directorySetup.createTomcatDirectories();
			configFilesSetup.copyDefaultConfig();
			configFilesSetup.copyUserConfigs(this.t7Mojo.userConfigDir);
			List<JarArtifact> tomcatLibs = artifactDescriptorReader.getJarArtifacts(this.t7Mojo.tomcatVersion);
			libDispatcher.resolveArtifacts(tomcatLibs).copyTo("lib");
			libDispatcher.clear();
			libDispatcher.resolveArtifacts(this.t7Mojo.libs).copyTo("lib");
			libDispatcher.clear();
			libDispatcher.resolveArtifacts(this.t7Mojo.webapps).copyTo("webapps");
			copyWebapp();
		} catch (TomcatSetupException e) {
			this.t7Mojo.getLog().error("Error setting up tomcat.");
			this.t7Mojo.getLog().error(e.getMessage(),e);
			throw new MojoExecutionException(e.getMessage(),e);
		}
	}
	
	protected void copyWebapp() throws TomcatSetupException {
		if((this.t7Mojo.webappOutputDirectory == null) || (!this.t7Mojo.webappOutputDirectory.exists())){
			return;
		}
		try {
			setupUtil.copyDirectory(this.t7Mojo.webappOutputDirectory, new File(this.t7Mojo.catalinaBase, "/webapps/" + this.t7Mojo.webappOutputDirectory.getName()));
		} catch (IOException e) {
			throw new TomcatSetupException(e.getMessage(), e);
		}
	}

}