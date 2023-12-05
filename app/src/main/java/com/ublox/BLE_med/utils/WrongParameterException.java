package com.ublox.BLE_med.utils;

public class WrongParameterException extends Exception {
    public WrongParameterException() {
        super("Some of the parameters are invalid!");
    }
}
