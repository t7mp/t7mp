package com.googlecode.t7mp;

import java.io.File;
import java.io.IOException;

import org.mockito.Mockito;

/**
 * 
 * 
 * 
 *
 */
public class SetupUtilThrowsExceptionSetup extends InsertMockAtValidationTomcatSetup {

	public SetupUtilThrowsExceptionSetup(AbstractT7Mojo t7Mojo) {
		super(t7Mojo);
	}

	@Override
	protected void validateConfiguration() throws TomcatSetupException {
		super.validateConfiguration();
		SetupUtil setupUtil = Mockito.mock(SetupUtil.class);
		try {
			Mockito.doThrow(new IOException("SETUPUTIL_COPYDIRECTORY_EXCEPTION")).when(setupUtil).copyDirectory(Mockito.any(File.class), Mockito.any(File.class));
		} catch (IOException e) {
			throw new RuntimeException("", e);
		}
		this.setupUtil = setupUtil;
	}
}