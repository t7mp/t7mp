package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
	private boolean fork;
	
	/**
	 * 
	 * @parameter default-value="${project.build.directory}/tomcat"
	 * @readonly // at the moment
	 * 
	 */
	private File catalinaBase;
	
	/**
	 * 
	 * @parameter expression="${basedir}/src/main/tomcatconf"
	 * 
	 */
	private File userConfigDir;
	
    /**
     * 
     * @parameter default-value="${project.build.directory}/${project.build.finalName}"
     * @readonly
     * 
     */
    private File webappOutputDirectory;
    
    /**
     * 
     * @parameter
     */
    private ArrayList<WebappArtifact> webapps = new ArrayList<WebappArtifact>();
    
    /**
     * 
     * @parameter
     */
    private ArrayList<JarArtifact> libs = new ArrayList<JarArtifact>();
    
    
    
    private Bootstrap bootstrap;
	
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Starting Tomcat ...");
		TomcatConfigurator configurator = new TomcatConfigurator(catalinaBase);
		configurator.createTomcatDirectories()
					.copyDefaultConfig()
					.copyUserConfigs(userConfigDir);
		
		MyArtifactResolver myArtifactResolver = new MyArtifactResolver(resolver, factory, local, remoteRepos);
		ArtifactDispatcher libDispatcher = new ArtifactDispatcher(myArtifactResolver, catalinaBase);
		libDispatcher.resolveArtifacts(getTomcatArtifacts()).copyTo("lib");
		libDispatcher.clear();
		libDispatcher.resolveArtifacts(libs).copyTo("lib");
		libDispatcher.clear();
		libDispatcher.resolveArtifacts(webapps).copyTo("webapps");
		
		copyWebapp();
		
		System.setProperty("catalina.home", catalinaBase.getAbsolutePath());
		System.setProperty("catalina.base", catalinaBase.getAbsolutePath());
		bootstrap = new Bootstrap();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
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
	
	private List<JarArtifact> getTomcatArtifacts() throws MojoExecutionException{
		List<JarArtifact> tomcatArtifactList = new ArrayList<JarArtifact>();
		Properties tomcatLibs = new Properties();
		try {
			tomcatLibs.load(getClass().getResourceAsStream("artifacts.properties"));
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		for(Map.Entry<Object, Object> entry : tomcatLibs.entrySet()){
			String groupId = entry.getKey().toString().substring(0, entry.getKey().toString().lastIndexOf("."));
			String artifactId = entry.getValue().toString();
			String version = null;
			if(artifactId.startsWith("tomcat-")){
				version = "7.0-SNAPSHOT";
			}else{
				String[] split = artifactId.split(":");
				version = split[1];
				artifactId = split[0];
			}
			JarArtifact jarArtifact = new JarArtifact();
			jarArtifact.setGroupId(groupId);
			jarArtifact.setArtifactId(artifactId);
			jarArtifact.setVersion(version);
			tomcatArtifactList.add(jarArtifact);
		}
		return tomcatArtifactList;
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