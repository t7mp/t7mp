package com.googlecode.t7mp.util;

import java.io.File;

public final class TomcatUtil {
	
	public static File getBinDirectory(File catalinaBase){
		return new File(catalinaBase, "/bin/");
	}
	
	public static File getWebappsDirectory(File catalinaBase){
		return new File(catalinaBase, "/webapps/");
	}
	
	public static File getTempDirectory(File catalinaBase){
		return new File(catalinaBase, "/temp/");
	}
	
	public static File getLibDirectory(File catalinaBase){
		return new File(catalinaBase, "/lib/");
	}
	
	public static String getStopScriptName(){
		return SystemUtil.isWindowsSystem() ? "shutdown.bat" : "./shutdown.sh";
	}
	
	public static String getStartScriptName(){
		return SystemUtil.isWindowsSystem() ? "catalina.bat" : "./catalina.sh";
	}

}
