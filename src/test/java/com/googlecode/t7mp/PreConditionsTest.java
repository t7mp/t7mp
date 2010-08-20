package com.googlecode.t7mp;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Test;
import org.mockito.Mockito;

public class PreConditionsTest {
	
	private Log log = Mockito.mock(Log.class);
	
	@Test(expected=MojoExecutionException.class)
	public void testWrongVersion() throws MojoExecutionException{
		PreConditions.checkConfiguredTomcatVersion(log, "6.x");
	}
	
	@Test
	public void testCorrectVersion() throws MojoExecutionException{
		PreConditions.checkConfiguredTomcatVersion(log, "7.x");
	}

}
