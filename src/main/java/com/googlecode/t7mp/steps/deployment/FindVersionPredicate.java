package com.googlecode.t7mp.steps.deployment;

import java.util.Collection;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.googlecode.t7mp.AbstractArtifact;

final class FindVersionPredicate implements Predicate<Dependency> {

    private final Map<String, AbstractArtifact> searchMap = Maps.newHashMap();
    private final Log log;

    public FindVersionPredicate(Collection<AbstractArtifact> noVersionArtifacts, Log log) {
        this.log = log;
        for (AbstractArtifact artifact : noVersionArtifacts) {
            String key = artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getType() + ":" + artifact.getClassifier();
            log.debug("put key [" + key + "] to map for artifact [" + artifact + "]");
            searchMap.put(key, artifact);
        }
    }

    @Override
    public boolean apply(Dependency artifact) {
        String key = artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getType() + ":" + artifact.getClassifier();
        log.debug("Search with key [" + key + "] for artifact " + artifact.toString());
        if (searchMap.containsKey(key)) {
            log.debug("Found something with key [" + key + "] for artifact " + artifact.toString());
            searchMap.get(key).setVersion(artifact.getVersion());
            return true;
        }
        return false;
    }

}
