package com.googlecode.t7mp;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.catalina.startup.Bootstrap;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * @goal run
 * @requiresDependencyResolution runtime
 * 
 *
 */
public class RunMojo extends AbstractRunMojo {
	
	/**
	 * 
	 * @parameter expression="${t7.fork}" default-value="false"
	 * @required
	 * 
	 */
	protected boolean fork;
	
	/**
	 * 
	 * @parameter expression="${t7.tomcat.version}" default-value="7.0-SNAPSHOT"
	 */
	protected String tomcatVersion;
	
	/**
	 * 
	 * @parameter default-value="${project.build.directory}/tomcat"
	 * @readonly // at the moment
	 * 
	 */
	protected File catalinaBase;
	
	/**
	 * 
	 * @parameter expression="${basedir}/src/main/tomcatconf"
	 * 
	 */
	protected File userConfigDir;
	
    /**
     * 
     * @parameter default-value="${project.build.directory}/${project.build.finalName}"
     * @readonly
     * 
     */
    protected File webappOutputDirectory;
    
    /**
     * 
     * @parameter
     */
    protected ArrayList<WebappArtifact> webapps = new ArrayList<WebappArtifact>();
    
    /**
     * 
     * @parameter
     */
    protected ArrayList<JarArtifact> libs = new ArrayList<JarArtifact>();
    
    
    
    protected Bootstrap bootstrap;
	
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		PreConditions.checkConfiguredTomcatVersion(getLog(), tomcatVersion);
		
		TomcatConfigurator configurator = new TomcatConfigurator(catalinaBase);
		configurator.createTomcatDirectories()
					.copyDefaultConfig()
					.copyUserConfigs(userConfigDir);
		
		MyArtifactResolver myArtifactResolver = new MyArtifactResolver(resolver, factory, local, remoteRepos);
		ArtifactDispatcher libDispatcher = new ArtifactDispatcher(myArtifactResolver, catalinaBase);
		libDispatcher.resolveArtifacts(new TomcatJarArtifactHelper().getTomcatArtifacts(this.tomcatVersion)).copyTo("lib");
		libDispatcher.clear();
		libDispatcher.resolveArtifacts(libs).copyTo("lib");
		libDispatcher.clear();
		libDispatcher.resolveArtifacts(webapps).copyTo("webapps");
		
		copyWebapp();
		
		System.setProperty("catalina.home", catalinaBase.getAbsolutePath());
		System.setProperty("catalina.base", catalinaBase.getAbsolutePath());
		bootstrap = new Bootstrap();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		getLog().info("Starting Tomcat ...");
		try {
			bootstrap.init();
			bootstrap.setAwait((!fork));
			bootstrap.start();
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private void copyWebapp() throws MojoExecutionException {
		if(!this.webappOutputDirectory.exists()){
			return;
		}
		try {
			FileUtils.copyDirectory(this.webappOutputDirectory, new File(catalinaBase, "/webapps/" + this.webappOutputDirectory.getName()));
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
	
	private void shutdown() throws MojoExecutionException{
		getLog().info("Stopping Tomcat ...");
		if(this.bootstrap != null){
			try {
				bootstrap.stop();
			} catch (Exception e) {
				throw new MojoExecutionException(e.getMessage(), e);
			}
		}
	}
	

	class ShutdownHook extends Thread {
		@Override
		public void run() {
			try {
				shutdown();
			} catch (MojoExecutionException e) {
				e.printStackTrace();
			}
		}
	}

}