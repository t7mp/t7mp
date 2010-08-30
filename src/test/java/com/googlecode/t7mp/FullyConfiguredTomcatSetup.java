package com.googlecode.t7mp;

import org.apache.maven.plugin.MojoExecutionException;
import org.mockito.Mockito;

/**
 * This class should only run the configuration successfull
 * 
 *
 */
public class FullyConfiguredTomcatSetup extends AbstractTomcatSetup {

	public FullyConfiguredTomcatSetup(AbstractT7Mojo t7Mojo) {
		super(t7Mojo);
	}

	@Override
	protected void configure() throws TomcatSetupException {
		log = t7Mojo.getLog();
		artifactDescriptorReader = Mockito.mock(TomcatArtifactDescriptorReader.class);
		directorySetup = new TomcatDirectorySetup(t7Mojo.catalinaBase);
		configFilesSetup = new TomcatConfigFilesSetup(t7Mojo.catalinaBase, log, new DoNothingSetupUtil());
		libDispatcher = Mockito.mock(TomcatArtifactDispatcher.class);
	}

	@Override
	public void buildTomcat() throws MojoExecutionException {
//		super.buildTomcat();
		// This test should pass
	}
	
}
