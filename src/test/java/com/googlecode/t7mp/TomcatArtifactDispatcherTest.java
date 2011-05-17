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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TomcatArtifactDispatcherTest {

    private File catalinaBaseDir;
    private static int counter = 1;
    private MyArtifactResolver myArtifactResolver = Mockito.mock(MyArtifactResolver.class);

    private File sourceFile = null;

    @Before
    public void setUp() throws IOException {
        catalinaBaseDir = new File(new File(System.getProperty("java.io.tmpdir")), "catalinaBase_" + (++counter));
        catalinaBaseDir.mkdirs();
        catalinaBaseDir.deleteOnExit();
        sourceFile = File.createTempFile("sourceTest", ".tmp");
        sourceFile.deleteOnExit();
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(catalinaBaseDir);
        if (catalinaBaseDir.exists()) {
            System.err.println("Could not delete directory " + catalinaBaseDir.getAbsolutePath());
        }
    }

    @Test
    public void testArtifactDispatcher() throws MojoExecutionException, IOException {
        TomcatArtifactDispatcher dispatcher = new TomcatArtifactDispatcher(myArtifactResolver, catalinaBaseDir, Mockito.mock(SetupUtil.class), Mockito.mock(Log.class));
        Artifact artifact = Mockito.mock(Artifact.class);
        Mockito.when(artifact.getArtifactId()).thenReturn(ArtifactConstants.ARTIFACTID);
        Mockito.when(artifact.getGroupId()).thenReturn(ArtifactConstants.GROUPID);
        Mockito.when(artifact.getVersion()).thenReturn(ArtifactConstants.VERSION);
        Mockito.when(artifact.getClassifier()).thenReturn(ArtifactConstants.CLASSIFIER);
        Mockito.when(artifact.getType()).thenReturn(ArtifactConstants.JAR_TYPE);
        Mockito.when(artifact.getFile()).thenReturn(sourceFile);
        Mockito.when(myArtifactResolver.resolve(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(artifact);

        List<? extends AbstractArtifact> artifactList = getArtifacList();
        dispatcher.resolveArtifacts(artifactList);
        Assert.assertNotNull(dispatcher.resolvedArtifacts);
        Assert.assertTrue(artifactList.size() == dispatcher.resolvedArtifacts.size());
        for (AbstractArtifact a : dispatcher.resolvedArtifacts) {
            Assert.assertEquals(a.getArtifactId(), a.getArtifact().getArtifactId());
            Assert.assertEquals(a.getGroupId(), a.getArtifact().getGroupId());
            Assert.assertEquals(a.getVersion(), a.getArtifact().getVersion());
        }

        File targetDir = new File(catalinaBaseDir, "lib");
        targetDir.mkdirs();
        Assert.assertTrue(targetDir.exists());
        dispatcher.copyTo(targetDir.getName());
        File[] targetFiles = targetDir.listFiles(new FilesOnlyFileFilter());
        Assert.assertTrue(targetFiles.length == 2);

        for (File f : targetFiles) {
            System.out.println(f.getAbsolutePath());
            Assert.assertTrue(f.getName().equals(ArtifactConstants.ARTIFACTID + "-" + ArtifactConstants.VERSION + ".jar") || f.getName().equals(ArtifactConstants.ARTIFACTID + ".war"));
        }

        dispatcher.clear();
        Assert.assertTrue(dispatcher.resolvedArtifacts.size() == 0);
    }

    @Test
    public void testArtifactDispatcherWithLocal() throws MojoExecutionException, IOException {
        TomcatArtifactDispatcher dispatcher = new TomcatArtifactDispatcher(myArtifactResolver, catalinaBaseDir, Mockito.mock(SetupUtil.class), Mockito.mock(Log.class));

        List<? extends AbstractArtifact> artifactList = getLocalArtifacList();
        dispatcher.resolveArtifacts(artifactList);
        Assert.assertNotNull(dispatcher.resolvedArtifacts);
        Assert.assertTrue(artifactList.size() == dispatcher.resolvedArtifacts.size());

        File targetDir = new File(catalinaBaseDir, "lib");
        targetDir.mkdirs();
        Assert.assertTrue(targetDir.exists());
        dispatcher.copyTo(targetDir.getName());
        File[] targetFiles = targetDir.listFiles(new FilesOnlyFileFilter());
        Assert.assertTrue(targetFiles.length == 3);

        for (File f : targetFiles) {
            System.out.println(f.getAbsolutePath());
            Assert.assertTrue(f.getName().startsWith("ecj-"));
            Assert.assertTrue(f.getName().endsWith(".jar"));
        }

        dispatcher.clear();
        Assert.assertTrue(dispatcher.resolvedArtifacts.size() == 0);
    }

    @Test(expected = TomcatSetupException.class)
    public void testCopyArtifactException() throws IOException {
        SetupUtil setupUtil = Mockito.mock(SetupUtil.class);
        Mockito.doThrow(new IOException("TESTEXCEPTION")).when(setupUtil).copy(Mockito.any(InputStream.class), Mockito.any(OutputStream.class));
        TomcatArtifactDispatcher artifactDispatcher = new TomcatArtifactDispatcher(myArtifactResolver, catalinaBaseDir, Mockito.mock(SetupUtil.class), Mockito.mock(Log.class));
        artifactDispatcher.setupUtil = setupUtil;
        artifactDispatcher.resolvedArtifacts = getArtifacList();
        artifactDispatcher.copyTo("test");
        Mockito.verify(setupUtil, Mockito.atLeast(1)).copy(Mockito.any(InputStream.class), Mockito.any(OutputStream.class));
    }

    @Test(expected = TomcatSetupException.class)
    public void testArtifactResolverException() throws ArtifactResolutionException, ArtifactNotFoundException, MojoExecutionException, IOException {
        Mockito.doThrow(new MojoExecutionException("TESTEXCEPTION")).when(myArtifactResolver).resolve(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        TomcatArtifactDispatcher dispatcher = new TomcatArtifactDispatcher(myArtifactResolver, catalinaBaseDir, Mockito.mock(SetupUtil.class), Mockito.mock(Log.class));
        dispatcher.resolveArtifacts(getArtifacList());
    }

    private List<AbstractArtifact> getArtifacList() throws IOException {
        Artifact mArtifact = Mockito.mock(Artifact.class);
        Mockito.when(mArtifact.getFile()).thenReturn(File.createTempFile("anArtifact_", ".jar"));
        JarArtifact artifact = new JarArtifact();
        artifact.setArtifactId(ArtifactConstants.ARTIFACTID);
        artifact.setGroupId(ArtifactConstants.GROUPID);
        artifact.setVersion(ArtifactConstants.VERSION);
        artifact.setType(ArtifactConstants.JAR_TYPE);
        artifact.setClassifier(ArtifactConstants.CLASSIFIER);
        artifact.setArtifact(mArtifact);

        WebappArtifact artifact2 = new WebappArtifact();
        artifact2.setArtifactId(ArtifactConstants.ARTIFACTID);
        artifact2.setGroupId(ArtifactConstants.GROUPID);
        artifact2.setVersion(ArtifactConstants.VERSION);
        artifact2.setType(ArtifactConstants.JAR_TYPE);
        artifact2.setClassifier(ArtifactConstants.CLASSIFIER);
        artifact2.setArtifact(mArtifact);

        List<AbstractArtifact> resultList = new ArrayList<AbstractArtifact>();
        resultList.add(artifact);
        resultList.add(artifact2);
        return resultList;
    }

    private List<AbstractArtifact> getLocalArtifacList() throws IOException {
        JarArtifact artifact3 = new JarArtifact();
        artifact3.setArtifactId("ecj");
        artifact3.setGroupId("local");
        artifact3.setVersion("3.6");
        artifact3.setType(ArtifactConstants.JAR_TYPE);

        JarArtifact artifact2 = new JarArtifact();
        artifact2.setArtifactId("ecj");
        artifact2.setGroupId("local");
        artifact2.setVersion("3.6.2");
        artifact2.setType(ArtifactConstants.JAR_TYPE);

        JarArtifact artifact1 = new JarArtifact();
        artifact1.setArtifactId("ecj");
        artifact1.setGroupId("local");
        artifact1.setVersion("3.3.1");
        artifact1.setType(ArtifactConstants.JAR_TYPE);
        List<AbstractArtifact> resultList = new ArrayList<AbstractArtifact>();
        resultList.add(artifact2);
        resultList.add(artifact3);
        resultList.add(artifact1);
        return resultList;
    }

}
