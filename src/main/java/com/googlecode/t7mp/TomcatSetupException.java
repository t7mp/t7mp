/**
 * Copyright (C) 2010 Joerg Bellmann <joerg.bellmann@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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