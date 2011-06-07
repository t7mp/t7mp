package com.googlecode.t7mp.steps.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.googlecode.t7mp.SetupUtil;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.util.CommonsSetupUtil;

/**
 * 
 * @author jbellmann
 *
 */
public class CopyConfigResourceFromClasspath implements Step {

    private static final String RESOURCEPATH = "/com/googlecode/t7mp/conf/";

    private SetupUtil setupUtil = new CommonsSetupUtil();

    private final String resource;

    public CopyConfigResourceFromClasspath(String resource) {
        this.resource = resource;
    }

    @Override
    public void execute(Context context) {
        final File tomcatConfDirectory = new File(context.getMojo().getCatalinaBase(), "/conf/");
        try {
            this.setupUtil.copy(getClass().getResourceAsStream(RESOURCEPATH + resource), new FileOutputStream(new File(
                    tomcatConfDirectory, resource)));
        } catch (FileNotFoundException e) {
            throw new TomcatSetupException(
                    "Could not copy classpathresource " + resource + " to tomcat-conf directory", e);
        } catch (IOException e) {
            throw new TomcatSetupException(
                    "Could not copy classpathresource " + resource + " to tomcat-conf directory", e);
        }
    }
}
