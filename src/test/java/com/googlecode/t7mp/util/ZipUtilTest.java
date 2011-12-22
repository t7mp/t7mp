package com.googlecode.t7mp.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jbellmann
 *
 */
public class ZipUtilTest {

    private File unpackDirectory = null;

    @Before
    public void setUp() {
        unpackDirectory = getUnpackDirectory();
    }

    @After
    public void tearDown() {
        try {
            FileUtils.deleteDirectory(unpackDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Could not delete unpackDirectory " + unpackDirectory.getAbsolutePath());
        }
    }

    @Test
    public void testUnzipJar() {
        InputStream jarInputStream = getClass().getResourceAsStream("tomcatconfig-0.0.1-SNAPSHOT.jar");
        ZipUtil.unzip(jarInputStream, unpackDirectory);
        Set<File> fileSet = FileUtil.getAllFiles(unpackDirectory, FileFilters.forAll(), false);
        Assert.assertEquals("Sollte eine Datei drin sein.", 1, fileSet.size());
    }

    protected File getUnpackDirectory() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File upackDirectory = new File(tempDir, UUID.randomUUID().toString());
        upackDirectory.mkdirs();
        return upackDirectory;
    }
}
