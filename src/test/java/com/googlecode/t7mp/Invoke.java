package com.googlecode.t7mp;

import java.lang.reflect.Constructor;

public class Invoke {
	
	public static void privateConstructor(Class<?> clazz) throws Exception {
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		for (Constructor<?> c : constructors) {
			c.setAccessible(true);
			c.newInstance(new Object[0]);
			// hier darf er gar nicht hinkommen
			throw new RuntimeException(
					"this code should never executed for private Constructors");
		}
	}

}
