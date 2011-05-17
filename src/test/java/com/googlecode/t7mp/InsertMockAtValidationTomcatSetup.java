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
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.mockito.Mockito;

public class InsertMockAtValidationTomcatSetup extends DefaultTomcatSetup {

    public InsertMockAtValidationTomcatSetup(AbstractT7Mojo t7Mojo) {
        super(t7Mojo);
    }

    @Override
    protected void validateConfiguration() throws TomcatSetupException {
        super.validateConfiguration();
        // try to fake some components
        setupUtil = new DoNothingSetupUtil();
        TomcatArtifactDescriptorReader artifactDescriptorReader = Mockito.mock(TomcatArtifactDescriptorReader.class);
        Artifact mavenArtifact = Mockito.mock(Artifact.class);
        JarArtifact jarArtifact = Mockito.mock(JarArtifact.class);

        Mockito.when(jarArtifact.getArtifact()).thenReturn(mavenArtifact);
        File fakeJar;
        try {
            fakeJar = File.createTempFile("mavenArtifact_", ".jar");
            fakeJar.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException("This Exception comes from a TestClass", e);
        }
        Mockito.when(mavenArtifact.getFile()).thenReturn(fakeJar);
        List<AbstractArtifact> artifactList = new ArrayList<AbstractArtifact>();
        artifactList.add(jarArtifact);
        Mockito.when(artifactDescriptorReader.getJarArtifacts(Mockito.anyString())).thenReturn(new ArrayList<JarArtifact>());
        this.artifactDescriptorReader = artifactDescriptorReader;
    }
}