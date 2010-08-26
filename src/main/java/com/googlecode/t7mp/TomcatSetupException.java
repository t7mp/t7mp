package com.googlecode.t7mp;


/**
 * 
 * 
 *
 */
public class TomcatSetupException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NOT_NULL = " should not be null";
	
	private static final String NOT_EMPTY = " should not be empty";

	public TomcatSetupException(){
		super();
	}
	
	public TomcatSetupException(String message){
		super(message);
	}
	
	public TomcatSetupException(Throwable cause){
		super(cause.getMessage(), cause);
	}
	
	public TomcatSetupException(String message, Throwable cause){
		super(message, cause);
	}
	
	public static void notNull(Object arg, String name) throws TomcatSetupException {
		if(arg == null){
			throw new TomcatSetupException(name + NOT_NULL);
		}
	}
	
	public static void notEmpty(String string, String name) throws TomcatSetupException {
		if(string.isEmpty()){
			throw new TomcatSetupException(name + NOT_EMPTY);
		}
	}
}