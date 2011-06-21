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
import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class ScannerConfiguration {

    private static final String DEFAULT_ENDINGS = ".html,.xhtml,.css,.js,.jsp,.jspx,.properties";
    private static final int DEFAULT_INTERVAL = 10;

    private int interval = DEFAULT_INTERVAL;
    private File rootDirectory;
    private File webappDirectory;
    private String endings = DEFAULT_ENDINGS;
    private List<String> endingsList;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public File getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public File getWebappDirectory() {
        return webappDirectory;
    }

    public void setWebappDirectory(File webappDirectory) {
        this.webappDirectory = webappDirectory;
    }

    public String getEndings() {
        return endings;
    }

    public void setEndings(String endings) {
        if ("%".equals(endings)) {
            endings = "";
        }
        this.endings = endings;
    }

    public List<String> getEndingsAsList() {
        if (endingsList == null) {
            endingsList = Lists.newArrayList(Splitter.on(",").split(endings));
        }
        return endingsList;
    }
}
