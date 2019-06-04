package com.personal.logseparator.core.impl;

import com.personal.logseparator.core.SeparatorService;
import com.personal.logseparator.model.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class SeparatorServiceImpl implements SeparatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeparatorServiceImpl.class);
    private static final char BRACKET = '[';

    @Override
    public void processLog(Log log) {
        log.setProcessKey(this.getProcessKeys(log));
        if (!log.getProcessKey().isEmpty()) {
            LOGGER.info("Process Keys found: {}", log.getProcessKey().size());
            log.setLogs(new ArrayList<>());
            this.getLogs(log);
            this.exportLog(log);
        } else {
            LOGGER.info("There is no match found.");
        }
    }

    private Set<String> getProcessKeys(Log log) {
        Set<String> processKeys = new LinkedHashSet<>();
        String line;
        try (Scanner scanner = new Scanner(new File(log.getPathInput()))) {
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                if (this.isPatternMatch(log.getSearchKey(), line)) {
                    int substring = line.indexOf(BRACKET);
                    processKeys.add(line.substring(substring, substring + 13));
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException: ", e);
        }
        if (processKeys.isEmpty()) {
            return Collections.emptySet();
        } else {
            return processKeys;
        }
    }

    private void getLogs(Log log) {
        LOGGER.info("Begin searching for all logs.");
        for (String processKey : log.getProcessKey()) {
            this.getAllProcessLogs(log, processKey);
        }
        LOGGER.info("Amount of total logs row: {}", log.getLogs().size());
    }

    private void getAllProcessLogs(Log log, String processKey) {
        LOGGER.info("Begin searching log for process key: {}", processKey);
        String line;
        try (Scanner scanner = new Scanner(new File(log.getPathInput()))) {
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                if (this.isPatternMatch(processKey, line)) {
                    log.getLogs().add(line);
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException: ", e);
        }
        LOGGER.info("End searching log for process key: {}", processKey);
    }

    private void exportLog(Log log) {
        LOGGER.info("------START EXPORTING LOGS------");
        try (FileWriter fileWriter = new FileWriter(log.getPathOutput().concat(log.getFileName().concat(".txt")))) {
            for (String s : log.getLogs()) {
                fileWriter.write(s);
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            LOGGER.error("IOException: ", e);
        }
        LOGGER.info("------END EXPORTING LOGS------");
    }

    private boolean isPatternMatch(String key, String source) {
        return Pattern.compile(Pattern.quote(key), Pattern.CASE_INSENSITIVE).matcher(source).find();
    }
}
