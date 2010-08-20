package com.googlecode.t7mp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;

final class TomcatJarArtifactHelper {
	
	private TomcatJarArtifactHelper(){
		// 
	}
	
	static List<JarArtifact> getTomcatArtifacts(String tomcatVersion) throws MojoExecutionException{
		List<JarArtifact> tomcatArtifactList = new ArrayList<JarArtifact>();
		Properties tomcatLibs = new Properties();
		try {
			tomcatLibs.load(TomcatJarArtifactHelper.class.getResourceAsStream("artifacts.properties"));
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		for(Map.Entry<Object, Object> entry : tomcatLibs.entrySet()){
			String groupId = entry.getKey().toString().substring(0, entry.getKey().toString().lastIndexOf("."));
			String artifactId = entry.getValue().toString();
			String version = null;
			if(artifactId.startsWith("tomcat-")){
				version = tomcatVersion;
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

}