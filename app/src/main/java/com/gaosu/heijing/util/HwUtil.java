package com.gaosu.heijing.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class HwUtil {
    public static void TURNON() {
        String WAKE_PATH = "/sys/class/gpio_show/backlight_status";
        try {
            BufferedWriter bufWriter = null;
            bufWriter = new BufferedWriter(new FileWriter(WAKE_PATH));
            bufWriter.write("0");  // 写操作
            bufWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void TURNOFF() {
        String WAKE_PATH = "/sys/class/gpio_show/backlight_status";
        try {
            BufferedWriter bufWriter = null;
            bufWriter = new BufferedWriter(new FileWriter(WAKE_PATH));
            bufWriter.write("1");  // 写操作
            bufWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String GETST() {
        String WAKE_PATH = "sys/class/gpio_show/ms_status";
        String st = "1";
        try {
            BufferedReader bufWriter = null;
            bufWriter = new BufferedReader(new FileReader(WAKE_PATH));
            st = bufWriter.readLine();

            bufWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return st;

    }
}
