package com.routers.utils.comparators;

import com.google.common.io.Files;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class BitByBitComparator implements FileComparator {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BitByBitComparator.class);

    @Override
    public boolean compare(File pattern, File subject) throws IOException {

        checkNotNull(pattern);
        checkNotNull(subject);

        boolean res = Files.equal(pattern, subject);
        log.info("Bit by bit comparison result of files {}  and {} is {}", pattern, subject, res);

        return res;
    }

}
