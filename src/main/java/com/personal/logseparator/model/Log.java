package com.personal.logseparator.model;

import java.util.List;
import java.util.Set;

public class Log {

    private String pathInput;
    private String pathOutput;
    private String fileName;
    private String searchKey;
    private Set<String> processKey;
    private List<String> logs;

    public String getPathInput() {
        return pathInput;
    }

    public void setPathInput(String pathInput) {
        this.pathInput = pathInput;
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

    public Set<String> getProcessKey() {
        return processKey;
    }

    public void setProcessKey(Set<String> processKey) {
        this.processKey = processKey;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }
}
