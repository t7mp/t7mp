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

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Uses Maven-API to resolve the Artifacts.
 * 
 *
 */
public class MyArtifactResolver {
	
	private ArtifactResolver resolver;
	private ArtifactFactory factory;
	private ArtifactRepository local;
	private List<ArtifactRepository> remoteRepositories;
	
	public MyArtifactResolver(ArtifactResolver resolver, ArtifactFactory factory, ArtifactRepository local, List<ArtifactRepository> remoteRepositories){
		this.remoteRepositories = remoteRepositories;
		this.factory = factory;
		this.resolver = resolver;
		this.local = local;
	}
	
	public Artifact resolve(String groupId, String artifactId, String version, String type, String scope) throws MojoExecutionException{
		Artifact artifact = factory.createDependencyArtifact(groupId, artifactId, VersionRange.createFromVersion(version), type, null, Artifact.SCOPE_COMPILE);
		try {
			resolver.resolve(artifact,remoteRepositories, local);
		} catch (ArtifactResolutionException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (ArtifactNotFoundException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		return artifact;
	}
	
	public Artifact resolveJar(String groupId, String artifactId, String version) throws MojoExecutionException {
		return resolve(groupId, artifactId, version, "jar", Artifact.SCOPE_COMPILE);
	}
	
	public Artifact resolveWar(String groupId, String artifactId, String version) throws MojoExecutionException {
		return resolve(groupId, artifactId, version, "war", Artifact.SCOPE_COMPILE);
	}
}