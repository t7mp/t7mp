package com.googlecode.t7mp;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Uses Maven-API to resolve the Artifacts.
 * 
 *
 */
class MyArtifactResolver {
	
	private ArtifactResolver resolver;
	private ArtifactFactory factory;
	private ArtifactRepository local;
	private List<ArtifactRepository> remoteRepositories;
	
	MyArtifactResolver(ArtifactResolver resolver, ArtifactFactory factory, ArtifactRepository local, List<ArtifactRepository> remoteRepositories){
		this.remoteRepositories = remoteRepositories;
		this.factory = factory;
		this.resolver = resolver;
		this.local = local;
	}
	
	
	public Artifact resolve(String groupId, String artifactId, String version, String type, String scope) throws MojoExecutionException{
		Artifact artifact = factory.createDependencyArtifact(groupId, artifactId, VersionRange.createFromVersion(version), type, null, Artifact.SCOPE_COMPILE);
		ArtifactResolutionRequest resolutionRequest = new ArtifactResolutionRequest().setArtifact(artifact).setLocalRepository(local).setRemoteRepositories(remoteRepositories);
		resolver.resolve(resolutionRequest);
		return artifact;
	}
	
	public Artifact resolveJar(String groupId, String artifactId, String version) throws MojoExecutionException {
		return resolve(groupId, artifactId, version, "jar", Artifact.SCOPE_COMPILE);
	}
	
	public Artifact resolveWar(String groupId, String artifactId, String version) throws MojoExecutionException {
		return resolve(groupId, artifactId, version, "war", Artifact.SCOPE_COMPILE);
	}
	
	

}
