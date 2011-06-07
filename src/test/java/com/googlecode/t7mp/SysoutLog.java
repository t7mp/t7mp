package com.googlecode.t7mp;

import org.apache.maven.plugin.logging.Log;

public class SysoutLog implements Log {

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void debug(CharSequence content) {
        System.out.println(content);
    }

    @Override
    public void debug(CharSequence content, Throwable error) {
        System.out.println(content);
        System.out.println(error.getMessage());
    }

    @Override
    public void debug(Throwable error) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isInfoEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void info(CharSequence content) {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(CharSequence content, Throwable error) {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(Throwable error) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isWarnEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void warn(CharSequence content) {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(CharSequence content, Throwable error) {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(Throwable error) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isErrorEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void error(CharSequence content) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(CharSequence content, Throwable error) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(Throwable error) {
        // TODO Auto-generated method stub

    }

}
