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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.IOUtil;


/**
 * Resolves artifacts from configuration-section and copy them to a specified directory.
 * 
 */
class TomcatArtifactDispatcher {
	
	protected MyArtifactResolver myArtifactResolver;
	
	protected List<AbstractArtifact> resolvedArtifacts = new ArrayList<AbstractArtifact>();
	
	protected File catalinaBaseDir;
	
	TomcatArtifactDispatcher(MyArtifactResolver myArtifactResolver, File catalinaBaseDir){
		this.myArtifactResolver = myArtifactResolver;
		this.catalinaBaseDir = catalinaBaseDir;
	}
	
	TomcatArtifactDispatcher resolveArtifacts(List<? extends AbstractArtifact> artifacts) throws MojoExecutionException{
		for(AbstractArtifact abstractArtifact : artifacts){
			Artifact artifact = myArtifactResolver.resolve(abstractArtifact.getGroupId(), abstractArtifact.getArtifactId(), abstractArtifact.getVersion(), abstractArtifact.getType(), Artifact.SCOPE_COMPILE);
			abstractArtifact.setArtifact(artifact);
			resolvedArtifacts.add(abstractArtifact);
		}
		return this;
	}
	
	void copyTo(String directoryName) throws MojoExecutionException{
		for(AbstractArtifact artifact : this.resolvedArtifacts){
			try {
				String targetFileName = createTargetFileName(artifact);
				IOUtil.copy(new FileInputStream(artifact.getArtifact().getFile()), new FileOutputStream(new File(catalinaBaseDir, "/" + directoryName + "/" + targetFileName)));
			} catch (FileNotFoundException e) {
				throw new MojoExecutionException(e.getMessage(), e);
			} catch (IOException e) {
				throw new MojoExecutionException(e.getMessage(), e);
			}
		}
	}
	
	protected String createTargetFileName(AbstractArtifact abstractArtifact){
		if(abstractArtifact.getClass().isAssignableFrom(WebappArtifact.class)){
			return ((WebappArtifact)abstractArtifact).getContextPath() + "." + abstractArtifact.getType();
		}
		return abstractArtifact.getArtifactId() + "-" + abstractArtifact.getVersion() + "." + abstractArtifact.getType();
	}
	
	void clear(){
		resolvedArtifacts.clear();
	}
}
