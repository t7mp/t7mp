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

import org.apache.commons.io.FileUtils;
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
	public void tearDown() throws IOException{
		FileUtils.deleteDirectory(catalinaBaseDir);
		if(catalinaBaseDir.exists()){
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
