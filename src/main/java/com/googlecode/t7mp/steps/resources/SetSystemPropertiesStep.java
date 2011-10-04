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

import java.util.Map;

import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;

public class SetSystemPropertiesStep implements Step {

    @Override
    public void execute(Context context) {
        final String catalinaBasPath = context.getMojo().getCatalinaBase().getAbsolutePath();
        final Log log = context.getLog();
        System.setProperty("catalina.home", catalinaBasPath);
        log.debug("set systemproperty key: catalina.home to value " + catalinaBasPath);
        System.setProperty("catalina.base", catalinaBasPath);
        log.debug("set systemproperty key: catalina.base to value " + catalinaBasPath);
        Map<String, String> properties = context.getMojo().getSystemProperties();
	for (String key : properties.keySet()) {
	    String value = replaceCatalinas(properties.get(key));
	    System.setProperty(key, value);
            log.debug("set systemproperty key: " + key + " to value: "
                    + System.getProperty(key));
	}
    }

    protected String replaceCatalinas(String value) {
        if (value.startsWith("${catalina.home}")) {
            value = value.replace("${catalina.home}", System.getProperty("catalina.home"));
        }
        if (value.startsWith("${catalina.base}")) {
            value = value.replace("${catalina.base}", System.getProperty("catalina.base"));
        }
        return value;
    }
}
