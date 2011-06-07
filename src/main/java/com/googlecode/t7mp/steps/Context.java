package com.googlecode.t7mp.steps;

import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.AbstractT7Mojo;

/**
 * 
 * @author jbellmann
 *
 */
public interface Context {
    
    AbstractT7Mojo getMojo();
    
    Log getLog();
    
    void put(String key, Object value);
    
    Object get(String key);

}
