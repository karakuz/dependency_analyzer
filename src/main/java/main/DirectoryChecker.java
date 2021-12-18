package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectoryChecker {
    public HashMap<String, List<String>> getJavaFiles(String ROOT, String PROJECT_FILE_PATH){
        HashMap<String, List<String>> javaFilesInDirectories = new HashMap<>();
        List<String> javaFileLocations = new ArrayList<>();
        File[] files = new File(PROJECT_FILE_PATH).listFiles();
        List<String> directories = new ArrayList<>();
        //If this pathname does not denote a directory, then listFiles() returns null.

        for (File file : files) {
            List<String> javaFilesInDirectory = new ArrayList<>();
            if(file.isFile() && file.getName().endsWith(".java"))
                javaFilesInDirectory.add("\\" + file.getName());
            if(file.isDirectory())
                directories.add("\\" + file.getName());
        }

        boolean isSubDirectory = directories.size() > 0;
        while(isSubDirectory){
            List<String> newDirectories = new ArrayList<>();
            for(String directoryName : directories){
                String directoryPath = ROOT + PROJECT_FILE_PATH + directoryName;
                List<String> javaFilesInDirectory = new ArrayList<>();
                files = new File(directoryPath).listFiles();

                for (File file : files) {
                    if(file.isFile() && file.getName().endsWith(".java")){
                        javaFilesInDirectory.add(directoryName + "\\" + file.getName());
                        javaFileLocations.add(directoryName + "\\" + file.getName());
                    }
                    if(file.isDirectory())
                        newDirectories.add(directoryName + "\\" + file.getName());
                }
                javaFilesInDirectories.put(directoryName, javaFilesInDirectory);
            }
            directories = newDirectories;
            isSubDirectory = directories.size() > 0;
        }


        return javaFilesInDirectories;
    }
}
