package com.googlecode.t7mp;

/**
 * An ConfigurationArtifact.
 * 
 * @author jbellmann
 *
 */
public class ConfigurationArtifact extends JarArtifact {

    @Override
    public String toString() {
        return "ConfigurationArtifact[" + getArtifactCoordinates() + "]";
    }

}
