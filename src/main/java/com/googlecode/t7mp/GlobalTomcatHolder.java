package com.googlecode.t7mp;

import org.apache.catalina.startup.Bootstrap;

/**
 * Holds a <i>Bootstrap</i>-instance for shutting down.
 */
public final class GlobalTomcatHolder {
	
	private GlobalTomcatHolder(){
		throw new RuntimeException("Dont call this private Constructor");
	}
	
	public static Bootstrap bootstrap;

}