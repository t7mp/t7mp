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
