package com.googlecode.t7mp.steps;

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.AbstractT7Mojo;

/**
 * 
 * @author jbellmann
 *
 */
public class DefaultContext implements Context {

    protected AbstractT7Mojo t7mojo;
    protected Map<String, Object> context = new HashMap<String, Object>();

    public DefaultContext(AbstractT7Mojo t7mojo) {
        this.t7mojo = t7mojo;
    }

    @Override
    public AbstractT7Mojo getMojo() {
        return this.t7mojo;
    }

    @Override
    public Log getLog() {
        return this.t7mojo.getLog();
    }

    @Override
    public void put(String key, Object value) {
        this.context.put(key, value);
    }

    @Override
    public Object get(String key) {
        return this.context.get(key);
    }

}
