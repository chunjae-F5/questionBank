package com.example.f5.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUrl {

    public String widowsFileDir = "C:\\";
    public String linuxFileDir = "/home/ec2-user/";

    public String selectUrl() {

        String fileDir = "";
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            fileDir = widowsFileDir;
        } else if (osName.contains("nux") || osName.contains("mac") || osName.contains("nix")) {
            fileDir = linuxFileDir;
        }

        return fileDir;
    }
}
