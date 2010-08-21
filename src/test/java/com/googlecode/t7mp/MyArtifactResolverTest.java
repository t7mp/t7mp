package com.googlecode.t7mp;

import java.util.ArrayList;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MyArtifactResolverTest {
	
	private ArtifactResolver resolver = Mockito.mock(ArtifactResolver.class);
	private ArtifactFactory factory = Mockito.mock(ArtifactFactory.class);
	private ArtifactRepository local = Mockito.mock(ArtifactRepository.class);
	
	private Artifact artifact = null;
	
	@Before
	public void setUp(){
		artifact = Mockito.mock(Artifact.class);
		Mockito.when(artifact.getArtifactId()).thenReturn(ArtifactConstants.ARTIFACTID);
		Mockito.when(artifact.getGroupId()).thenReturn(ArtifactConstants.GROUPID);
		Mockito.when(artifact.getVersion()).thenReturn(ArtifactConstants.VERSION);
		Mockito.when(artifact.getClassifier()).thenReturn(ArtifactConstants.CLASSIFIER);
		Mockito.when(artifact.getType()).thenReturn(ArtifactConstants.JAR_TYPE);
	}
	
	
	@Test
	public void testArtifactResolver() throws MojoExecutionException{
		Mockito.when(factory.createArtifact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(artifact);
		MyArtifactResolver resolver = new MyArtifactResolver(this.resolver, this.factory, this.local, new ArrayList<ArtifactRepository>());
		resolver.resolve(ArtifactConstants.GROUPID, ArtifactConstants.ARTIFACTID, ArtifactConstants.VERSION, ArtifactConstants.JAR_TYPE, Artifact.SCOPE_COMPILE);
	}
	
	@Test
	public void testArtifactResolverResolveJar() throws MojoExecutionException{
		Mockito.when(factory.createArtifact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(artifact);
		MyArtifactResolver resolver = new MyArtifactResolver(this.resolver, this.factory, this.local, new ArrayList<ArtifactRepository>());
		resolver.resolveJar(ArtifactConstants.GROUPID, ArtifactConstants.ARTIFACTID, ArtifactConstants.VERSION);
	}
	
	@Test
	public void testArtifactResolverResolveWar() throws MojoExecutionException{
		Mockito.when(factory.createArtifact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(artifact);
		MyArtifactResolver resolver = new MyArtifactResolver(this.resolver, this.factory, this.local, new ArrayList<ArtifactRepository>());
		resolver.resolveWar(ArtifactConstants.GROUPID, ArtifactConstants.ARTIFACTID, ArtifactConstants.VERSION);
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testArtifactResolverArtifactNotFound() throws MojoExecutionException, ArtifactResolutionException, ArtifactNotFoundException{
		Mockito.when(factory.createArtifact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(artifact);
		Mockito.doThrow(new ArtifactNotFoundException("TEST_EXCEPTION", artifact)).when(this.resolver).resolve(Mockito.any(Artifact.class), Mockito.anyList(), Mockito.any(ArtifactRepository.class));
		MyArtifactResolver myResolver = new MyArtifactResolver(this.resolver, this.factory, this.local, new ArrayList<ArtifactRepository>());
		myResolver.resolve(ArtifactConstants.GROUPID, ArtifactConstants.ARTIFACTID, ArtifactConstants.VERSION, ArtifactConstants.JAR_TYPE, Artifact.SCOPE_COMPILE);
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testArtifactResolutionException() throws MojoExecutionException, ArtifactResolutionException, ArtifactNotFoundException{
		Mockito.when(factory.createArtifact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(artifact);
		Mockito.doThrow(new ArtifactResolutionException("TEST_EXCEPTION", artifact)).when(resolver).resolve(Mockito.any(Artifact.class), Mockito.anyList(), Mockito.any(ArtifactRepository.class));
		MyArtifactResolver resolver = new MyArtifactResolver(this.resolver, this.factory, this.local, new ArrayList<ArtifactRepository>());
		resolver.resolve(ArtifactConstants.GROUPID, ArtifactConstants.ARTIFACTID, ArtifactConstants.VERSION, ArtifactConstants.JAR_TYPE, Artifact.SCOPE_COMPILE);
	}
}
