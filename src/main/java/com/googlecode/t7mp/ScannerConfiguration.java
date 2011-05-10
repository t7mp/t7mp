package com.googlecode.t7mp;

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
