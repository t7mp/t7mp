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

import org.apache.maven.artifact.Artifact;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class JarArtifactTest {
	
	
	
	@Test
	public void testJarArtifact(){
		JarArtifact artifact = new JarArtifact();
		artifact.setArtifactId(ArtifactConstants.ARTIFACTID);
		artifact.setGroupId(ArtifactConstants.GROUPID);
		artifact.setVersion(ArtifactConstants.VERSION);
		artifact.setType(ArtifactConstants.JAR_TYPE);
		artifact.setClassifier(ArtifactConstants.CLASSIFIER);
		Assert.assertEquals(ArtifactConstants.ARTIFACTID, artifact.getArtifactId());
		Assert.assertEquals(ArtifactConstants.GROUPID, artifact.getGroupId());
		Assert.assertEquals(ArtifactConstants.JAR_TYPE, artifact.getType());
		Assert.assertEquals(ArtifactConstants.VERSION, artifact.getVersion());
		Assert.assertEquals(ArtifactConstants.CLASSIFIER, artifact.getClassifier());
		// JarArtifact should return jar as Type, set this property should have no effect
		artifact.setType(ArtifactConstants.WAR_TYPE);
		Assert.assertEquals(ArtifactConstants.JAR_TYPE, artifact.getType());
	}
	
	@Test
	public void testArtifactConstructor(){
		Artifact artifact = Mockito.mock(Artifact.class);
		Mockito.when(artifact.getArtifactId()).thenReturn(ArtifactConstants.ARTIFACTID);
		Mockito.when(artifact.getGroupId()).thenReturn(ArtifactConstants.GROUPID);
		Mockito.when(artifact.getVersion()).thenReturn(ArtifactConstants.VERSION);
		Mockito.when(artifact.getClassifier()).thenReturn(ArtifactConstants.CLASSIFIER);
		Mockito.when(artifact.getType()).thenReturn(ArtifactConstants.JAR_TYPE);
		JarArtifact jarArtifact = new JarArtifact(artifact);
		
		Assert.assertEquals(ArtifactConstants.GROUPID, jarArtifact.getGroupId());
		Assert.assertEquals(ArtifactConstants.ARTIFACTID, jarArtifact.getArtifactId());
		Assert.assertEquals(ArtifactConstants.JAR_TYPE, jarArtifact.getType());
		Assert.assertEquals(ArtifactConstants.CLASSIFIER, jarArtifact.getClassifier());
		Assert.assertNotNull(jarArtifact.getArtifact());
		Assert.assertEquals(artifact,jarArtifact.getArtifact());
	}

}