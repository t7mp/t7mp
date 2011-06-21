/**
 * Copyright (C) 2010-2011 Joerg Bellmann <joerg.bellmann@googlemail.com>
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
