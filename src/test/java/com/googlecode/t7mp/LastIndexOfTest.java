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
