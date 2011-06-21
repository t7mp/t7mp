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
