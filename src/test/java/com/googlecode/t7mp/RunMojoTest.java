package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

@Ignore
public class RunMojoTest {
	
	private ArtifactResolver resolver = Mockito.mock(ArtifactResolver.class);
	private ArtifactFactory factory = Mockito.mock(ArtifactFactory.class);
	private ArtifactRepository local = Mockito.mock(ArtifactRepository.class);
	
	private File catalinaBaseDir;
	private static int counter = 1;
	
	@Before
	public void setUp() throws IOException{
		catalinaBaseDir = new File(new File(System.getProperty("java.io.tmpdir")), "catalinaBase_" + (++counter));
		catalinaBaseDir.mkdirs();
		catalinaBaseDir.deleteOnExit();
	}
	
	@After
	public void tearDown(){
		boolean deleted = catalinaBaseDir.delete();
		if(!deleted){
			System.err.println("Could not delete directory " + catalinaBaseDir.getAbsolutePath());
		}
	}
	
	@Test
	public void testRunMojo() throws MojoExecutionException, MojoFailureException{
		RunMojo runMojo = new RunMojo();
		runMojo.resolver = resolver;
		runMojo.factory = factory;
		runMojo.local = local;
		runMojo.tomcatVersion = "7.0-SNAPSHOT";
		runMojo.catalinaBase = catalinaBaseDir;
		runMojo.execute();
	}

}
