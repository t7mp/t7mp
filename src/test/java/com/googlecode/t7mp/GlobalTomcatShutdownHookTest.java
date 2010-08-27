package com.googlecode.t7mp;

import org.apache.catalina.startup.Bootstrap;
import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({GlobalTomcatHolder.class,Bootstrap.class})
public class GlobalTomcatShutdownHookTest {
	
	private Bootstrap bootstrap;
	
	@Before
	public void setUp(){
		bootstrap = PowerMockito.mock(Bootstrap.class);
	}
	
	@Test
	public void testRun() throws Exception{
		GlobalTomcatHolder.bootstrap = bootstrap;
		GlobalTomcatShutdownHook hook = new GlobalTomcatShutdownHook(Mockito.mock(Log.class));
		hook.run();
		Assert.assertTrue(GlobalTomcatHolder.bootstrap == null);
		Mockito.verify(bootstrap, Mockito.atLeastOnce()).stop();
	}
	
	@Test
	public void testRunWithException() throws Exception{
		GlobalTomcatHolder.bootstrap = bootstrap;
		Mockito.doThrow(new Exception("TESTEXCEPTION")).when(bootstrap).stop();
		Log log = Mockito.mock(Log.class);
		GlobalTomcatShutdownHook hook = new GlobalTomcatShutdownHook(log);
		hook.run();
		Mockito.verify(bootstrap, Mockito.atLeastOnce()).stop();
		Mockito.verify(log, Mockito.atLeastOnce()).error(Mockito.any(Exception.class));
	}
	
	@Test
	public void testRunWithNonBootstrap(){
		GlobalTomcatHolder.bootstrap = null;
		GlobalTomcatShutdownHook hook = new GlobalTomcatShutdownHook(Mockito.mock(Log.class));
		hook.run();
		Assert.assertTrue(GlobalTomcatHolder.bootstrap == null);
	}

}
