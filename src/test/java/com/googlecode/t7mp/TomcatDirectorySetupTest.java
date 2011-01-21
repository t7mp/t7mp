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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TomcatDirectorySetupTest {

    private File baseDir = null;

    private List<String> expectedDirectoryNames = Arrays.asList(new String[] { "conf", "lib", "logs", "temp", "webapps", "work" });

    @Before
    public void setUp() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        baseDir = new File(tempDir, "tomcat_TEST_" + UUID.randomUUID().toString());
        Assert.assertTrue(baseDir.mkdirs());
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(baseDir);
    }

    @Test
    public void testCreateDirectory() throws MojoExecutionException {
        TomcatDirectorySetup creator = new TomcatDirectorySetup(baseDir, Mockito.mock(Log.class));
        creator.createTomcatDirectories();
        for (String directory : expectedDirectoryNames) {
            Assert.assertTrue(checkExist(directory));
        }
    }

    private boolean checkExist(String directoryName) {
        File targetDirectory = new File(baseDir, "/" + directoryName + "/");
        return targetDirectory.exists();
    }

    @Test(expected = TomcatSetupException.class)
    public void testBaseDirNotExist() {
        TomcatDirectorySetup creator = new TomcatDirectorySetup(new File("/peter"), Mockito.mock(Log.class));
        creator.createTomcatDirectories();
    }

    @Test(expected = TomcatSetupException.class)
    public void testTomcatDirectoryCouldNotBeCreated() {
        TomcatDirectorySetup creator = new TomcatDirectorySetup(new File("/peter"), Mockito.mock(Log.class));
        creator.createTomcatDirectory("test");
    }

    /**
     * This test is only to get a 100% Branch-Coverage
     * 
     * @throws IOException
     */
    @Test(expected = Exception.class)
    public void testTomcatDirectoryIsNotADirectory() throws IOException {
        File file = Mockito.mock(File.class);
        Mockito.when(file.exists()).thenReturn(false);
        Mockito.when(file.mkdirs()).thenReturn(true);
        TomcatDirectorySetup setup = new TomcatDirectorySetup(file, Mockito.mock(Log.class));
        setup.createTomcatDirectories();
    }

}
