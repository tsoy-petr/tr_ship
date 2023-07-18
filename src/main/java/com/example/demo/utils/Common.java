package com.example.demo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Common {

    public static boolean fileExists(String folderForSavingReports) {

        Path path = Paths.get(folderForSavingReports);

        return Files.exists(path);

    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean isEmpty(String str) {

        return (str == null || str.isEmpty());

    }

}
