package com.yandex.money.api.utils;

/**
 * @author vyasevich
 */
public final class Threads {

    private Threads() {
        // prevents instantiating of this class
    }

    /**
     * Causes the current thread to sleep on specified amount of milliseconds.
     *
     * @param time milliseconds
     */
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // do nothing
        }
    }
}
