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

import org.apache.catalina.startup.Bootstrap;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * @goal run
 * @requiresDependencyResolution runtime
 * 
 *
 */
public class RunMojo extends AbstractT7Mojo {

    private Bootstrap bootstrap;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		PreConditions.checkConfiguredTomcatVersion(getLog(), tomcatVersion);
		
		new TomcatSetup().withMojo(this).setUp();
		
		System.setProperty("catalina.home", catalinaBase.getAbsolutePath());
		System.setProperty("catalina.base", catalinaBase.getAbsolutePath());
		bootstrap = new Bootstrap();
		getLog().info("Starting Tomcat ...");
		try {
			bootstrap.init();
			if(setAwait){
				Runtime.getRuntime().addShutdownHook(new TomcatShutdownHook(this.bootstrap));
				bootstrap.setAwait(setAwait);
				bootstrap.start();
			}else{
				bootstrap.start();
//				GlobalTomcatHolder.bootstrap = bootstrap;
				getPluginContext().put("TEST", bootstrap);
				Runtime.getRuntime().addShutdownHook(new TomcatShutdownHook(this.bootstrap));
				getLog().info("Tomcat started");
			}
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

//	private void shutdown() throws MojoExecutionException{
//		getLog().info("Shutting down tomcat ...");
//		if(this.bootstrap != null){
//			try {
//				bootstrap.stop();
//			} catch (Exception e) {
//				throw new MojoExecutionException(e.getMessage(), e);
//			}
//		}
//	}
	

//	class InternalShutdownHook extends Thread {
//		
//		@Override
//		public void run() {
//			try {
//				shutdown();
//			} catch (MojoExecutionException e) {
//				e.printStackTrace();
//			}
//		}
//	}

}