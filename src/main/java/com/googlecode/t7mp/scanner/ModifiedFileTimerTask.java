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
package com.googlecode.t7mp.scanner;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.logging.Log;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.googlecode.t7mp.util.FileUtil;

public final class ModifiedFileTimerTask extends TimerTask {

    private static final String DEF_STATIC = FilenameUtils.separatorsToSystem("src/main/webapp/");
    private static final String DEF_CLASSES = FilenameUtils.separatorsToSystem("target/classes/");
    private long lastrun = System.currentTimeMillis();
    private final File rootDirectory;
    private final File webappDirectory;
    private final List<String> suffixe;
    private final Log log;

    public ModifiedFileTimerTask(File rootDirectory, File webappDirectory, List<String> suffixe, Log log) {
        this.rootDirectory = rootDirectory;
        this.webappDirectory = webappDirectory;
        this.suffixe = suffixe;
        this.log = log;
    }

    @Override
    public void run() {
        long timeStamp = lastrun;
        lastrun = System.currentTimeMillis();
        Set<File> fileSet = FileUtil.getAllFiles(rootDirectory);
        Collection<File> changedFiles = Collections2.filter(fileSet,
                Predicates.and(new ModifiedFilePredicate(timeStamp), new FileSuffixPredicate(suffixe)));
        for (File file : changedFiles) {
            String absolutePath = file.getAbsolutePath();
            String def = getResourceDef(absolutePath);

            int endIndex = absolutePath.lastIndexOf(def);
            String copyFragment = absolutePath.substring(endIndex + def.length());
            File copyToFile = new File(webappDirectory, copyFragment);
            log.debug("CHANGED: " + absolutePath);
            log.debug("COPY TO : " + copyToFile.getAbsolutePath());
            try {
                FileUtils.copyFile(file, copyToFile);
                FileUtils.touch(copyToFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.debug("-----------END SCAN-------------");
    }

    private String getResourceDef(String absolutePath) {
        if (absolutePath.lastIndexOf(DEF_STATIC) != -1) {
            return DEF_STATIC;
        } else {
            return DEF_CLASSES;
        }
    }
}
