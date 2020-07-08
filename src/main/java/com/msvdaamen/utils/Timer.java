package com.msvdaamen.utils;

public class Timer {

    long start;
    long finished;

    public Timer() {
        start = System.nanoTime();
    }


    public long stop() {
        finished = System.nanoTime();
        return finished - start;
    }
}
