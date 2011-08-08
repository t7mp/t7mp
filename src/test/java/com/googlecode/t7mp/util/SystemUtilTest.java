package com.googlecode.t7mp.util;

import org.junit.Assert;
import org.junit.Test;

public class SystemUtilTest {
	
	@Test
	public void testSystemUtil(){
		Assert.assertFalse(SystemUtil.isWindowsSystem());
	}

}
