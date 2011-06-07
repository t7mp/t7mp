package com.googlecode.t7mp.steps.resources;

import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.SystemProperty;
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
        for (SystemProperty property : context.getMojo().getSystemProperties()) {
            String value = replaceCatalinas(property.getValue());
            System.setProperty(property.getKey(), value);
            log.debug("set systemproperty key: " + property.getKey() + " to value: "
                    + System.getProperty(property.getKey()));
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
