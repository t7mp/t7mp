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
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.archiver.manager.ArchiverManager;

/**
 * Maven components.
 * Maybe used in subclasses.
 */
public abstract class AbstractT7Mojo extends AbstractMojo {

    public static final String T7_BOOTSTRAP_CONTEXT_ID = "com.googlecode.t7mp.Bootstrap";

    public static final int DEFAULT_TOMCAT_HTTP_PORT = 8080;

    public static final int DEFAULT_TOMCAT_SHUTDOWN_PORT = 8005;

    public static final String DEFAULT_TOMCAT_VERSION = "7.0.12";

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
     * List of Remote Repositories used by the resolver.
     *
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @readonly
     * @required
     */
    protected java.util.List<ArtifactRepository> remoteRepos;

    /**
     * To look up Archiver/UnArchiver implementations.
     *
     * @component
     */
    protected ArchiverManager archiverManager;

    /**
     * 
     * @parameter expression="${t7.tomcatSetAwait}" default-value="true"
     * @required
     * 
     */
    protected boolean tomcatSetAwait = true;

    /**
     * 
     * @parameter expression="${t7.lookInside}" default-value="false"
     * @required
     * 
     */
    protected boolean lookInside = false;

    /**
     * 
     * @parameter expression="${t7.resolverUpdateSnapshotsAlways}" default-value="false"
     * @required
     */
    protected boolean resolverUpdateSnapshotsAllways = false;

    /**
     * 
     * @parameter expression="${t7.tomcatVersion}" default-value="7.0.12"
     */
    protected String tomcatVersion = DEFAULT_TOMCAT_VERSION;

    /**
     * 
     * @parameter expression="${t7.tomcatHttpPort}" default-value="8080"
     */
    protected int tomcatHttpPort = DEFAULT_TOMCAT_HTTP_PORT;

    /**
     * 
     * @parameter expression="${t7.tomcatShutdownPort}" default-value="8005"
     */
    protected int tomcatShutdownPort = DEFAULT_TOMCAT_SHUTDOWN_PORT;

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
     * @parameter expression="${t7.tomcatConfigDirectory}" default-value="${basedir}/src/main/tomcat/conf"
     * @optional
     * 
     */
    protected File tomcatConfigDirectory;

    /**
     * 
     * @parameter expression="${t7.overwriteWebXML}"
     */
    protected File overwriteWebXML;

    /**
     * 
     * @parameter default-value="${project.build.directory}/${project.build.finalName}"
     * @readonly
     * 
     */
    protected File webappOutputDirectory;

    /**
     * @parameter default-value="${project.build.finalName}"
     * @readonly
     */
    protected String buildFinalName;

    /**
     * @parameter default-value="${basedir}/src/main/webapp"
     * @readonly
     */
    protected File webappSourceDirectory;

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
    protected ArrayList<SystemProperty> systemProperties = new ArrayList<SystemProperty>();

    /**
     * 
     * @parameter
     */
    protected ArrayList<JarArtifact> libs = new ArrayList<JarArtifact>();

    /**
     * 
     * @parameter
     */
    protected ArrayList<ScannerConfiguration> scanners = new ArrayList<ScannerConfiguration>();

    private Log log;

    protected boolean isWebProject() {
        return this.packaging.equals("war");
    }

    public boolean isTomcatSetAwait() {
        return tomcatSetAwait;
    }

    public void setTomcatSetAwait(boolean setAwait) {
        this.tomcatSetAwait = setAwait;
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
        return tomcatConfigDirectory;
    }

    public void setUserConfigDir(File userConfigDir) {
        this.tomcatConfigDirectory = userConfigDir;
    }

    public File getWebappOutputDirectory() {
        return webappOutputDirectory;
    }

    public void setWebappOutputDirectory(File webappOutputDirectory) {
        this.webappOutputDirectory = webappOutputDirectory;
    }

    public File getOverwriteWebXML() {
        return overwriteWebXML;
    }

    public void setOverwriteWebXML(File overwriteWebXML) {
        this.overwriteWebXML = overwriteWebXML;
    }

    public ArrayList<ScannerConfiguration> getScanners() {
        return scanners;
    }

    @Override
    public Log getLog() {
        if (this.log == null) {
            if (lookInside) {
                this.log = new LookInsideLog(super.getLog());
            } else {
                this.log = super.getLog();
            }
        }
        return this.log;
    }

}
