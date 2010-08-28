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