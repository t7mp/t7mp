/**
 * Copyright (C) 2010-2011 Joerg Bellmann <joerg.bellmann@googlemail.com>
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactCollector;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.manager.ArchiverManager;

import com.googlecode.t7mp.scanner.ScannerConfiguration;

/**
 * Maven components.
 * Maybe used in subclasses.
 */
public abstract class AbstractT7Mojo extends AbstractMojo {

    public static final String T7_BOOTSTRAP_CONTEXT_ID = "com.googlecode.t7mp.Bootstrap";

    public static final int DEFAULT_TOMCAT_HTTP_PORT = 8080;

    public static final int DEFAULT_TOMCAT_SHUTDOWN_PORT = 8005;

    public static final String DEFAULT_TOMCAT_VERSION = "7.0.22";

    public static final String CONTEXT_PATH_ROOT = "ROOT";

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     * 
     */
    protected MavenProject mavenProject;

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
     * @parameter expression="${t7.tomcatVersion}" default-value="7.0.22"
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
     *
     * @parameter expression="${t7.contextPath}" default-value="${project.build.finalName}"
     * @optional
     *
     */
    protected String contextPath;

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
     * @parameter expression="${t7.scanClasses}" default-value="false"
     */
    protected boolean scanClasses = false;

    /**
     * 
     * @parameter expression="${t7.addGithubRepository}" default-value="false"
     */
    protected boolean addGithubRepository = false;

    /**
     * @parameter default-value="${basedir}/target/classes"
     * @readonly
     */
    protected File webappClassDirectory;

    /**
     * @parameter
     * @optional
     */
    protected File contextFile = null;

    /**
     * 
     * @parameter
     */
    protected ArrayList<AbstractArtifact> webapps = new ArrayList<AbstractArtifact>();

    /**
     * 
     * @parameter
     */
    protected Map<String, String> systemProperties = new HashMap<String, String>();

    /**
     * 
     * @parameter
     */
    protected List<AbstractArtifact> libs = new ArrayList<AbstractArtifact>();

    /**
     * 
     * @parameter
     */
    protected ArrayList<ScannerConfiguration> scanners = new ArrayList<ScannerConfiguration>();

    /**
     * 
     * @parameter default-value="false"
     */
    protected boolean downloadTomcatExamples = false;

    /**
     * 
     * @parameter default-value="false"
     */
    protected boolean suspendConsoleOutput = false;

    private Log log;

    public boolean isWebProject() {
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

    public boolean isScanClasses() {
        return scanClasses;
    }

    public void setScanClasses(boolean scanClasses) {
        this.scanClasses = scanClasses;
    }

    public boolean isAddGithubRepository() {
        return addGithubRepository;
    }

    public void setAddGithubRepository(boolean addGithubRepository) {
        this.addGithubRepository = addGithubRepository;
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

    public String getContextPath() {
        if (StringUtils.isEmpty(contextPath) || "/".equals(contextPath)) {
            return CONTEXT_PATH_ROOT;
        }
        if (contextPath.startsWith("/")) {
            return contextPath.substring(1);
        }
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public File getOverwriteWebXML() {
        return overwriteWebXML;
    }

    public void setOverwriteWebXML(File overwriteWebXML) {
        this.overwriteWebXML = overwriteWebXML;
    }

    public File getContextFile() {
        return contextFile;
    }

    public void setContextFile(File contextFile) {
        this.contextFile = contextFile;
    }

    public File getCatalinaBase() {
        return catalinaBase;
    }

    public void setCatalinaBase(File catalinaBase) {
        this.catalinaBase = catalinaBase;
    }

    public ArrayList<ScannerConfiguration> getScanners() {
        return scanners;
    }

    public String getBuildFinalName() {
        return buildFinalName;
    }

    public void setBuildFinalName(String buildFinalName) {
        this.buildFinalName = buildFinalName;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public List<AbstractArtifact> getLibs() {
        return libs;
    }

    public void setLibs(ArrayList<AbstractArtifact> libs) {
        this.libs = libs;
    }

    public File getTomcatConfigDirectory() {
        return tomcatConfigDirectory;
    }

    public void setTomcatConfigDirectory(File tomcatConfigDirectory) {
        this.tomcatConfigDirectory = tomcatConfigDirectory;
    }

    public File getWebappSourceDirectory() {
        return webappSourceDirectory;
    }

    public void setWebappSourceDirectory(File webappSourceDirectory) {
        this.webappSourceDirectory = webappSourceDirectory;
    }

    public File getWebappClassDirectory() {
        return webappClassDirectory;
    }

    public void setWebappClassDirectory(File webappClassDirectory) {
        this.webappClassDirectory = webappClassDirectory;
    }

    public List<AbstractArtifact> getWebapps() {
        return webapps;
    }

    public void setWebapps(ArrayList<AbstractArtifact> webapps) {
        this.webapps = webapps;
    }

    public Map<String, String> getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(Map<String, String> systemProperties) {
        this.systemProperties = systemProperties;
    }

    public ArtifactFactory getFactory() {
        return factory;
    }

    public void setFactory(ArtifactFactory factory) {
        this.factory = factory;
    }

    public ArtifactResolver getResolver() {
        return resolver;
    }

    public void setResolver(ArtifactResolver resolver) {
        this.resolver = resolver;
    }

    public ArtifactCollector getArtifactCollector() {
        return artifactCollector;
    }

    public void setArtifactCollector(ArtifactCollector artifactCollector) {
        this.artifactCollector = artifactCollector;
    }

    public ArtifactRepository getLocal() {
        return local;
    }

    public void setLocal(ArtifactRepository local) {
        this.local = local;
    }

    public java.util.List<ArtifactRepository> getRemoteRepos() {
        return remoteRepos;
    }

    public void setRemoteRepos(java.util.List<ArtifactRepository> remoteRepos) {
        this.remoteRepos = remoteRepos;
    }

    public ArchiverManager getArchiverManager() {
        return archiverManager;
    }

    public void setArchiverManager(ArchiverManager archiverManager) {
        this.archiverManager = archiverManager;
    }

    public boolean isResolverUpdateSnapshotsAllways() {
        return resolverUpdateSnapshotsAllways;
    }

    public void setResolverUpdateSnapshotsAllways(boolean resolverUpdateSnapshotsAllways) {
        this.resolverUpdateSnapshotsAllways = resolverUpdateSnapshotsAllways;
    }

    public void setScanners(ArrayList<ScannerConfiguration> scanners) {
        this.scanners = scanners;
    }

    public MavenProject getMavenProject() {
        return mavenProject;
    }

    public void setMavenProject(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
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

    public boolean isDownloadTomcatExamples() {
        return downloadTomcatExamples;
    }

    public void setDownloadTomcatExamples(boolean downloadTomcatExamples) {
        this.downloadTomcatExamples = downloadTomcatExamples;
    }

    public boolean isSuspendConsoleOutput() {
        return suspendConsoleOutput;
    }

    public void setSuspendConsoleOutput(boolean suspendConsoleOutput) {
        this.suspendConsoleOutput = suspendConsoleOutput;
    }

}
