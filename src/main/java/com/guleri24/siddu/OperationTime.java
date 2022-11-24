package com.guleri24.siddu;

public class OperationTime {
    private long startTime = 0;
    private long endTime = 0;

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
