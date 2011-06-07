package com.googlecode.t7mp.steps.resources;

import com.googlecode.t7mp.steps.DefaultStepSequence;

public class CopyConfigResourcesFromClasspathSequence extends DefaultStepSequence {

    public CopyConfigResourcesFromClasspathSequence() {
        this.add(new CopyConfigResourceFromClasspath("context.xml"));
        this.add(new CopyConfigResourceFromClasspath("catalina.policy"));
        this.add(new CopyConfigResourceFromClasspath("logging.properties"));
        this.add(new CopyConfigResourceFromClasspath("server.xml"));
        this.add(new CopyConfigResourceFromClasspath("tomcat-users.xml"));
        this.add(new CopyConfigResourceFromClasspath("web.xml"));
    }

}
