package com.example.f5.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FileUrl {

    public String selectUrl(String windows, String linux) {

        String fileDir = "";
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            fileDir = "C:\\";
        } else if (osName.contains("nux") || osName.contains("mac") || osName.contains("nix")) {
            fileDir = "/Users/kim/";
        }

        return fileDir;
    }
}
