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
package com.googlecode.t7mp.steps.directory;

import java.io.File;

import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;

/**
 * 
 * @author jbellmann
 *
 */
public class CreateTomcatDirectoryStep implements Step {

    private final String directory;

    public CreateTomcatDirectoryStep(String directory) {
        this.directory = directory;
    }

    @Override
    public void execute(Context context) {
        final Log log = context.getLog();
        final File createdDirectory = new File(context.getMojo().getCatalinaBase(), directory);
        boolean created = createdDirectory.mkdirs();
        if (created) {
            log.debug("directory " + createdDirectory.getAbsolutePath() + " created");
        } else {
            log.warn("could not create " + createdDirectory.getAbsolutePath() + ", maybe it exist.");
        }
    }

}
