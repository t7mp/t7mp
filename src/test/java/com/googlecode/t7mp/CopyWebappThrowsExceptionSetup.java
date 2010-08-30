package com.googlecode.t7mp;

public class CopyWebappThrowsExceptionSetup extends InsertMockAtValidationTomcatSetup {

	public CopyWebappThrowsExceptionSetup(AbstractT7Mojo t7Mojo) {
		super(t7Mojo);
	}

	@Override
	protected void copyWebapp() throws TomcatSetupException {
		throw new TomcatSetupException("This Exception was called in copyWebapp for testing.");
	}
}