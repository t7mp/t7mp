package com.googlecode.t7mp.util;

import java.io.File;
import java.io.FileFilter;

/**
 * Static methods to create configured {@link FileFilter} objects.
 * 
 * @author jbellmann
 *
 */
public final class FileFilters {

    public static final String XML_SUFFIX = ".xml";
    public static final String TXT_SUFFIX = ".txt";
    public static final String PROPERTIES_SUFFIX = ".properties";
    public static final String JAVA_SUFFIX = ".java";

    public static FileFilter forAll() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile();
            }
        };
    }

    public static FileFilter forXmlFiles() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(XML_SUFFIX);
            }
        };
    }

    public static FileFilter forTxtFiles() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(TXT_SUFFIX);
            }
        };
    }

    public static FileFilter forPropertyFiles() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(PROPERTIES_SUFFIX);
            }
        };
    }

    public static FileFilter forJavaFiles() {
        return new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().endsWith(JAVA_SUFFIX);
            }
        };
    }
}
