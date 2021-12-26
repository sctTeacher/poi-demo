package com.shan.utils;

import java.util.UUID;

public class UUIDGenerator {
    public static String getInitialUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static Long getLongUUID() {
        return UUID.randomUUID().getMostSignificantBits() & 9223372036854775807L;
    }

    public static Long[] getLongUUID(int number) {
        if (number < 1) {
            return null;
        } else {
            Long[] ss = new Long[number];

            for(int i = 0; i < number; ++i) {
                ss[i] = getLongUUID();
            }

            return ss;
        }
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
    }

    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        } else {
            String[] ss = new String[number];

            for(int i = 0; i < number; ++i) {
                ss[i] = getUUID();
            }

            return ss;
        }
    }

    public static void main(String[] args) {
        Long[] ss = getLongUUID(10);

        for(int i = 0; i < ss.length; ++i) {
            System.out.println("ss[" + i + "]=====" + ss[i]);
        }

    }

    public UUIDGenerator() {
    }
}
