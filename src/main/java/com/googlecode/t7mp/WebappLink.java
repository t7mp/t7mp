package com.googlecode.t7mp;

/**
 * TODO Comment.
 * 
 * @author jbellmann
 *
 */
public final class WebappLink {

    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_ENGINE = "Catalina";

    private String host = DEFAULT_HOST;
    private String engine = DEFAULT_ENGINE;
    private String contextPath = "";
    private String documentPath = "";

    private WebappLink() {
        //
    }

}
