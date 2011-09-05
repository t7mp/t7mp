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

/**
 * 
 * @author jbellmann
 *
 */
public class CopySetenvScriptStep implements Step {

    private static final String PREFIX = "setenv";

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
                    FileUtils.copyFile(scriptFile, new File(TomcatUtil.getBinDirectory(mojo.getCatalinaBase()),
                            scriptFile.getName()));
                } catch (IOException e) {
                    throw new TomcatSetupException(e.getMessage(), e);
                }
            }
        }
    }
}
