/**
 * Copyright (C) 2010-2011 Joerg Bellmann <joerg.bellmann@googlemail.com>
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
package com.googlecode.t7mp.artifact;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.junit.Before;
import org.junit.Ignore;
import org.mockito.Mockito;

import com.googlecode.t7mp.ArtifactConstants;

@Ignore
public class MyArtifactResolverTest {

    private ArtifactResolver resolver = Mockito.mock(ArtifactResolver.class);
    private ArtifactFactory factory = Mockito.mock(ArtifactFactory.class);
    private ArtifactRepository local = Mockito.mock(ArtifactRepository.class);

    private Artifact artifact = null;

    @Before
    public void setUp() {
        artifact = Mockito.mock(Artifact.class);
        Mockito.when(artifact.getArtifactId()).thenReturn(ArtifactConstants.ARTIFACTID);
        Mockito.when(artifact.getGroupId()).thenReturn(ArtifactConstants.GROUPID);
        Mockito.when(artifact.getVersion()).thenReturn(ArtifactConstants.VERSION);
        Mockito.when(artifact.getClassifier()).thenReturn(ArtifactConstants.CLASSIFIER);
        Mockito.when(artifact.getType()).thenReturn(ArtifactConstants.JAR_TYPE);
    }

//    @Test
//    public void testArtifactResolver() throws MojoExecutionException {
//        Mockito.when(factory.createArtifact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(artifact);
//        MyArtifactResolver resolver = new MyArtifactResolver(this.resolver, this.factory, this.local, new ArrayList<ArtifactRepository>());
//        resolver.resolve(ArtifactConstants.GROUPID, ArtifactConstants.ARTIFACTID, ArtifactConstants.VERSION, ArtifactConstants.JAR_TYPE, Artifact.SCOPE_COMPILE);
//    }
//
//    @Test
//    public void testArtifactResolverResolveJar() throws MojoExecutionException {
//        Mockito.when(factory.createArtifact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(artifact);
//        MyArtifactResolver resolver = new MyArtifactResolver(this.resolver, this.factory, this.local, new ArrayList<ArtifactRepository>());
//        resolver.resolveJar(ArtifactConstants.GROUPID, ArtifactConstants.ARTIFACTID, ArtifactConstants.VERSION);
//    }
//
//    @Test
//    public void testArtifactResolverResolveWar() throws MojoExecutionException {
//        Mockito.when(factory.createArtifact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(artifact);
//        MyArtifactResolver resolver = new MyArtifactResolver(this.resolver, this.factory, this.local, new ArrayList<ArtifactRepository>());
//        resolver.resolveWar(ArtifactConstants.GROUPID, ArtifactConstants.ARTIFACTID, ArtifactConstants.VERSION);
//    }
//
//    @Test(expected = MojoExecutionException.class)
//    public void testArtifactResolverArtifactNotFound() throws MojoExecutionException, ArtifactResolutionException, ArtifactNotFoundException {
//        Mockito.when(factory.createArtifact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(artifact);
//        Mockito.doThrow(new ArtifactNotFoundException("TEST_EXCEPTION", artifact)).when(this.resolver).resolve(Mockito.any(Artifact.class), Mockito.anyList(), Mockito.any(ArtifactRepository.class));
//        MyArtifactResolver myResolver = new MyArtifactResolver(this.resolver, this.factory, this.local, new ArrayList<ArtifactRepository>());
//        myResolver.resolve(ArtifactConstants.GROUPID, ArtifactConstants.ARTIFACTID, ArtifactConstants.VERSION, ArtifactConstants.JAR_TYPE, Artifact.SCOPE_COMPILE);
//    }
//
//    @Test(expected = MojoExecutionException.class)
//    public void testArtifactResolutionException() throws MojoExecutionException, ArtifactResolutionException, ArtifactNotFoundException {
//        Mockito.when(factory.createArtifact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(artifact);
//        Mockito.doThrow(new ArtifactResolutionException("TEST_EXCEPTION", artifact)).when(resolver).resolve(Mockito.any(Artifact.class), Mockito.anyList(), Mockito.any(ArtifactRepository.class));
//        MyArtifactResolver resolver = new MyArtifactResolver(this.resolver, this.factory, this.local, new ArrayList<ArtifactRepository>());
//        resolver.resolve(ArtifactConstants.GROUPID, ArtifactConstants.ARTIFACTID, ArtifactConstants.VERSION, ArtifactConstants.JAR_TYPE, Artifact.SCOPE_COMPILE);
//    }
}
