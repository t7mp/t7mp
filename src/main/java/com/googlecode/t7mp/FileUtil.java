package com.googlecode.t7mp;

import java.io.File;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Little Helper.
 * 
 * @author jbellmann
 *
 */
public final class FileUtil {

    private FileUtil() {
        //hide constructor
    }

    public static Set<File> getAllFiles(File rootDirectory) {
        Set<File> fileSet = Sets.newHashSet();
        return getAllFiles(rootDirectory, fileSet);
    }

    private static Set<File> getAllFiles(File rootDirectory, Set<File> fileSet) {
        File[] files = rootDirectory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getAllFiles(file, fileSet);
            } else {
                fileSet.add(file);
            }
        }
        return fileSet;
    }

}
