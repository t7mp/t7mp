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

    protected Bootstrap bootstrap;
    
    protected TomcatSetup tomcatSetup;
	
	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException, MojoFailureException {
		PreConditions.checkConfiguredTomcatVersion(getLog(), tomcatVersion);
		
		this.tomcatSetup = getTomcatSetup();
		this.tomcatSetup.buildTomcat();
		
		System.setProperty("catalina.home", catalinaBase.getAbsolutePath());
		System.setProperty("catalina.base", catalinaBase.getAbsolutePath());
		bootstrap = getBootstrap();
		getLog().info("Starting Tomcat ...");
		try {
			bootstrap.init();
			if(setAwait){
				Runtime.getRuntime().addShutdownHook(new TomcatShutdownHook(this.bootstrap));
				bootstrap.setAwait(setAwait);
				bootstrap.start();
			}else{
				bootstrap.start();
				getPluginContext().put(T7_BOOTSTRAP_CONTEXT_ID, bootstrap);
				Runtime.getRuntime().addShutdownHook(new TomcatShutdownHook(this.bootstrap));
				getLog().info("Tomcat started");
			}
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
	
	protected TomcatSetup getTomcatSetup(){
		return new DefaultTomcatSetup((AbstractT7Mojo)this);
	}
	
	protected Bootstrap getBootstrap(){
		return new Bootstrap();
	}

}