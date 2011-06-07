package com.googlecode.t7mp.steps.resources;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.TomcatSetupException;
import com.googlecode.t7mp.steps.Context;
import com.googlecode.t7mp.steps.Step;

public class BuildCatalinaPropertiesFileStep implements Step {

    @Override
    public void execute(Context context) {
        final AbstractT7Mojo t7Mojo = context.getMojo();
        try {
            Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM, new LogNothingLogChute());
            Velocity.setProperty(Velocity.RESOURCE_LOADER, "class");
            Velocity.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
            Velocity.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
            Velocity.init();
            Template template = Velocity.getTemplate("com/googlecode/t7mp/conf/catalina.properties");
            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("tomcatHttpPort", t7Mojo.getTomcatHttpPort() + "");
            velocityContext.put("tomcatShutdownPort", t7Mojo.getTomcatShutdownPort() + "");
            velocityContext.put("tomcatShutdownCommand", t7Mojo.getTomcatShutdownCommand());
            Writer writer = new FileWriter(new File(t7Mojo.getCatalinaBase(), "/conf/catalina.properties"));
            template.merge(velocityContext, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new TomcatSetupException(e.getMessage(), e);
        }
    }
    
    /**
     * 
     * @author jbellmann
     *
     */
    static class LogNothingLogChute implements LogChute {

        @Override
        public void init(RuntimeServices rs) throws Exception {

        }

        @Override
        public void log(int level, String message) {

        }

        @Override
        public void log(int level, String message, Throwable t) {

        }

        @Override
        public boolean isLevelEnabled(int level) {
            return false;
        }

    }

}
