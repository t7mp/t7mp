package com.googlecode.t7mp;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public abstract class AbstractT7MojoTestCase extends AbstractMojoTestCase {
	
	public AbstractT7MojoTestCase(){
		super();
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		System.out.println("MOJO-TEST-CASE");
		// yourself
	}
	
	protected void tearDown() throws Exception {
		//first yourself
		super.tearDown();
	}
}