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
package com.googlecode.t7mp.steps;

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;

import com.googlecode.t7mp.AbstractT7BaseMojo;

/**
 * 
 * @author jbellmann
 *
 */
public class DefaultContext implements Context {

    protected AbstractT7BaseMojo t7mojo;
    protected Map<String, Object> context = new HashMap<String, Object>();

    public DefaultContext(AbstractT7BaseMojo t7mojo) {
        this.t7mojo = t7mojo;
    }

    @Override
    public AbstractT7BaseMojo getMojo() {
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

    @Override
    public void clear() {
        this.context.clear();
    }

}
