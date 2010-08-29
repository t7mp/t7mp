package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

@Ignore
public class TomcatSetupTest {

	private File catalinaBaseDir;
	private static int counter = 1;
	
	private ArtifactResolver resolver = Mockito.mock(ArtifactResolver.class);
	private ArtifactFactory factory = Mockito.mock(ArtifactFactory.class);
	private ArtifactRepository local = Mockito.mock(ArtifactRepository.class);
	private AbstractT7Mojo t7Mojo = Mockito.mock(AbstractT7Mojo.class);
	private Log log = Mockito.mock(Log.class);
	private TomcatArtifactDescriptorReader artifactDescriptorReader = Mockito.mock(TomcatArtifactDescriptorReader.class);
	private TomcatArtifactDispatcher artifactDispatcher = new TomcatArtifactDispatcher(null, catalinaBaseDir, new DoNothingSetupUtil());
	private Artifact mavenArtifact = Mockito.mock(Artifact.class);
	private JarArtifact jarArtifact = Mockito.mock(JarArtifact.class);
	
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
		Mockito.when(jarArtifact.getArtifact()).thenReturn(mavenArtifact);
		Mockito.when(mavenArtifact.getFile()).thenReturn(File.createTempFile("mavenArtifact_", ".jar"));
		List<AbstractArtifact> artifactList = new ArrayList<AbstractArtifact>();
		artifactList.add(jarArtifact);
		Mockito.when(artifactDescriptorReader.getJarArtifacts(Mockito.anyString())).thenReturn(new ArrayList<JarArtifact>());
		artifactDispatcher.resolvedArtifacts = artifactList;
	}
	
	@After
	public void tearDown() throws IOException{
		FileUtils.deleteDirectory(catalinaBaseDir);
		if(catalinaBaseDir.exists()){
			System.err.println("Could not delete directory " + catalinaBaseDir.getAbsolutePath());
		}
	}
	
	@Test
	public void testSetup() throws MojoExecutionException, MojoFailureException{
		DefaultTomcatSetup setup = new DefaultTomcatSetup(t7Mojo);
		setup.setupUtil = new DoNothingSetupUtil();
		setup.t7Mojo = t7Mojo;
		setup.artifactDescriptorReader = artifactDescriptorReader;
		setup.libDispatcher = artifactDispatcher;
		setup.buildTomcat();
	}
	
}
