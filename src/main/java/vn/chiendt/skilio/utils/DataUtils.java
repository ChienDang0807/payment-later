package vn.chiendt.skilio.utils;


import java.util.UUID;

public class DataUtils {
    public static String genUUIDWithPrefix(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

}
