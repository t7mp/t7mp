package com.googlecode.t7mp;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class SystemOutAnswer implements Answer<Void> {

	@Override
	public Void answer(InvocationOnMock invocation) throws Throwable {
		String message = (String)invocation.getArguments()[0];
		System.out.println(message);
		return null;
	}
}