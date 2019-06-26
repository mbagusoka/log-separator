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
    private static final String TXT = ".txt";

    @Override
    public void processLog(Log log) {
        log.setProcessKeyMap(this.getProcessKeyMap(log));
        if (!log.getProcessKeyMap().isEmpty()) {
            LOGGER.info("Process Keys found: {}", this.getTotalProcess(log.getProcessKeyMap()));
            log.setLogs(this.getLogs(log));
            this.exportLog(log);
        } else {
            LOGGER.info("There is no match found.");
        }
    }

    private Map<String, Set<String>> getProcessKeyMap(Log log) {
        Map<String, Set<String>> processKeysMap = new LinkedHashMap<>();
        Set<String> processKeys;
        for (String pathInput : log.getPathInputs()) {
            processKeys = this.getProcessKey(pathInput, log.getSearchKey());
            if (!processKeys.isEmpty()) {
                processKeysMap.put(pathInput, processKeys);
            }
        }
        if (!processKeysMap.isEmpty()) {
            return processKeysMap;
        } else {
            return Collections.emptyMap();
        }
    }

    private Set<String> getProcessKey(String pathInput, String searchKey) {
        Set<String> processKeys = new LinkedHashSet<>();
        String line;
        try (Scanner scanner = new Scanner(new File(pathInput))) {
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                if (this.isPatternMatch(searchKey, line)) {
                    int substring = line.indexOf(BRACKET);
                    if (substring >= 0) {
                        processKeys.add(line.substring(substring, substring + 13));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException: ", e);
        }
        if (!processKeys.isEmpty()) {
            return processKeys;
        } else {
            return Collections.emptySet();
        }
    }

    private List<String> getLogs(Log log) {
        LOGGER.info("Begin searching for all logs.");
        List<String> logs = new ArrayList<>();
        for (String fileInput : log.getProcessKeyMap().keySet()) {
            for (String processKey : log.getProcessKeyMap().get(fileInput)) {
                this.getAllProcessLogs(logs, processKey, fileInput);
            }
        }
        LOGGER.info("Amount of total logs row: {}", logs.size());
        return logs;
    }

    private void getAllProcessLogs(List<String> logs, String processKey, String pathInput) {
        LOGGER.info("Begin searching log for process key: {}", processKey);
        String line;
        try (Scanner scanner = new Scanner(new File(pathInput))) {
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                if (this.isPatternMatch(processKey, line)) {
                    logs.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException: ", e);
        }
        LOGGER.info("End searching log for process key: {}", processKey);
    }

    private void exportLog(Log log) {
        LOGGER.info("------START EXPORTING LOGS------");
        try (FileWriter fileWriter = new FileWriter(log.getPathOutput().concat(log.getFileName().concat(TXT)))) {
            for (int i = 0; i < log.getLogs().size(); i++) {
                if (i == log.getLogs().size() - 1) {
                    fileWriter.write(log.getLogs().get(i));
                } else {
                    fileWriter.write(log.getLogs().get(i));
                    fileWriter.write("\n");
                }
            }
        } catch (IOException e) {
            LOGGER.error("IOException: ", e);
        }
        LOGGER.info("------END EXPORTING LOGS------");
    }

    private boolean isPatternMatch(String key, String source) {
        return Pattern.compile(Pattern.quote(key), Pattern.CASE_INSENSITIVE).matcher(source).find();
    }

    private int getTotalProcess(Map<String, Set<String>> processKeyMap) {
        int total = 0;
        for (Set<String> processKeys : processKeyMap.values()) {
            total += processKeys.size();
        }
        return total;
    }
}
