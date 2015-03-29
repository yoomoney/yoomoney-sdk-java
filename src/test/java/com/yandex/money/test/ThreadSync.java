package com.yandex.money.test;

/**
 * You can use it for simple syncing of threads.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
final class ThreadSync {

    private final Object lock = new Object();

    private boolean notified = false;

    /**
     * @see #wait()
     */
    public void doWait() {
        synchronized (lock) {
            if (!notified) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
            notified = false;
        }
    }

    /**
     * @see #notifyAll()
     */
    public void doNotify() {
        synchronized (lock) {
            lock.notifyAll();
            notified = true;
        }
    }
}
