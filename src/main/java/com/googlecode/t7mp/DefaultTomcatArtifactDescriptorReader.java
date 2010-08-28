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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.plugin.logging.Log;

public class DefaultTomcatArtifactDescriptorReader implements TomcatArtifactDescriptorReader {
	
	private final Log log;
	private PropertiesLoader propertiesLoader = new DefaultPropertiesLoader();
	
	public DefaultTomcatArtifactDescriptorReader(Log log){
		this.log = log;
	}
	
	public DefaultTomcatArtifactDescriptorReader(Log log, PropertiesLoader propertiesLoader){
		this.log = log;
		this.propertiesLoader = propertiesLoader;
	}

	@Override
	public List<JarArtifact> getJarArtifacts(String tomcatVersion) {
		TomcatSetupException.notNull(tomcatVersion, "tomcatVersion");
		TomcatSetupException.notEmpty(tomcatVersion, "tomcatVersion");
		String artifactsPropertiesFile = "tomcat_" + getMajorVersion(tomcatVersion) + ".properties";
		Properties tomcatLibs = new Properties();
		try {
			log.debug("Try to load " + artifactsPropertiesFile + " as resource from classpath.");
			tomcatLibs = propertiesLoader.load(getClass(), artifactsPropertiesFile);
		} catch (IOException e) {
			throw new TomcatSetupException("Could not load " + artifactsPropertiesFile + " to resolve Tomcat-Artifacts", e);
		}
		List<JarArtifact> tomcatArtifactList = new ArrayList<JarArtifact>();
		for(Map.Entry<Object, Object> entry : tomcatLibs.entrySet()){
			String[] gav = entry.getValue().toString().split(":");
			JarArtifact jarArtifact = new JarArtifact();
			jarArtifact.setGroupId(gav[0]);
			jarArtifact.setArtifactId(gav[1]);
			if(gav.length == 3){
				jarArtifact.setVersion(gav[2]);
			}else{
				jarArtifact.setVersion(tomcatVersion);
			}
			tomcatArtifactList.add(jarArtifact);
		}
		return tomcatArtifactList;
	}
	
	protected String getMajorVersion(String tomcatVersion){
		String majorVersion = tomcatVersion.substring(0, tomcatVersion.indexOf("."));
		if(majorVersion.length() > 1){
			throw new TomcatSetupException("Could not get major version from " + tomcatVersion);
		}
		return majorVersion;
	}

}