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

import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.googlecode.t7mp.AbstractT7BaseMojo;

/**
 * 
 * @author jbellmann
 *
 */
public class DefaultContextTest {

    private AbstractT7BaseMojo mojo = Mockito.mock(AbstractT7BaseMojo.class);
    private Log log = Mockito.mock(Log.class);
    
    @Before
    public void setUp(){
        Mockito.when(mojo.getLog()).thenReturn(log);
    }
    
    @Test
    public void testDefaultContext(){
        Context context = new DefaultContext(mojo);
        context.put("TEST_KEY", "TEST_VALUE");
        Assert.assertNotNull(context.get("TEST_KEY"));
        Assert.assertEquals("TEST_VALUE",context.get("TEST_KEY"));
        Assert.assertNotNull(context.getMojo());
        Assert.assertNotNull(context.getLog());
        context.clear();
        Assert.assertNull(context.get("TEST_KEY"));
    }
    
}
