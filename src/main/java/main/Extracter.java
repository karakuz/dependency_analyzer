package main;

import java.util.List;

public class Extracter {
    public static String[] implementsExtracter(List<String> implements_, List<String> imports_, String directory){
        String[] implements__ = implements_.toArray(String[]::new);
        if(implements__.length > 0){
            int implementsIndex = 0;
            for(String implementsName : implements__){
                boolean isImplementedClassImported = false;
                for(String imports : imports_){
                    final int lastPackageIndex = imports.lastIndexOf('.') + 1;
                    String importClassName = imports.substring(lastPackageIndex);
                    if(implementsName.equals(importClassName)){
                        isImplementedClassImported = true;
                        implements__[implementsIndex] = imports;
                        break;
                    }
                }
                String lastDirectory = (directory.chars().filter(ch -> ch == '\\').count() > 2)
                        ? directory.replaceAll("\\\\", ".").substring(1)
                        : "";
                if(!isImplementedClassImported)
                    implements__[implementsIndex] = lastDirectory + implements__[implementsIndex];
                implementsIndex++;
            }
        }
        return implements__;
    }

    public static String extendsExtracter(String extends_, List<String> imports_, String directory){
        if(!extends_.equals("")){
            boolean isExtendedClassImported = false;
            for(String imports : imports_){
                final int lastPackageIndex = imports.lastIndexOf('.') + 1;
                String importClassName = imports.substring(lastPackageIndex);
                if(extends_.equals(importClassName)){
                    extends_ = imports;
                    isExtendedClassImported = true;
                    break;
                }
            }
            String lastDirectory = (directory.chars().filter(ch -> ch == '\\').count() > 2)
                    ? directory.replaceAll("\\\\", ".").substring(1)
                    : "";
            if(!isExtendedClassImported)
                extends_ = lastDirectory + extends_;
        }

        return extends_;
    }
}
