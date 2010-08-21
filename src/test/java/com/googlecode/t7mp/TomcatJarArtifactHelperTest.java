package com.googlecode.t7mp;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;
import org.mockito.Mockito;

public class TomcatJarArtifactHelperTest {
	
	@Test
	public void testTomcatArtifactHelper() throws MojoExecutionException{
		List<JarArtifact> artifacts = new TomcatJarArtifactHelper().getTomcatArtifacts("7.0-SNAPSHOT");
		Assert.assertNotNull(artifacts);
		Assert.assertTrue(artifacts.size() > 0);
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testTomcatArtifactHelperWithIOException() throws IOException, MojoExecutionException{
		Properties properties = Mockito.mock(Properties.class);
		Mockito.doThrow(new IOException()).when(properties).load(Mockito.any(InputStream.class));
		TomcatJarArtifactHelper helper = new TomcatJarArtifactHelper();
		helper.tomcatLibs = properties;
		helper.getTomcatArtifacts("7.0-SNAPSHOT");
	}

}