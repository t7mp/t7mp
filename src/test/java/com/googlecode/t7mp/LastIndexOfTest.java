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

import org.junit.Test;

public class LastIndexOfTest {

    private final static String absolutePath = "home/jbellmann/DEV/ECLIPSE_INST/sts_2.5_RELEASE/workspace/flow-test/src/main/webapp/index.jsp";

    @Test
    public void testLastIndexOf() {
        int lastIndexOf = absolutePath.lastIndexOf("src/main/webapp/");
        String last = absolutePath.substring(lastIndexOf + "src/main/webapp/".length());
        System.out.println(last);
        System.out.println(lastIndexOf);
    }
}
