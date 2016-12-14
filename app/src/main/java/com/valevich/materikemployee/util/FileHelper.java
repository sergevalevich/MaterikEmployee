package com.valevich.materikemployee.util;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.util.ArrayList;

@EBean
public class FileHelper {
    public ArrayList<String> findFiles(File dir, String pattern, ArrayList<String> matchingSAFFileNames) {

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (File file : listFile) {

                if (file.isDirectory()) {
                    findFiles(file, pattern, matchingSAFFileNames);
                } else {
                    if (file.getAbsoluteFile().getAbsolutePath().endsWith(pattern)) {
                        matchingSAFFileNames.add(dir.toString() + File.separator + file.getName());
                    }
                }
            }
        }
        return matchingSAFFileNames;
    }
}
