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

import java.util.ArrayList;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.catalina.startup.Bootstrap;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * This Mojo uses the plugin context to get a reference to shutdown.
 * 
 * @goal stop
 *
 */
public final class StopMojo extends AbstractMojo {
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Bootstrap bootstrap = (Bootstrap) getPluginContext().get(AbstractT7Mojo.T7_BOOTSTRAP_CONTEXT_ID);
        getPluginContext().remove(AbstractT7Mojo.T7_BOOTSTRAP_CONTEXT_ID);
        if (bootstrap != null) {
            try {
                bootstrap.stop();
                cleanupMBeanServer();
            } catch (Exception e) {
                throw new MojoExecutionException("Error stopping the Tomcat with Bootstrap from Plugin-Context", e);
            }
        }
    }

    /*
     * On shutdown not all MBean will be unregistered. That causes a problem if the Tomcat must
     * start more than once in the same VM. The bootstrap.start() breaks.
     */
    private void cleanupMBeanServer() throws MalformedObjectNameException,
            MBeanRegistrationException, InstanceNotFoundException {
        ArrayList<MBeanServer> servers = MBeanServerFactory.findMBeanServer(null);
        for (MBeanServer server : servers) {
            ObjectName naming = new ObjectName("Catalina:*");
            for (ObjectInstance object : server.queryMBeans(naming, null)) {
                server.unregisterMBean(object.getObjectName());
            }
        }
    }
}
