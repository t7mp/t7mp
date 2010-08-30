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
