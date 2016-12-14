package com.valevich.materikemployee.util;

import com.valevich.materikemployee.R;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@EBean
public class FileHelper {

    @StringRes(R.string.stats_storage)
    String storagePath;

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

    public List<String> formatFiles(List<String> fullPathFiles) {
        List<String> formattedFiles = new ArrayList<>();
        for(String fileName:fullPathFiles) {
            formattedFiles.add(fileName.substring(fileName.indexOf(storagePath) + storagePath.length()+1));
        }
        return formattedFiles;
    }
}
