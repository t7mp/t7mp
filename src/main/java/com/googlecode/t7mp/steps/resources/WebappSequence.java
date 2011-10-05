package com.googlecode.t7mp.steps.resources;

import com.googlecode.t7mp.steps.DefaultStepSequence;

/**
 * Combines all steps to prepare the webapp before starting the tomcat.
 * 
 * @author jbellmann
 *
 */
public class WebappSequence extends DefaultStepSequence {

    public WebappSequence() {
        add(new CopyProjectWebappStep());
        add(new CopyTestContextXmlStep());
        add(new OverwriteWebXmlStep());
    }
}
