package com.ublox.BLE_med;

public class Wait {
    public static void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread interrupted", e);
        }
    }
}
