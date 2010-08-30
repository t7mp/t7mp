package com.googlecode.t7mp;

/**
 * This class should not pass the validateConfiguration()-method
 * 
 *
 */
public class NotConfiguredTomcatSetup extends AbstractTomcatSetup {

	public NotConfiguredTomcatSetup(AbstractT7Mojo t7Mojo) {
		super(t7Mojo);
	}

	@Override
	protected void configure() throws TomcatSetupException {
		// Do Nothing here			
	}

}