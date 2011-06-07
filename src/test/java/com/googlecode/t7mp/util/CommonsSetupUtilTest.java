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

package com.googlecode.t7mp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.t7mp.SetupUtil;
import com.googlecode.t7mp.util.CommonsSetupUtil;
import com.googlecode.t7mp.util.FilesOnlyFileFilter;

public class CommonsSetupUtilTest {

    private File catalinaBaseDir;
    private static int counter = 1;

    private final static String MESSAGE = "TEST";
    private File source;
    private File target;

    @Before
    public void setUp() throws IOException {
        catalinaBaseDir = new File(new File(System.getProperty("java.io.tmpdir")), "catalinaBase_" + (++counter));
        catalinaBaseDir.mkdirs();

        source = File.createTempFile("source", ".tmp");
        source.deleteOnExit();
        target = File.createTempFile("target", ".tmp");
        target.deleteOnExit();
        FileWriter sourceWriter = new FileWriter(source);
        sourceWriter.write(MESSAGE);
        sourceWriter.close();
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(catalinaBaseDir);
        if (catalinaBaseDir.exists()) {
            System.err.println("Could not delete directory " + catalinaBaseDir.getAbsolutePath());
        }
        FileUtils.deleteQuietly(source);
        FileUtils.deleteQuietly(target);
    }

    @Test
    public void testCopy() throws IOException {
        SetupUtil setupUtil = new CommonsSetupUtil();
        setupUtil.copy(new FileInputStream(source), new FileOutputStream(target));
        checkResultFile();
    }

    @Test
    public void testCopyFile() throws IOException {
        SetupUtil setupUtil = new CommonsSetupUtil();
        setupUtil.copyFile(source, target);
        checkResultFile();
    }

    private void checkResultFile() throws IOException {
        FileReader reader = new FileReader(target);
        char[] buffer = new char[10];
        reader.read(buffer);
        reader.close();
        String result = new String(buffer);
        Assert.assertEquals(MESSAGE, result.trim());
    }

    @Test
    public void testCopyDirectory() throws IOException {
        File rootDirectory = new File(catalinaBaseDir, "root");
        Assert.assertTrue(rootDirectory.mkdir());
        File firstFile = File.createTempFile("first_", ".tmp", rootDirectory);
        File childDirectory = new File(rootDirectory, "child");
        Assert.assertTrue(childDirectory.mkdir());
        File secondFile = File.createTempFile("second_", ".tmp", childDirectory);
        File webappsDirectory = new File(catalinaBaseDir, "/webapps/");
        webappsDirectory.mkdirs();
        File targetDirectory = new File(webappsDirectory, rootDirectory.getName());
        Assert.assertTrue(targetDirectory.mkdirs());

        SetupUtil setupUtil = new CommonsSetupUtil();
        setupUtil.copyDirectory(rootDirectory, targetDirectory);

        File copiedRootDir = new File(webappsDirectory, "/root/");
        Assert.assertTrue(copiedRootDir.isDirectory());
        Assert.assertTrue(copiedRootDir.exists());
        File copiedChildDirectory = new File(copiedRootDir, "/child/");
        Assert.assertTrue(copiedChildDirectory.isDirectory());
        Assert.assertTrue(copiedChildDirectory.exists());
        File[] rootDirectoryFiles = copiedRootDir.listFiles(new FilesOnlyFileFilter());
        Assert.assertEquals(1, rootDirectoryFiles.length);
        Assert.assertTrue(rootDirectoryFiles[0].getName().startsWith("first_"));
        File[] childDirectoryFiles = copiedChildDirectory.listFiles(new FilesOnlyFileFilter());
        Assert.assertEquals(1, childDirectoryFiles.length);
        Assert.assertTrue(childDirectoryFiles[0].getName().startsWith("second_"));
    }

}
