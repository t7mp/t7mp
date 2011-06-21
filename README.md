T7MP - Maven Plugin
====================

This Tomcat 7 Maven Plugin supports a full-featured Tomcat for testing 
(mainly integration-tests) with Maven.
It supports multiple webapps deployed on tomcat.


## News & Features ##

 * license header updated (21.06.2011)
 * new default version is 7.0.16 (21.06.2011)
 * complete refactoring (smallsteps-api) and rewrite for setup process (21.06.2011)
 
 
 * alexcojocaru supported an update to react on changed class-files, thanks to alex (17.05.2011)
 * integration tests added to test that local artifacts are resolved (17.05.2011)
 * default version for tomcat is now 7.0.14 (17.05.2011)
 * first simple implementation for scanning changed resources (use 0.9.7-SNAPSHOT, 27.04.2011) as version in your pom.mxl, please report all your issues, thanks
 * default version for tomcat is now 7.0.8
 * configure systemproperties is supported 0.9.7-SNAPSHOT (07.02.2011), try it
 * default version for tomcat is now 7.0.6 (first official release of Tomcat 7)
 * latest release is 0.9.6 (04.10.2010)
 * runs any version of tomcat 7 or 6 you cand find in maven-central
 * runs multiple webapps in the container where contextPath is configurable
 * cofigure shared libs similar to dependencies in pom.xml
 * using some best practices for configuration in server.xml
 
## Configuration Snippets ##

Change the port tomcat is listening for http connections (default is 8080). Use 
the `tomcatHttpPort` configuration element.

		<configuration>
		  <tomcatHttpPort>12345</tomcatHttpPort>
		</configuration>

Change the port tomcat is listening for shutdown (default is 8005). Use 
the `tomcatShutdownPort` configuration element.

		<configuration>
		  <tomcatShutdownPort>54321</tomcatShutdownPort>
		</configuration>

Change version of tomcat (default is 7.0.16). 
Use the `tomcatVersion` configuration element.

		<configuration>
		  <tomcatVersion>6.0.28</tomcatVersion>
		</configuration>
