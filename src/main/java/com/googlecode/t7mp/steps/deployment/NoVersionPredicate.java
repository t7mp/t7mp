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
