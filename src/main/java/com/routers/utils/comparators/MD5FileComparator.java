package com.routers.utils.comparators;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

public class MD5FileComparator implements FileComparator {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MD5FileComparator.class);

    @Override
    public boolean compare(File pattern, File subject) throws IOException {

        checkNotNull(pattern);
        checkNotNull(subject);

        boolean res = getFileHash(pattern).equals(getFileHash(subject));
        log.info("MD5 comparison result of files {}  and {} is {}", pattern, subject, res);

        return res;
    }

    private static String getFileHash(File file) throws IOException {
        try(FileInputStream fis = new FileInputStream(file)) {
            return md5Hex(fis);
        }
    }

}
