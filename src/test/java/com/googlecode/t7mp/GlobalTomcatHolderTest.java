package com.googlecode.t7mp;

import junit.framework.Assert;

import org.apache.catalina.startup.Bootstrap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bootstrap.class})
public class GlobalTomcatHolderTest {

	private Bootstrap bootstrap;
	
	@Before
	public void setUp(){
		bootstrap = PowerMockito.mock(Bootstrap.class);
	}
	
	@Test
	public void testSetBootstrap(){
		Assert.assertNull(GlobalTomcatHolder.bootstrap);
		GlobalTomcatHolder.bootstrap = bootstrap;
		Assert.assertNotNull(GlobalTomcatHolder.bootstrap);
		GlobalTomcatHolder.bootstrap = null;
		Assert.assertNull(GlobalTomcatHolder.bootstrap);
	}
	
	@Test
	public void testPrivateConstructor(){
		try {
			Invoke.privateConstructor(GlobalTomcatHolder.class);
		} catch (Exception e) {
			Assert.assertEquals("Dont call this private Constructor", e.getCause().getMessage());
		}
	}
	
}