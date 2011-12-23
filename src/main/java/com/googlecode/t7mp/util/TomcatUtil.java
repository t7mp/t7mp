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
package com.googlecode.t7mp.util;

import java.io.File;

/**
 * 
 * @author jbellmann
 *
 */
public final class TomcatUtil {

    private TomcatUtil() {
        //hide constructor
    }

    public static File getBinDirectory(File catalinaBase) {
        return new File(catalinaBase, "/bin/");
    }

    public static File getWebappsDirectory(File catalinaBase) {
        return new File(catalinaBase, "/webapps/");
    }

    public static File getTempDirectory(File catalinaBase) {
        return new File(catalinaBase, "/temp/");
    }

    public static File getLibDirectory(File catalinaBase) {
        return new File(catalinaBase, "/lib/");
    }

    public static String getStopScriptName() {
        if (SystemUtil.isWindowsSystem()) {
            return "shutdown.bat";
        } else {
            return "./shutdown.sh";
        }
    }

    public static String getStartScriptName() {
        if (SystemUtil.isWindowsSystem()) {
            return "cmd catalina.bat";
        } else {
            return "./catalina.sh";
        }
    }

}
