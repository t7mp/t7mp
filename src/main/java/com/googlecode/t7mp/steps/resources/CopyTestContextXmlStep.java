package com.googlecode.t7mp.steps.resources;

import java.io.File;
import java.io.IOException;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.SetupUtil;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.util.CommonsSetupUtil;

/**
 * Copies configured 'context.xml' to 'META-INF' directory.
 * 
 * @author jbellmann
 *
 */
public class CopyTestContextXmlStep implements Step {

    private SetupUtil setupUtil = new CommonsSetupUtil();

    @Override
    public void execute(Context context) {
        final AbstractT7Mojo mojo = context.getMojo();
        final File contextXml = mojo.getContextFile();
        if (contextXml != null && contextXml.exists()) {
            final File metaInfDirectory = new File(mojo.getCatalinaBase(), "/webapps/"
                    + mojo.getContextPath() + "/META-INF");
            metaInfDirectory.mkdirs();
            try {
                this.setupUtil.copyFile(mojo.getContextFile(), new File(metaInfDirectory, "context.xml"));
            } catch (IOException e) {
                throw new TomcatSetupException("Could not copy 'context.xml'.", e);
            }
        }
    }
}
