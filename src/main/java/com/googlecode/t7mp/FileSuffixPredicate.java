package com.googlecode.t7mp;

import java.io.File;
import java.util.List;

import com.google.common.base.Predicate;

final class FileSuffixPredicate implements Predicate<File> {

    private final List<String> suffixe;

    FileSuffixPredicate(List<String> suffixe) {
        this.suffixe = suffixe;
    }

    @Override
    public boolean apply(File input) {
        final String path = input.getAbsolutePath();
        for (String suffix : suffixe) {
            if (path.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
