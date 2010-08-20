package com.googlecode.t7mp;

import org.junit.Assert;
import org.junit.Test;

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

}
