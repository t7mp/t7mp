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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

public class TomcatSetupTestZwo {
	
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
		t7Mojo.catalinaBase = catalinaBaseDir;
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
		TomcatSetup setup = new SetupUtilThrowsExceptionSetup(t7Mojo);
		setup.buildTomcat();
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testCopyWebappthrowsException() throws MojoExecutionException {
		TomcatSetup setup = new CopyWebappThrowsExceptionSetup(t7Mojo);
		setup.buildTomcat();
	}
}