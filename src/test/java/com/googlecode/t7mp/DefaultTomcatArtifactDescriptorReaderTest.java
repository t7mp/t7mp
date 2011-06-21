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
package com.googlecode.t7mp;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.googlecode.t7mp.artifact.JarArtifact;

@Ignore
public class DefaultTomcatArtifactDescriptorReaderTest {

    private Log mockLog = Mockito.mock(Log.class);

    @Before
    public void setUp() {
        Mockito.doAnswer(new Answer<CharSequence>() {

            @Override
            public CharSequence answer(InvocationOnMock invocation) throws Throwable {
                System.out.println(invocation.getArguments()[0]);
                return null;
            }
        }).when(mockLog).debug(Mockito.anyString());
    }

    @Test
    public void testResolveTomcat7() {
        DefaultTomcatArtifactDescriptorReader reader = new DefaultTomcatArtifactDescriptorReader(mockLog);
        reader.getMajorVersion("7.1.2");
        List<JarArtifact> artifactList = reader.getJarArtifacts("7.1.2");
        Assert.assertNotNull(artifactList);
        Assert.assertEquals("15 Artifacte sind gefordert", 15, artifactList.size());
        System.out.println(artifactList);
    }

    @Test(expected = TomcatSetupException.class)
    public void testResolveTomcatNullVersion() {
        DefaultTomcatArtifactDescriptorReader reader = new DefaultTomcatArtifactDescriptorReader(mockLog);
        reader.getJarArtifacts(null);
    }

    @Test(expected = TomcatSetupException.class)
    public void testResolveTomcatEmptyVersion() {
        DefaultTomcatArtifactDescriptorReader reader = new DefaultTomcatArtifactDescriptorReader(mockLog);
        reader.getJarArtifacts("");
    }

    @Test
    public void testMajorVersion() {
        DefaultTomcatArtifactDescriptorReader reader = new DefaultTomcatArtifactDescriptorReader(mockLog);
        String majorVersion = reader.getMajorVersion("7.1.2");
        Assert.assertNotNull(majorVersion);
        Assert.assertNotSame("", majorVersion);
        Assert.assertEquals("7", majorVersion);
    }

    @Test(expected = TomcatSetupException.class)
    public void testMajorVersionFailure() {
        DefaultTomcatArtifactDescriptorReader reader = new DefaultTomcatArtifactDescriptorReader(mockLog);
        reader.getMajorVersion("71.2");
    }

    @Test(expected = TomcatSetupException.class)
    public void testPropertiesLoaderException() throws IOException {
        DefaultTomcatArtifactDescriptorReader reader = new DefaultTomcatArtifactDescriptorReader(mockLog, new ExceptionalPropertiesLoader());
        reader.getJarArtifacts("7.1.2");
    }

    public static class ExceptionalPropertiesLoader implements PropertiesLoader {

        @Override
        public Properties load(Class<?> clazz, String propertiesFilename) throws IOException {
            throw new IOException("LOADERTESTEXCEPTION");
        }

    }

}
