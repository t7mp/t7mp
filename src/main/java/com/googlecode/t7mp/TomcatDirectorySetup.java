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

package com.googlecode.t7mp;

import java.io.File;

import org.apache.maven.plugin.logging.Log;

/**
 * TODO Comment.
 * @author jbellmann
 *
 */
public class TomcatDirectorySetup {

    private final File catalinaBaseDir;
    private final Log log;

    public TomcatDirectorySetup(File catalinaBaseDir, Log log) {
        this.catalinaBaseDir = catalinaBaseDir;
        this.log = log;
    }

    public void createTomcatDirectories() {
        if (!catalinaBaseDir.exists() && !catalinaBaseDir.mkdirs()) {
            throw new TomcatSetupException("could not create 'catalina.base' on " + catalinaBaseDir.getAbsolutePath());
        }
        createTomcatDirectory("conf");
        createTomcatDirectory("webapps");
        createTomcatDirectory("lib");
        createTomcatDirectory("temp");
        createTomcatDirectory("work");
        createTomcatDirectory("logs");
    }

    protected void createTomcatDirectory(String name) {
        File directory = new File(catalinaBaseDir, name);
        log.debug("Try to create directory " + directory.getAbsolutePath());
        if (!directory.exists() && !directory.mkdirs()) {
            throw new TomcatSetupException("could not create '" + name + "' on " + directory.getAbsolutePath());
        }
    }

}
