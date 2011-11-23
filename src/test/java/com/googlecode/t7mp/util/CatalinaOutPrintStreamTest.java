/**
 * Copyright (C) 2010-2011 Joerg Bellmann <joerg.bellmann@googlemail.com>
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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CatalinaOutPrintStreamTest {

    private File originalFile;
    private File catalinaOutFile;
    private PrintStream original;
    private CatalinaOutPrintStream catalinaOutPrintStream;

    @Before
    public void setUp() throws IOException {
        originalFile = File.createTempFile("orignal_", ".temp");
        catalinaOutFile = File.createTempFile("catalinaOut_", ".temp");
        original = new PrintStream(originalFile.getAbsolutePath());
        catalinaOutPrintStream = new CatalinaOutPrintStream(original, new FileOutputStream(catalinaOutFile), false);
    }

    @After
    public void tearDown() {
        catalinaOutPrintStream.close();
        original.close();
        originalFile.delete();
        originalFile.delete();
    }

    @Test
    public void testCatalinaOutPrintStream() throws IOException {
        catalinaOutPrintStream.print("TEST");
        String content = getOriginalContent();
        String catalinaOutContent = getCatalinaOutContent();
        Assert.assertEquals(content, catalinaOutContent);
        catalinaOutPrintStream.close();
        catalinaOutPrintStream.getOriginalSystemErr().print(" AFTERCLOSE");
        Assert.assertEquals("TEST AFTERCLOSE", getOriginalContent());
    }

    private String getCatalinaOutContent() throws IOException {
        LineNumberReader lineReader = new LineNumberReader(new FileReader(catalinaOutFile));
        String line = lineReader.readLine();
        lineReader.close();
        return line;

    }

    private String getOriginalContent() throws IOException {
        LineNumberReader lineReader = new LineNumberReader(new FileReader(originalFile));
        String line = lineReader.readLine();
        lineReader.close();
        return line;
    }

}
