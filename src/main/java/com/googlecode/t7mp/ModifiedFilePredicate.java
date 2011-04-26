package com.googlecode.t7mp;

import java.io.File;

import com.google.common.base.Predicate;

final class ModifiedFilePredicate implements Predicate<File> {

    private final long timestamp;

    ModifiedFilePredicate(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean apply(File file) {
        return (file.lastModified() >= timestamp);
    }
}
