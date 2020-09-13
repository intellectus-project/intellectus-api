package com.intellectus.utils;

public class NumberUtils {

    static public double roundDouble(double num) {
        return Math.round(num * 100.0) / 100.0;
    }
}
