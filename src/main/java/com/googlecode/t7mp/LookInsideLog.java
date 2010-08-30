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

import org.apache.maven.plugin.logging.Log;

public class LookInsideLog implements Log {
	
	private final Log realLog;

	public LookInsideLog(Log realLog){
		this.realLog = realLog;
	}
	
	@Override
	public void debug(CharSequence arg0) {
		if(realLog.isDebugEnabled()){
			realLog.debug(arg0);
		}else{
			realLog.info(arg0);
		}
	}

	@Override
	public void debug(Throwable arg0) {
		if(realLog.isDebugEnabled()){
			realLog.debug(arg0);
		}else{
			realLog.info(arg0);
		}
	}

	@Override
	public void debug(CharSequence arg0, Throwable arg1) {
		if(realLog.isDebugEnabled()){
			realLog.debug(arg0,arg1);
		}else{
			realLog.info(arg0,arg1);
		}
	}

	@Override
	public void error(CharSequence arg0) {
		realLog.error(arg0);

	}

	@Override
	public void error(Throwable arg0) {
		realLog.error(arg0);
	}

	@Override
	public void error(CharSequence arg0, Throwable arg1) {
		realLog.error(arg0, arg1);

	}

	@Override
	public void info(CharSequence arg0) {
		realLog.info(arg0);
	}

	@Override
	public void info(Throwable arg0) {
		realLog.info(arg0);

	}

	@Override
	public void info(CharSequence arg0, Throwable arg1) {
		realLog.info(arg0, arg1);

	}

	@Override
	public boolean isDebugEnabled() {
		return realLog.isDebugEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return realLog.isErrorEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return realLog.isInfoEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return realLog.isWarnEnabled();
	}

	@Override
	public void warn(CharSequence arg0) {
		realLog.warn(arg0);
	}

	@Override
	public void warn(Throwable arg0) {
		realLog.warn(arg0);
	}

	@Override
	public void warn(CharSequence arg0, Throwable arg1) {
		realLog.warn(arg0, arg1);
	}

}