package com.googlecode.t7mp.steps.resources;

import java.io.File;
import java.io.IOException;

import com.googlecode.t7mp.SetupUtil;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;
import com.googlecode.t7mp.util.CommonsSetupUtil;

public class OverwriteWebXmlStep implements Step {
    
    private final SetupUtil setupUtil = new CommonsSetupUtil();

    @Override
    public void execute(Context context) {
        if ((context.getMojo().getOverwriteWebXML() == null) || (!context.getMojo().getOverwriteWebXML().exists())) {
            return;
        }
        try {
            setupUtil.copyFile(context.getMojo().getOverwriteWebXML(), new File(context.getMojo().getCatalinaBase(), "/webapps/"
                    + context.getMojo().getWebappOutputDirectory().getName() + "/WEB-INF/web.xml"));
        } catch (IOException e) {
            throw new TomcatSetupException(e.getMessage(), e);
        }
    }

}
