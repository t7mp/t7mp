package com.googlecode.t7mp;

import org.apache.catalina.startup.Bootstrap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bootstrap.class})
public class TomcatShutdownHookTest {
	
	private Bootstrap bootstrap;
	
	@Before
	public void setUp(){
		bootstrap = PowerMockito.mock(Bootstrap.class);
	}
	
	@Test
	public void testRun() throws Exception{
		TomcatShutdownHook hook = new TomcatShutdownHook(bootstrap);
		hook.run();
		Mockito.verify(bootstrap, Mockito.atLeast(1)).stop();
	}
	
	@Test
	public void testRunWithNullArgument(){
		TomcatShutdownHook hook = new TomcatShutdownHook(null);
		hook.run();
	}
	
	@Test
	public void testRunWithException() throws Exception{
		Mockito.doThrow(new Exception("TESTEXCEPTION")).when(bootstrap).stop();
		TomcatShutdownHook hook = new TomcatShutdownHook(bootstrap);
		hook.run();
	}

}
