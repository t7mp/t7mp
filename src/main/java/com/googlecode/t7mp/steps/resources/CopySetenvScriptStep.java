package com.googlecode.t7mp.steps.resources;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.util.TomcatUtil;

public class CopySetenvScriptStep implements Step {

    private final String PREFIX = "setenv";

    @Override
    public void execute(Context context) {
        AbstractT7Mojo mojo = context.getMojo();
        File tomcatConfigDirectory = mojo.getTomcatConfigDirectory();
        if (tomcatConfigDirectory == null || !tomcatConfigDirectory.exists()) {
            return;
        }
        File tomcatDirectory = tomcatConfigDirectory.getParentFile();
        File tomcatBinDirectory = new File(tomcatDirectory, "/bin/");
        File[] setEnvFiles = tomcatBinDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().startsWith(PREFIX));
            }
        });
        // listFiles returns null if binDirectory does not exist
        // https://github.com/t7mp/t7mp/issues/28
        if (setEnvFiles != null) {
            for (File scriptFile : setEnvFiles) {
                try {
                    FileUtils.copyFile(scriptFile, new File(TomcatUtil.getBinDirectory(mojo.getCatalinaBase()), scriptFile.getName()));
                } catch (IOException e) {
                    throw new TomcatSetupException(e.getMessage(), e);
                }
            }
        }
    }
}
