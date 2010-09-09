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
import java.util.ArrayList;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactCollector;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.codehaus.plexus.archiver.manager.ArchiverManager;


/**
 * Maven components. Maybe used in subclasses.
 *
 */
public abstract class AbstractT7Mojo extends AbstractMojo {
	
	public static String T7_BOOTSTRAP_CONTEXT_ID = "com.googlecode.t7mp.Bootstrap";
	
    /**
     * Used to look up Artifacts in the remote repository.
     *
     * @component
     */
    protected ArtifactFactory factory;

    /**
     * Used to look up Artifacts in the remote repository.
     *
     * @component
     */
    protected ArtifactResolver resolver;

    /**
     * Artifact collector, needed to resolve dependencies.
     *
     * @component role="org.apache.maven.artifact.resolver.ArtifactCollector"
     * @required
     * @readonly
     */
    protected ArtifactCollector artifactCollector;

    /**
     * Location of the local repository.
     *
     * @parameter expression="${localRepository}"
     * @readonly
     * @required
     */
    protected ArtifactRepository local;

    /**
     * List of Remote Repositories used by the resolver
     *
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @readonly
     * @required
     */
    protected java.util.List<ArtifactRepository> remoteRepos;

    /**
     * To look up Archiver/UnArchiver implementations
     *
     * @component
     */
    protected ArchiverManager archiverManager;
    
	
	/**
	 * 
	 * @parameter expression="${t7.setAwait}" default-value="true"
	 * @required
	 * 
	 */
	protected boolean setAwait = true;
	
	/**
	 * 
	 * @parameter expression="${t7.lookInside}" default-value="false"
	 * @required
	 * 
	 */
	protected boolean lookInside = false;
	
	/**
	 * 
	 * @parameter expression="${t7.tomcatVersion}" default-value="7.0-SNAPSHOT"
	 */
	protected String tomcatVersion = "7.0-SNAPSHOT";
	
	/**
	 * 
	 * @parameter expression="${t7.tomcatHttpPort}" default-value="8080"
	 */
	protected int tomcatHttpPort = 8080;
	
	/**
	 * 
	 * @parameter expression="${t7.tomcatShutdownPort}" default-value="8005"
	 */
	protected int tomcatShutdownPort = 8005;
	
	/**
	 * 
	 * @parameter expression="${t7.tomcatShutdownCommand}" default-value="SHUTDOWN"
	 */
	protected String tomcatShutdownCommand = "SHUTDOWN";
	
	/**
	 * 
	 * @parameter expression="${t7.tomcatShutdownHost}" default-value="localhost"
	 * 
	 */
	protected String tomcatShutdownHost = "localhost";
	
	/**
	 * 
	 * @parameter default-value="${project.build.directory}/tomcat"
	 * @readonly // at the moment
	 * 
	 */
	protected File catalinaBase;
	
	/**
	 * 
	 * @parameter expression="${t7.tomcat.confdir}" default-value="${basedir}/src/main/tomcat/conf"
	 * @optional
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
     * @parameter default-value="${project.packaging}"
     * 
     * 
     */
    protected String packaging = "war";
    
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
    
    
    
    protected boolean isWebProject(){
    	return this.packaging.equals("war");
    }

	public boolean isSetAwait() {
		return setAwait;
	}

	public void setSetAwait(boolean setAwait) {
		this.setAwait = setAwait;
	}

	public boolean isLookInside() {
		return lookInside;
	}

	public void setLookInside(boolean lookInside) {
		this.lookInside = lookInside;
	}

	public String getTomcatVersion() {
		return tomcatVersion;
	}

	public void setTomcatVersion(String tomcatVersion) {
		this.tomcatVersion = tomcatVersion;
	}

	public int getTomcatHttpPort() {
		return tomcatHttpPort;
	}

	public void setTomcatHttpPort(int tomcatHttpPort) {
		this.tomcatHttpPort = tomcatHttpPort;
	}

	public int getTomcatShutdownPort() {
		return tomcatShutdownPort;
	}

	public void setTomcatShutdownPort(int tomcatShutdownPort) {
		this.tomcatShutdownPort = tomcatShutdownPort;
	}

	public String getTomcatShutdownCommand() {
		return tomcatShutdownCommand;
	}

	public void setTomcatShutdownCommand(String tomcatShutdownCommand) {
		this.tomcatShutdownCommand = tomcatShutdownCommand;
	}

	public String getTomcatShutdownHost() {
		return tomcatShutdownHost;
	}

	public void setTomcatShutdownHost(String tomcatShutdownHost) {
		this.tomcatShutdownHost = tomcatShutdownHost;
	}

	public File getUserConfigDir() {
		return userConfigDir;
	}

	public void setUserConfigDir(File userConfigDir) {
		this.userConfigDir = userConfigDir;
	}

	public File getWebappOutputDirectory() {
		return webappOutputDirectory;
	}

	public void setWebappOutputDirectory(File webappOutputDirectory) {
		this.webappOutputDirectory = webappOutputDirectory;
	}

}
