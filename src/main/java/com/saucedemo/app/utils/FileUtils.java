package com.saucedemo.app.utils;

import com.saucedemo.app.controller.LoggerManager;

import java.io.File;

public class FileUtils {
    /**
     * Deletes all files in the specified folder.
     * Logs information if the folder path is invalid or already empty.
     * Only files are deleted; subdirectories are ignored.
     *
     * @param folderPath the path to the folder to clear
     */
    public static void clearFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            LoggerManager.info("Invalid folder path: " + folderPath);
            return;
        }
        File[] files = folder.listFiles(File::isFile);
        if (files == null || files.length == 0) {
            LoggerManager.info("Folder is already empty.");
            return;
        }
        boolean allDeleted = true;
        for (File file : files) {
            if (!file.delete()) {
                allDeleted = false;
            }
        }
        if (allDeleted) {
            LoggerManager.info("All files deleted successfully in folder: " + folderPath);
        }
    }
}
