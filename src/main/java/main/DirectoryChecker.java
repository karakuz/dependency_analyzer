package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryChecker {
    public List<String> getJavaFiles(String ROOT, String PROJECT_FILE_PATH){
        List<String> javaFileLocations = new ArrayList<>();
        File[] files = new File(PROJECT_FILE_PATH).listFiles();
        List<String> directories = new ArrayList<>();
        //If this pathname does not denote a directory, then listFiles() returns null.

        for (File file : files) {
            if(file.isFile() && file.getName().endsWith(".java"))
                javaFileLocations.add("\\" + file.getName());
            if(file.isDirectory())
                directories.add("\\" + file.getName());
        }

        boolean isSubDirectory = directories.size() > 0;
        while(isSubDirectory){
            List<String> newDirectories = new ArrayList<>();
            for(String directoryName : directories){
                String directoryPath = ROOT + PROJECT_FILE_PATH + directoryName;
                files = new File(directoryPath).listFiles();

                for (File file : files) {
                    if(file.isFile() && file.getName().endsWith(".java"))
                        javaFileLocations.add(directoryName + "\\" + file.getName());
                    if(file.isDirectory())
                        newDirectories.add(directoryName + "\\" + file.getName());
                }
            }
            directories = newDirectories;
            isSubDirectory = directories.size() > 0;
        }

        return javaFileLocations;
    }
}
