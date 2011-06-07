package com.googlecode.t7mp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import com.googlecode.t7mp.TomcatSetupException;

public final class WarUnzipper {

    private WarUnzipper() {
        //hide constructor
    }

    public static void unzip(File war, File destination) {
        try {
            unzip(new FileInputStream(war), destination);
        } catch (FileNotFoundException e) {
            throw new TomcatSetupException(e.getMessage(), e);
        }
    }

    public static void unzip(InputStream warInputStream, File destination) {
        try {
            ZipArchiveInputStream in = null;
            try {
                in = new ZipArchiveInputStream(warInputStream);

                ZipArchiveEntry entry = null;
                while ((entry = in.getNextZipEntry()) != null) {
                    File outfile = new File(destination.getCanonicalPath() + "/" + entry.getName());
                    outfile.getParentFile().mkdirs();
                    if (entry.isDirectory()) {
                        outfile.mkdir();
                        entry = in.getNextZipEntry();
                        continue;
                    }
                    OutputStream o = new FileOutputStream(outfile);
                    try {
                        IOUtils.copy(in, o);
                    } finally {
                        o.close();
                    }
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
            warInputStream.close();
        } catch (FileNotFoundException e) {
            throw new TomcatSetupException(e.getMessage(), e);
        } catch (IOException e) {
            throw new TomcatSetupException(e.getMessage(), e);
        }
    }

}
