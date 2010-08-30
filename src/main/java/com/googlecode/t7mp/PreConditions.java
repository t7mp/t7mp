/**
 * Copyright (C) 2010 Joerg Bellmann <joerg.bellmann@googlemail.com>
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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

class PreConditions {
	
	private PreConditions(){
		throw new RuntimeException("Dont call this private constructor");
	}
	
	static void checkConfiguredTomcatVersion(Log log, String tomcatVersion) throws MojoExecutionException{
		if(!tomcatVersion.startsWith("7.") && !tomcatVersion.startsWith("6.")){
			log.info("");
			log.info("");
			log.error("======================= MAVEN-T7-PLUGIN ==================");
			log.error("This plugin supports only Version 7 or 6 of Tomcat. You configured: " + tomcatVersion + ". Cancel the Build.");
			log.error("===========================================================");
			log.info("");
			log.info("");
			throw new MojoExecutionException("This plugin supports only Version 7 or 6 of Tomcat. You configured " + tomcatVersion);
		}
	}
}