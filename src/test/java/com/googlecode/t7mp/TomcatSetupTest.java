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

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

public class TomcatSetupTest {
	
	private File catalinaBaseDir;
	private static int counter = 1;
	
	private AbstractT7Mojo t7Mojo = Mockito.mock(AbstractT7Mojo.class);
	private Log log = Mockito.mock(Log.class);
	
	protected ArtifactResolver resolver = Mockito.mock(ArtifactResolver.class);
	protected ArtifactFactory factory = Mockito.mock(ArtifactFactory.class);
	protected ArtifactRepository local = Mockito.mock(ArtifactRepository.class);
	
	@Before
	public void setUp() throws IOException{
		catalinaBaseDir = new File(new File(System.getProperty("java.io.tmpdir")), "catalinaBase_" + (++counter));
		catalinaBaseDir.mkdirs();
		// Mock Log
		Answer<Void> logAnswer = new SystemOutAnswer();
		Mockito.doAnswer(logAnswer).when(log).debug(Mockito.anyString());
		Mockito.doAnswer(logAnswer).when(log).info(Mockito.anyString());
		Mockito.doAnswer(logAnswer).when(log).warn(Mockito.anyString());
		Mockito.doAnswer(logAnswer).when(log).error(Mockito.anyString());
		Mockito.doAnswer(logAnswer).when(log).error(Mockito.anyString(), Mockito.any(Exception.class));
		Mockito.when(t7Mojo.getLog()).thenReturn(log);
		// Mock Mojo
		t7Mojo.catalinaBase = catalinaBaseDir;
		t7Mojo.resolver = resolver;
		t7Mojo.factory = factory;
		t7Mojo.local = local;
		t7Mojo.tomcatVersion = "7.0-SNAPSHOT";
		t7Mojo.webapps = new ArrayList<WebappArtifact>();
		t7Mojo.libs = new ArrayList<JarArtifact>();
	}
	
	@After
	public void tearDown() throws IOException{
		FileUtils.deleteDirectory(catalinaBaseDir);
		if(catalinaBaseDir.exists()){
			System.err.println("Could not delete directory " + catalinaBaseDir.getAbsolutePath());
		}
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testTomcatSetupNotConfigured() throws MojoExecutionException{
		TomcatSetup setup = new NotConfiguredTomcatSetup(t7Mojo);
		setup.buildTomcat();
	}
	
	@Test
	public void testFullyConfigured() throws MojoExecutionException{
		TomcatSetup setup = new FullyConfiguredTomcatSetup(t7Mojo);
		setup.buildTomcat();
	}
	
	@Test
	public void testDefaultTomcatSetup() throws MojoExecutionException{
		TomcatSetup setup = new InsertMockAtValidationTomcatSetup(t7Mojo);
		setup.buildTomcat();
	}
	
	@Test
	public void testDefaultTomcatSetupWithCopyWebapp() throws MojoExecutionException{
		File configDirectory = new File(t7Mojo.catalinaBase, "/conf/");
		t7Mojo.webappOutputDirectory = configDirectory;
		TomcatSetup setup = new InsertMockAtValidationTomcatSetup(t7Mojo);
		setup.buildTomcat();
	}
	
	@Test
	public void testDefaultTomcatSetupWithCopyWebappWithInsideLog() throws MojoExecutionException{
		File configDirectory = new File(t7Mojo.catalinaBase, "/conf/");
		t7Mojo.webappOutputDirectory = configDirectory;
		t7Mojo.lookInside = true;
		TomcatSetup setup = new InsertMockAtValidationTomcatSetup(t7Mojo);
		setup.buildTomcat();
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testDefaultTomcatSetupCopyDirectoryThrowsException() throws MojoExecutionException, IOException{
		File configDirectory = new File(t7Mojo.catalinaBase, "/conf/");
		t7Mojo.webappOutputDirectory = configDirectory;
		t7Mojo.lookInside = true;
		t7Mojo.userConfigDir = configDirectory;
		Mockito.when(t7Mojo.isWebProject()).thenReturn(true);
		Assert.assertTrue(t7Mojo.isWebProject());
		TomcatSetup setup = new SetupUtilThrowsExceptionSetup(t7Mojo);
		setup.buildTomcat();
	}
	
	@Test
	public void testWebappOutputDirectoryDoesNotExist() throws MojoExecutionException, IOException{
		File configDirectory = new File(t7Mojo.catalinaBase, "/conf/");
		File webappOutputDirectory = new File("/klaus");
		Assert.assertFalse(webappOutputDirectory.exists());
		t7Mojo.webappOutputDirectory = webappOutputDirectory;
		t7Mojo.lookInside = true;
		t7Mojo.userConfigDir = configDirectory;
		Mockito.when(t7Mojo.isWebProject()).thenReturn(true);
		Assert.assertTrue(t7Mojo.isWebProject());
		TomcatSetup setup = new SetupUtilThrowsExceptionSetup(t7Mojo);
		setup.buildTomcat();
	}
	
	@Test
	public void testWebappOutputDirectoryIsNull() throws MojoExecutionException, IOException{
		File configDirectory = new File(t7Mojo.catalinaBase, "/conf/");
		t7Mojo.webappOutputDirectory = null;
		t7Mojo.lookInside = true;
		t7Mojo.userConfigDir = configDirectory;
		Mockito.when(t7Mojo.isWebProject()).thenReturn(true);
		Assert.assertTrue(t7Mojo.isWebProject());
		TomcatSetup setup = new SetupUtilThrowsExceptionSetup(t7Mojo);
		setup.buildTomcat();
	}
	
	@Test
	public void testSuccessfullCopyWebapp() throws MojoExecutionException, IOException{
		File configDirectory = new File(t7Mojo.catalinaBase, "/conf/");
		t7Mojo.webappOutputDirectory = configDirectory;
		t7Mojo.lookInside = true;
		t7Mojo.userConfigDir = configDirectory;
		Mockito.when(t7Mojo.isWebProject()).thenReturn(true);
		Assert.assertTrue(t7Mojo.isWebProject());
		TomcatSetup setup = new SuccessfullCopyWebappMockSetup(t7Mojo);
		setup.buildTomcat();
		File webappsDirectory = new File(catalinaBaseDir, "/webapps");
		Assert.assertTrue(webappsDirectory.exists());
		File confWebapp = new File(webappsDirectory, "/conf");
		Assert.assertTrue(confWebapp.exists());
		Assert.assertTrue(confWebapp.isDirectory());
	}
	
	
	
	@Test
	public void testIsWebProject(){
		RunMojo runMojo = new RunMojo();
		Assert.assertEquals("war", runMojo.packaging);
		Assert.assertTrue(runMojo.isWebProject());
		Assert.assertFalse(runMojo.packaging.equals("jar"));
		Assert.assertFalse(runMojo.packaging.equals("pom"));
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testCopyWebappthrowsException() throws MojoExecutionException {
		Mockito.when(t7Mojo.getTomcatHttpPort()).thenReturn(8080);
		Mockito.when(t7Mojo.getTomcatShutdownPort()).thenReturn(8005);
		Mockito.when(t7Mojo.getTomcatShutdownCommand()).thenReturn("SHUTDOWN");
		TomcatSetup setup = new CopyWebappThrowsExceptionSetup(t7Mojo);
		setup.buildTomcat();
		Mockito.verify(t7Mojo, Mockito.atLeast(1)).getTomcatHttpPort();
		Mockito.verify(t7Mojo, Mockito.atLeast(1)).getTomcatShutdownPort();
		Mockito.verify(t7Mojo, Mockito.atLeast(1)).getTomcatShutdownCommand();
	}
}