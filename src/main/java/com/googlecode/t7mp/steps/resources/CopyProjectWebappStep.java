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
import java.io.IOException;

import com.googlecode.t7mp.SetupUtil;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.util.CommonsSetupUtil;

public class CopyProjectWebappStep implements Step {

    private SetupUtil setupUtil = new CommonsSetupUtil();
    
    @Override
    public void execute(Context context) {
        if (!context.getMojo().isWebProject()) {
            return;
        }
        if ((context.getMojo().getWebappOutputDirectory() == null) || (!context.getMojo().getWebappOutputDirectory().exists())) {
            return;
        }
        try {
            this.setupUtil.copyDirectory(context.getMojo().getWebappOutputDirectory(), new File(context.getMojo().getCatalinaBase(), "/webapps/"
                    + context.getMojo().getWebappOutputDirectory().getName()));
            if (context.getMojo().getContextFile() != null) {
                if (context.getMojo().getContextFile().exists()) {
                    final File metaInfDirectory = new File(context.getMojo().getCatalinaBase(), "/webapps/"
                            + context.getMojo().getWebappOutputDirectory().getName() + "/META-INF");
                    metaInfDirectory.mkdirs();
                    this.setupUtil.copyFile(context.getMojo().getContextFile(), new File(metaInfDirectory, "context.xml"));
                }
            }
        } catch (IOException e) {
            throw new TomcatSetupException(e.getMessage(), e);
        }
    }

}
