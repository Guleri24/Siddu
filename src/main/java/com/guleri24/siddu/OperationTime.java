package com.guleri24.siddu;

public class OperationTime {
    private long startTime;
    private long endTime;

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public void end() {
        this.endTime = System.currentTimeMillis();
    }

    public long getDuration() {
        return endTime - startTime;
    }
}
