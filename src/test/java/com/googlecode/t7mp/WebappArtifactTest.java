package com.googlecode.t7mp;

import org.apache.maven.artifact.Artifact;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class WebappArtifactTest {
	
	@Test
	public void testWarArtifact(){
		WebappArtifact artifact = new WebappArtifact();
		artifact.setArtifactId(ArtifactConstants.ARTIFACTID);
		artifact.setGroupId(ArtifactConstants.GROUPID);
		artifact.setVersion(ArtifactConstants.VERSION);
		artifact.setType(ArtifactConstants.WAR_TYPE);
		artifact.setClassifier(ArtifactConstants.CLASSIFIER);
		artifact.setContextPath(ArtifactConstants.CONTEXTPATH);
		Assert.assertEquals(ArtifactConstants.ARTIFACTID, artifact.getArtifactId());
		Assert.assertEquals(ArtifactConstants.GROUPID, artifact.getGroupId());
		Assert.assertEquals(ArtifactConstants.WAR_TYPE, artifact.getType());
		Assert.assertEquals(ArtifactConstants.VERSION, artifact.getVersion());
		Assert.assertEquals(ArtifactConstants.CLASSIFIER, artifact.getClassifier());
		Assert.assertEquals(ArtifactConstants.CONTEXTPATH, artifact.getContextPath());
		// WebappArtifact should return war as Type, set this property should have no effect
		artifact.setType(ArtifactConstants.JAR_TYPE);
		Assert.assertEquals(ArtifactConstants.WAR_TYPE, artifact.getType());
	}
	
	@Test
	public void testContextPathNotSet(){
		WebappArtifact artifact = new WebappArtifact();
		artifact.setArtifactId(ArtifactConstants.ARTIFACTID);
		Assert.assertEquals(ArtifactConstants.ARTIFACTID, artifact.getContextPath());
	}
	
	@Test
	public void testContextPathConfiguredWithSlash(){
		WebappArtifact artifact = new WebappArtifact();
		artifact.setContextPath("/" + ArtifactConstants.CONTEXTPATH);
		Assert.assertEquals(ArtifactConstants.CONTEXTPATH, artifact.getContextPath());
	}
	
	@Test
	public void testContextPathConfiguredWithOutSlash(){
		WebappArtifact artifact = new WebappArtifact();
		artifact.setContextPath(ArtifactConstants.CONTEXTPATH);
		Assert.assertEquals(ArtifactConstants.CONTEXTPATH, artifact.getContextPath());
	}
	
	@Test
	public void testContextPathConfiguredWithNull(){
		WebappArtifact artifact = new WebappArtifact();
		artifact.setArtifactId(ArtifactConstants.ARTIFACTID);
		artifact.setContextPath(null);
		Assert.assertEquals(ArtifactConstants.ARTIFACTID, artifact.getContextPath());
	}
	
	@Test
	public void testContextPathConfiguredWithEmptyString(){
		WebappArtifact artifact = new WebappArtifact();
		artifact.setArtifactId(ArtifactConstants.ARTIFACTID);
		artifact.setContextPath("");
		Assert.assertEquals(ArtifactConstants.ARTIFACTID, artifact.getContextPath());
	}
	
	@Test
	public void testArtifactConstructor(){
		Artifact artifact = Mockito.mock(Artifact.class);
		Mockito.when(artifact.getArtifactId()).thenReturn(ArtifactConstants.ARTIFACTID);
		Mockito.when(artifact.getGroupId()).thenReturn(ArtifactConstants.GROUPID);
		Mockito.when(artifact.getVersion()).thenReturn(ArtifactConstants.VERSION);
		Mockito.when(artifact.getClassifier()).thenReturn(ArtifactConstants.CLASSIFIER);
		Mockito.when(artifact.getType()).thenReturn(ArtifactConstants.JAR_TYPE);
		WebappArtifact webappArtifact = new WebappArtifact(artifact);
		
		Assert.assertEquals(ArtifactConstants.GROUPID, webappArtifact.getGroupId());
		Assert.assertEquals(ArtifactConstants.ARTIFACTID, webappArtifact.getArtifactId());
		Assert.assertEquals(ArtifactConstants.WAR_TYPE, webappArtifact.getType());
		Assert.assertEquals(ArtifactConstants.CLASSIFIER, webappArtifact.getClassifier());
		Assert.assertEquals(ArtifactConstants.ARTIFACTID, webappArtifact.getContextPath());
		Assert.assertNotNull(webappArtifact.getArtifact());
		Assert.assertEquals(artifact,webappArtifact.getArtifact());
	}

}
