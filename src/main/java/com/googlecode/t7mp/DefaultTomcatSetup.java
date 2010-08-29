/**
 * Copyright (C) 2010 Joerg Bellmann <joerg.bellmann@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

public class DefaultTomcatSetup implements TomcatSetup{
	
	protected AbstractT7Mojo t7Mojo = null;
	protected SetupUtil setupUtil = new CommonsSetupUtil();
	
	protected Log log;
	
	protected TomcatDirectorySetup directorySetup;
	protected TomcatConfigFilesSetup configFilesSetup;
	protected TomcatArtifactDescriptorReader artifactDescriptorReader;
	protected MyArtifactResolver myArtifactResolver;
	protected TomcatArtifactDispatcher libDispatcher;
	
	public DefaultTomcatSetup(AbstractT7Mojo t7Mojo){
		TomcatSetupException.notNull(t7Mojo, "T7Mojo");
		this.t7Mojo = t7Mojo;
	}
	
	@Override
	public void buildTomcat() throws MojoExecutionException{
		this.log = configureLog();
		try{
			//directories and configfiles
			directorySetup = getTomcatDirectorySetup();
			directorySetup.createTomcatDirectories();
			configFilesSetup = getTomcatConfigFilesSetup();
			configFilesSetup.copyDefaultConfig();
			configFilesSetup.copyUserConfigs(this.t7Mojo.userConfigDir);
			//read property-files for artifacts
			artifactDescriptorReader = getTomcatArtifactDescriptorReader();
			List<JarArtifact> tomcatLibs = artifactDescriptorReader.getJarArtifacts(this.t7Mojo.tomcatVersion);
			//resolve and copy
			libDispatcher = getTomcatArtifactDispatcher();
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
		copyWebapp();
	}
	
	protected Log configureLog() {
		Log log = t7Mojo.getLog();
		if(t7Mojo.lookInside){
			log = new LookInsideLog(log);
		}
		return log;
	}

	protected void copyWebapp() throws MojoExecutionException {
		if((this.t7Mojo.webappOutputDirectory == null) || (!this.t7Mojo.webappOutputDirectory.exists())){
			return;
		}
		try {
//			FileUtils.copyDirectory(this.t7Mojo.webappOutputDirectory, new File(this.t7Mojo.catalinaBase, "/webapps/" + this.t7Mojo.webappOutputDirectory.getName()));
			setupUtil.copyDirectory(this.t7Mojo.webappOutputDirectory, new File(this.t7Mojo.catalinaBase, "/webapps/" + this.t7Mojo.webappOutputDirectory.getName()));
		} catch (IOException e) {
			throw new TomcatSetupException(e.getMessage(), e);
		}
	}
	
	// Factory-Methods
	
	protected TomcatDirectorySetup getTomcatDirectorySetup(){
		return new TomcatDirectorySetup(this.t7Mojo.catalinaBase);
	}
	
	protected TomcatConfigFilesSetup getTomcatConfigFilesSetup(){
		return new TomcatConfigFilesSetup(this.t7Mojo.catalinaBase, this.t7Mojo.getLog(), setupUtil);
	}
	
	protected TomcatArtifactDescriptorReader getTomcatArtifactDescriptorReader(){
		return new DefaultTomcatArtifactDescriptorReader(this.t7Mojo.getLog());
	}
	
	protected MyArtifactResolver getMyArtifactResolver(){
		return new MyArtifactResolver(this.t7Mojo.resolver, this.t7Mojo.factory, this.t7Mojo.local, this.t7Mojo.remoteRepos);
	}
	
	protected TomcatArtifactDispatcher getTomcatArtifactDispatcher(){
		return new TomcatArtifactDispatcher(getMyArtifactResolver(), this.t7Mojo.catalinaBase, setupUtil);
	}

}