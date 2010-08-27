package com.googlecode.t7mp;

import junit.framework.Assert;

import org.apache.catalina.startup.Bootstrap;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bootstrap.class})
public class StopMojoTest {
	
	private Bootstrap bootstrap;
	
	@Before
	public void setUp(){
		bootstrap = PowerMockito.mock(Bootstrap.class);
	}
	
	@Test
	public void testExecute() throws Exception{
		Assert.fail("muss neu implementiert werden");
		StopMojo mojo = new StopMojo();
		mojo.execute();
		Mockito.verify(bootstrap, Mockito.atLeastOnce()).stop();
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testExecuteWithException() throws Exception{
		Assert.fail("muss neu implementiert werden");
		Mockito.doThrow(new Exception("TESTEXCEPTION")).when(bootstrap).stop();
		StopMojo mojo = new StopMojo();
		mojo.execute();
		Mockito.verify(bootstrap, Mockito.atLeastOnce()).stop();
	}

}
