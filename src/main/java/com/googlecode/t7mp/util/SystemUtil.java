package com.googlecode.t7mp.util;

public final class SystemUtil {
	
	public static final String OS_NAME_PROPERTY = "os.name";
	
	public static boolean isWindowsSystem(){
		return (System.getProperty(OS_NAME_PROPERTY)).startsWith("Win");
	}

}
