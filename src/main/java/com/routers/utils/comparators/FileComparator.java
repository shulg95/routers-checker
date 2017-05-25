package com.routers.utils.comparators;

import java.io.File;

public interface FileComparator {
    default boolean compare(File pattern, File subject) throws Exception {
        return false;
    }

    default boolean compare(File targetFile, File etalonFile, File destination) throws Exception {
        return false;
    }
}