package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TomcatSetupTest {

	private File catalinaBaseDir;
	private static int counter = 1;
	
	protected ArtifactResolver resolver = Mockito.mock(ArtifactResolver.class);
	protected ArtifactFactory factory = Mockito.mock(ArtifactFactory.class);
	protected ArtifactRepository local = Mockito.mock(ArtifactRepository.class);
	protected AbstractT7Mojo t7Mojo = Mockito.mock(AbstractT7Mojo.class);
	
	
	protected Log log = Mockito.mock(Log.class);
	
	protected TomcatArtifactDescriptorReader artifactDescriptorReader = Mockito.mock(TomcatArtifactDescriptorReader.class);
	protected TomcatArtifactDispatcher artifactDispatcher = new TomcatArtifactDispatcher(null, catalinaBaseDir, new DoNothingSetupUtil());
	
	protected Artifact mavenArtifact = Mockito.mock(Artifact.class);
	protected JarArtifact jarArtifact = Mockito.mock(JarArtifact.class);
	
	@Before
	public void setUp() throws IOException{
		catalinaBaseDir = new File(new File(System.getProperty("java.io.tmpdir")), "catalinaBase_" + (++counter));
		catalinaBaseDir.mkdirs();
		
		Mockito.when(t7Mojo.getLog()).thenReturn(log);
		t7Mojo.resolver = resolver;
		t7Mojo.factory = factory;
		t7Mojo.local = local;
		t7Mojo.catalinaBase = catalinaBaseDir;
		t7Mojo.tomcatVersion = "7.0-SNAPSHOT";
		t7Mojo.webapps = new ArrayList<WebappArtifact>();
		t7Mojo.libs = new ArrayList<JarArtifact>();
		
		Mockito.when(jarArtifact.getArtifact()).thenReturn(mavenArtifact);
		Mockito.when(mavenArtifact.getFile()).thenReturn(File.createTempFile("mavenArtifact_", ".jar"));
		List<AbstractArtifact> artifactList = new ArrayList<AbstractArtifact>();
		artifactList.add(jarArtifact);
		Mockito.when(artifactDescriptorReader.getJarArtifacts(Mockito.anyString())).thenReturn(new ArrayList<JarArtifact>());
		
	}
	
	@After
	public void tearDown() throws IOException{
		FileUtils.deleteDirectory(catalinaBaseDir);
		if(catalinaBaseDir.exists()){
			System.err.println("Could not delete directory " + catalinaBaseDir.getAbsolutePath());
		}
	}
	
	@Test
	public void testBuildTomcat() throws MojoExecutionException {
		TestTomcatSetup setup = new TestTomcatSetup(t7Mojo);
		setup.setupUtil = new DoNothingSetupUtil();
		setup.buildTomcat();
	}
	
	@Test
	public void testBuildTomcatWithLookInsideEnabled() throws MojoExecutionException {
		TestTomcatSetup setup = new TestTomcatSetup(t7Mojo);
		t7Mojo.lookInside = true;
		setup.setupUtil = new DoNothingSetupUtil();
		setup.buildTomcat();
	}
	
	@Test
	public void testBuildTomcatWithException() {
		try{
			TestTomcatSetup setup = new TestTomcatSetup(t7Mojo);
			setup.setupUtil = new DoNothingSetupUtil();
			Mockito.doThrow(new TomcatSetupException("TESTEXCEPTION")).when(artifactDescriptorReader).getJarArtifacts(Mockito.anyString());
			setup.buildTomcat();
		}catch(MojoExecutionException e){
			Assert.assertEquals("TESTEXCEPTION", e.getMessage());
		}
	}

	@Test
	public void testCopyWebapp() throws MojoExecutionException{
		AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
		DefaultTomcatSetupZwo setup = new DefaultTomcatSetupZwo(mojo);
		setup.copyWebapp();
	}

	@Test
	public void testCopyWebappWithNotExistingWebappDirectory() throws MojoExecutionException{
		AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
		mojo.webappOutputDirectory = new File("/doesNotExist");
		DefaultTomcatSetupZwo setup = new DefaultTomcatSetupZwo(mojo);
		setup.copyWebapp();
	}
	
	@Test(expected=TomcatSetupException.class)
	public void testCopyWebappWithException() throws MojoExecutionException, IOException{
		AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
		File confDirectory = new File(catalinaBaseDir, "/conf/");
		confDirectory.mkdirs();
		mojo.webappOutputDirectory = confDirectory;
		mojo.catalinaBase = catalinaBaseDir;
		SetupUtil setupUtil = Mockito.mock(SetupUtil.class);
		Mockito.doThrow(new IOException("COPYWEBAPPEXCEPTION")).when(setupUtil).copyDirectory(Mockito.any(File.class), Mockito.any(File.class));
		DefaultTomcatSetupZwo setup = new DefaultTomcatSetupZwo(mojo);
		setup.setupUtil = setupUtil;
		setup.copyWebapp();
	}
	
	@Test
	public void testCopyWebappSuccess() throws MojoExecutionException, IOException{
		AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
		File confDirectory = new File(catalinaBaseDir, "/conf/");
		confDirectory.mkdirs();
		mojo.webappOutputDirectory = confDirectory;
		mojo.catalinaBase = catalinaBaseDir;
		DefaultTomcatSetupZwo setup = new DefaultTomcatSetupZwo(mojo);
		setup.copyWebapp();
	}

	public class TestTomcatSetup extends DefaultTomcatSetup {

		public TestTomcatSetup(AbstractT7Mojo t7Mojo) {
			super(t7Mojo);
		}

		@Override
		protected TomcatArtifactDescriptorReader getTomcatArtifactDescriptorReader() {
			return TomcatSetupTest.this.artifactDescriptorReader;
		}

		@Override
		protected MyArtifactResolver getMyArtifactResolver() {
			return Mockito.mock(MyArtifactResolver.class);
		}

		@Override
		protected TomcatArtifactDispatcher getTomcatArtifactDispatcher() {
			return TomcatSetupTest.this.artifactDispatcher;
		}
		
	}
	
	public class TestTomcatSetupZwo extends DefaultTomcatSetup {
		
		public TestTomcatSetupZwo(AbstractT7Mojo t7Mojo) {
			super(t7Mojo);
		}
		
		@Override
		protected TomcatArtifactDescriptorReader getTomcatArtifactDescriptorReader() {
			return TomcatSetupTest.this.artifactDescriptorReader;
		}
	}
	
}
