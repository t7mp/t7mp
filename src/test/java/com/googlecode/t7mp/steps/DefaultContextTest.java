package com.googlecode.t7mp.steps;

import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.googlecode.t7mp.AbstractT7Mojo;

/**
 * 
 * @author jbellmann
 *
 */
public class DefaultContextTest {

    private AbstractT7Mojo mojo = Mockito.mock(AbstractT7Mojo.class);
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
