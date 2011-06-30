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

import java.util.Collection;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.googlecode.t7mp.AbstractArtifact;
import com.googlecode.t7mp.JarArtifact;

public class FindVersionPredicateTest {
    
    private Log log = Mockito.mock(Log.class);
    
    @Test
    public void findVersionPredicateTest(){
        FindVersionPredicate predicate = new FindVersionPredicate(getLibs(), log);
        Collection<Dependency> deps = Collections2.filter(getDependencies(), predicate);
        Assert.assertNotNull(deps);
        Assert.assertFalse(deps.isEmpty());
    }
    
    private List<Dependency> getDependencies(){
        List<Dependency> result = Lists.newArrayList();
        Dependency dep = new Dependency();
        dep.setGroupId("commons-logging");
        dep.setArtifactId("commons-logging");
        dep.setVersion("1.1.1");
        dep.setClassifier( "sources" );
        result.add(dep);
        return result;
    }
    
    private List<AbstractArtifact> getLibs(){
        List<AbstractArtifact> result = Lists.newArrayList();
        JarArtifact jar = new JarArtifact();
        jar.setArtifactId("commons-logging");
        jar.setGroupId("commons-logging");
        jar.setClassifier( "sources" );
        jar.setType("jar");
        result.add(jar);
        return result;
    }

}
