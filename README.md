T7MP - Maven Plugin
====================

This Tomcat 7 Maven Plugin supports a full-featured Tomcat for testing 
(mainly integration-tests) with Maven.
It supports multiple webapps deployed on tomcat.


## News & Features ##

 * first simple implementation for scanning changed resources (use 0.9.7-scanner-SNAPSHOT) as version in your pom.mxl, please report all your issues, thanks
 * default version for tomcat is now 7.0.8
 * configure systemproperties is supported 0.9.7-SNAPSHOT (07.02.2011), try it
 * default version for tomcat is now 7.0.6 (first official release of Tomcat 7)
 * latest release is 0.9.6 (04.10.2010)
 * runs any version of tomcat 7 or 6 you cand find in maven-central
 * runs multiple webapps in the container where contextPath is configurable
 * cofigure shared libs similar to dependencies in pom.xml
 * using some best practices for configuration in server.xml