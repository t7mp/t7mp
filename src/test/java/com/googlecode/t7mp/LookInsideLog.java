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