/**
 * Copyright (C) 2010 Joerg Bellmann <joerg.bellmann@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.catalina.startup.Bootstrap;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SubRunMojo.class, Bootstrap.class})
public class RunMojoTest {
	
	private Bootstrap bootstrap;
	private TomcatSetup setup;
	private File catalinaBaseDir;
	
	@Before
	public void setUp(){
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		catalinaBaseDir = new File(tempDir, "tomcat_TEST_" +  UUID.randomUUID().toString());
		Assert.assertTrue(catalinaBaseDir.mkdirs());
		bootstrap = PowerMockito.mock(Bootstrap.class);
		setup = PowerMockito.mock(TomcatSetup.class);
	}
	
	@After
	public void tearDown() throws IOException{
		FileUtils.deleteDirectory(catalinaBaseDir);
	}
	
	@Test
	public void testRunMojo() throws Exception{
		RunMojo mojo = new SubRunMojo(bootstrap, setup);
		mojo.catalinaBase = catalinaBaseDir;
		mojo.setAwait = false;
		mojo.execute();
		Mockito.verify(setup, Mockito.atLeast(1)).buildTomcat();
		Mockito.verify(bootstrap, Mockito.atLeastOnce()).start();
		Assert.assertEquals(bootstrap, mojo.getPluginContext().get(AbstractT7Mojo.T7_BOOTSTRAP_CONTEXT_ID));
	}

	@Test(expected=MojoExecutionException.class)
	public void testRunMojoSetAwaitWithException() throws Exception{
		RunMojo mojo = new SubRunMojo(bootstrap, setup);
		mojo.catalinaBase = catalinaBaseDir;
		mojo.setAwait = true;
		
		Mockito.doThrow(new Exception("TESTEXCEPTION")).when(bootstrap).start();
		
		mojo.execute();
		Mockito.verify(setup, Mockito.atLeast(1)).buildTomcat();
		Mockito.verify(bootstrap, Mockito.atLeast(1)).init();
		Mockito.verify(bootstrap, Mockito.atLeast(1)).setAwait(Mockito.anyBoolean());
		Mockito.verify(bootstrap, Mockito.atLeast(1)).start();
	}

	
	@Test
	public void testRunMojoSetAwait() throws Exception{
		RunMojo mojo = new SubRunMojo(bootstrap, setup);
		mojo.catalinaBase = catalinaBaseDir;
		mojo.setAwait = true;
		mojo.execute();
		Mockito.verify(setup, Mockito.atLeast(1)).buildTomcat();
		Mockito.verify(bootstrap, Mockito.atLeast(1)).init();
		Mockito.verify(bootstrap, Mockito.atLeast(1)).setAwait(Mockito.anyBoolean());
		Mockito.verify(bootstrap, Mockito.atLeast(1)).start();
	}

}
