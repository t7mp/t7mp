package com.googlecode.t7mp;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.startup.Bootstrap;

public class SubRunMojo extends RunMojo {
	
	private Map testPluginContext = new HashMap();
	
	public SubRunMojo(Bootstrap bootstrap, TomcatSetup setup){
		this.bootstrap = bootstrap;
		this.tomcatSetup = setup;
	}
	
	@Override
	protected TomcatSetup getTomcatSetup() {
		return tomcatSetup;
	}

	@Override
	protected Bootstrap getBootstrap() {
		return bootstrap;
	}

	@Override
	public Map getPluginContext() {
		return testPluginContext;
	}
}
