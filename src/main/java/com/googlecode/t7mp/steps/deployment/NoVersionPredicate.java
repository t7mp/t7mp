package com.googlecode.t7mp.steps.deployment;

import org.apache.maven.plugin.logging.Log;

import com.google.common.base.Predicate;
import com.googlecode.t7mp.AbstractArtifact;

class NoVersionPredicate implements Predicate<AbstractArtifact> {

    private final Log log;

    public NoVersionPredicate(Log log) {
        this.log = log;
    }

    @Override
    public boolean apply(AbstractArtifact artifact) {
        boolean apply = (artifact.getVersion() == null);
        if (apply) {
            log.debug("Artifact " + artifact.toString() + " has no version configured.");
        }
        return apply;
    }
}
