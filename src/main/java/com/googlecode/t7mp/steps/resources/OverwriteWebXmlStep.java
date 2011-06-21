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

public class OverwriteWebXmlStep implements Step {
    
    private final SetupUtil setupUtil = new CommonsSetupUtil();

    @Override
    public void execute(Context context) {
        if ((context.getMojo().getOverwriteWebXML() == null) || (!context.getMojo().getOverwriteWebXML().exists())) {
            return;
        }
        try {
            setupUtil.copyFile(context.getMojo().getOverwriteWebXML(), new File(context.getMojo().getCatalinaBase(), "/webapps/"
                    + context.getMojo().getWebappOutputDirectory().getName() + "/WEB-INF/web.xml"));
        } catch (IOException e) {
            throw new TomcatSetupException(e.getMessage(), e);
        }
    }

}
