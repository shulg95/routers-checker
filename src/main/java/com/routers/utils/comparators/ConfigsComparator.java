package com.routers.utils.comparators;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConfigsComparator implements FileComparator {

    private static final Logger log = LoggerFactory.getLogger(ConfigsComparator.class);

    @Override
    public boolean compare(File targetFile, File etalonFile, File diffDestination) throws IOException {
        checkNotNull(targetFile);
        checkNotNull(etalonFile);

        List<String> targetLines = Files.readLines(targetFile, Charset.defaultCharset())
                .stream()
                .filter(s -> !s.equals("!"))
                .collect(Collectors.toList());
        List<String> etalonLines = Files.readLines(etalonFile, Charset.defaultCharset())
                .stream()
                .filter(s -> !s.equals("!"))
                .collect(Collectors.toList());

        List<String> addedLines = Lists.newArrayList(targetLines);
        addedLines.removeAll(etalonLines);

        List<String> removedLines = Lists.newArrayList(etalonLines);
        removedLines.removeAll(targetLines);

        if(addedLines.isEmpty() && removedLines.isEmpty()) {
            log.info("Configurations comparison result of files {}  and {} is true", targetFile, etalonFile);
            return true;
        }

        FileWriter writer = new FileWriter(diffDestination);
        List<String> logicalBlock = Lists.newArrayList();

        if (!addedLines.isEmpty()) {
            writer.write("ADDED LINES: \n");
            for(String addedLine: addedLines) {
                if(addedLine.startsWith(" ")) {
                    if (logicalBlock.contains(addedLine)) {
                        continue;
                    }
                    int index = targetLines.indexOf(addedLine);
                    List<String> follwingSubList = targetLines.subList(index, targetLines.size());
                    List<String> reversedPrevSubList = Lists.reverse(targetLines.subList(0, index));

                    int blockStartIndex = 0;
                    for(String str: reversedPrevSubList) {
                        if(!str.startsWith(" ")) {
                            blockStartIndex = targetLines.indexOf(str);
                            break;
                        }
                    }

                    int blockEndIndex = 0;
                    for(String str: follwingSubList) {
                        if(!str.startsWith(" ")) {
                            blockEndIndex = targetLines.indexOf(str);
                            break;
                        }
                    }

                    logicalBlock = targetLines.subList(blockStartIndex, blockEndIndex);
                    for(String str: logicalBlock) {
                        if(addedLines.contains(str)){
                            writer.write("++ ");
                        } else {
                            writer.write("   ");
                        }
                        writer.write(str);
                        writer.write("\n");
                    }
                } else if (!addedLines.get(addedLines.indexOf(addedLine) + 1).startsWith(" ")) {
                    writer.write("++ " + addedLine);
                    writer.write("\n");
                }
            }
        }

        if(!removedLines.isEmpty()) {
            writer.write("\n\nREMOVED LINES: \n");
            for(String removedLine: removedLines) {
                if(removedLine.startsWith(" ")) {
                    if (logicalBlock.contains(removedLine)) {
                        continue;
                    }
                    int index = etalonLines.indexOf(removedLine);
                    List<String> follwingSubList = etalonLines.subList(index, etalonLines.size());
                    List<String> reversedPrevSubList = Lists.reverse(etalonLines.subList(0, index));

                    int blockStartIndex = 0;
                    for(String str: reversedPrevSubList) {
                        if(!str.startsWith(" ")) {
                            blockStartIndex = etalonLines.indexOf(str);
                            break;
                        }
                    }

                    int blockEndIndex = 0;
                    for(String str: follwingSubList) {
                        if(!str.startsWith(" ")) {
                            blockEndIndex = etalonLines.indexOf(str);
                            break;
                        }
                    }

                    logicalBlock = etalonLines.subList(blockStartIndex, blockEndIndex);
                    for(String str: logicalBlock) {
                        if(removedLines.contains(str)){
                            writer.write("-- ");
                        } else {
                            writer.write("   ");
                        }
                        writer.write(str);
                        writer.write("\n");
                    }
                } else if (!removedLines.get(removedLines.indexOf(removedLine) + 1).startsWith(" ")) {
                    writer.write("-- " + removedLine);
                    writer.write("\n");
                }
            }
        }
        writer.close();

        log.info("Configurations comparison result of files {}  and {} is false", targetFile, etalonFile);
        return false;
    }
}