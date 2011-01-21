package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.mockito.Mockito;

public class SuccessfullCopyWebappMockSetup extends DefaultTomcatSetup {

    public SuccessfullCopyWebappMockSetup(AbstractT7Mojo t7Mojo) {
        super(t7Mojo);
    }

    @Override
    protected void validateConfiguration() throws TomcatSetupException {
        super.validateConfiguration();
        // try to fake some components
        TomcatArtifactDescriptorReader artifactDescriptorReader = Mockito.mock(TomcatArtifactDescriptorReader.class);
        Artifact mavenArtifact = Mockito.mock(Artifact.class);
        JarArtifact jarArtifact = Mockito.mock(JarArtifact.class);

        Mockito.when(jarArtifact.getArtifact()).thenReturn(mavenArtifact);
        File fakeJar;
        try {
            fakeJar = File.createTempFile("mavenArtifact_", ".jar");
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
