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

public final class ZipUtil {

    private ZipUtil() {
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
