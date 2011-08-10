package com.googlecode.t7mp.util;

import org.apache.commons.lang.SystemUtils;
import org.junit.Assert;
import org.junit.Test;

public class SystemUtilTest {
        
	@Test
	public void testSystemUtil(){
            Assert.assertEquals(SystemUtils.IS_OS_WINDOWS, SystemUtil.isWindowsSystem());
	}

}
