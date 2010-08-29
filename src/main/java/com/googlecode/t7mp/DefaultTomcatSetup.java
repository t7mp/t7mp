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

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;

public class DefaultTomcatSetup implements TomcatSetup{
	
	protected AbstractT7Mojo t7Mojo = null;
	protected SetupUtil setupUtil = new CommonsSetupUtil();
	
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
		try{
			//directories and configfiles
			directorySetup = new TomcatDirectorySetup(this.t7Mojo.catalinaBase);
			directorySetup.createTomcatDirectories();
			configFilesSetup = new TomcatConfigFilesSetup(this.t7Mojo.catalinaBase, this.t7Mojo.getLog(), setupUtil);
			configFilesSetup.copyDefaultConfig();
			configFilesSetup.copyUserConfigs(this.t7Mojo.userConfigDir);
			//read property-files for artifacts
			artifactDescriptorReader = new DefaultTomcatArtifactDescriptorReader(this.t7Mojo.getLog());
			List<JarArtifact> tomcatLibs = artifactDescriptorReader.getJarArtifacts(this.t7Mojo.tomcatVersion);
			//resolve and copy
			myArtifactResolver = new MyArtifactResolver(this.t7Mojo.resolver, this.t7Mojo.factory, this.t7Mojo.local, this.t7Mojo.remoteRepos);
			libDispatcher = new TomcatArtifactDispatcher(myArtifactResolver, this.t7Mojo.catalinaBase, setupUtil);
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