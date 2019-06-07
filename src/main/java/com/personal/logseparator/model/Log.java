package com.personal.logseparator.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Log {

    private List<String> pathInputs;
    private String pathOutput;
    private String fileName;
    private String searchKey;
    private Map<String, Set<String>> processKeyMap;
    private List<String> logs;

    public List<String> getPathInputs() {
        return pathInputs;
    }

    public void setPathInputs(List<String> pathInputs) {
        this.pathInputs = pathInputs;
    }

    public String getPathOutput() {
        return pathOutput;
    }

    public void setPathOutput(String pathOutput) {
        this.pathOutput = pathOutput;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Map<String, Set<String>> getProcessKeyMap() {
        return processKeyMap;
    }

    public void setProcessKeyMap(Map<String, Set<String>> processKeyMap) {
        this.processKeyMap = processKeyMap;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }
}
