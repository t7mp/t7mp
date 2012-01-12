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

import com.googlecode.t7mp.AbstractT7BaseMojo;
import com.googlecode.t7mp.SetupUtil;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.util.CommonsSetupUtil;

/**
 * Copies configured 'context.xml' to 'META-INF' directory.
 * 
 * @author jbellmann
 *
 */
public class CopyTestContextXmlStep implements Step {

    private SetupUtil setupUtil = new CommonsSetupUtil();

    @Override
    public void execute(Context context) {
        final AbstractT7BaseMojo mojo = context.getMojo();
        final File contextXml = mojo.getContextFile();
        if (contextXml != null && contextXml.exists()) {
            final File metaInfDirectory = new File(mojo.getCatalinaBase(), "/webapps/"
                    + mojo.getContextPath() + "/META-INF");
            metaInfDirectory.mkdirs();
            try {
                this.setupUtil.copyFile(mojo.getContextFile(), new File(metaInfDirectory, "context.xml"));
            } catch (IOException e) {
                throw new TomcatSetupException("Could not copy 'context.xml'.", e);
            }
        }
    }
}
