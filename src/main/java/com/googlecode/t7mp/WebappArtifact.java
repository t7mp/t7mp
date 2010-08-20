package com.googlecode.t7mp;

import org.apache.maven.artifact.Artifact;

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

/**
 * 
 * 
 *
 */
public class WebappArtifact extends AbstractArtifact {
	
	private String contextPath;
	
	public WebappArtifact(){
		super();
	}
	
	public WebappArtifact(Artifact artifact){
		super(artifact);
	}

	@Override
	public String getType() {
		return "war";
	}

	public String getContextPath() {
		if(contextPath == null || contextPath.equals("")){
			return artifactId;
		}
		if(contextPath.startsWith("/")){
			return contextPath.substring(1);
		}
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
}