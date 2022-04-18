package com.seo.auto.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Swaroop Pallapothu on 26 Jan, 2020
 */
public class CSVFilesJoiner {

    private static final Logger LOGGER = Logger.getLogger(CSVFilesJoiner.class);

    private final List<String> INPUTFILES;
    private final String OUTPUT_FILE;

    public CSVFilesJoiner(List<String> INPUT_FILES, String OUTPUT_FILE) {
        this.INPUTFILES = INPUT_FILES;
        this.OUTPUT_FILE = OUTPUT_FILE;
    }

    public boolean join() {
        boolean joined = false;
        if (CollectionUtils.isEmpty(INPUTFILES)) {
            return joined;
        }

        try {

            if (!Files.exists(Paths.get(OUTPUT_FILE))) {
                Files.createFile(Paths.get(OUTPUT_FILE));
            }

            AtomicInteger linesCount = new AtomicInteger(0);
            INPUTFILES.forEach((filePath) -> {
                try {
                    if (!Files.exists(Paths.get(filePath))) {
                        return;
                    }

                    List<String> lines = Files.readAllLines(Paths.get(filePath));
                    if (CollectionUtils.isEmpty(lines)) {
                        return;
                    }
                    int startIndex = 1;
                    if (linesCount.incrementAndGet() == 1) {
                        startIndex = 0;
                    }

                    Files.write(Paths.get(OUTPUT_FILE), lines.subList(startIndex, lines.size()), StandardOpenOption.APPEND);

                } catch (Exception e) {
                    LOGGER.error("Exception while joining file: " + filePath, e);
                }

            });
            joined = true;

        } catch (Exception e) {
            joined = false;
            LOGGER.error(e);
        }

        return joined;
    }

}
