package ExcelAPI;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.*;

public class DependencyClassifier {
    private static final String[] forbiddenFileExtensions = {".java", ".javax"};
    public static List<List<String>> cyclicDependencies = new ArrayList<>();

    public static List<String> importClassifier(JsonObject classDependencies){
        List<String> importDependencies = new ArrayList<>();
        JsonArray imports = classDependencies.asObject().get("imports").asArray();

        for(JsonValue imported : imports){
            String dependsOn = imported.asString();
            if(!Arrays.asList(forbiddenFileExtensions).contains(dependsOn))
                importDependencies.add(dependsOn);
        }

        return importDependencies;
    }

    public static List<String> extendClassifier(JsonObject classDependencies){
        List<String> classExtends = new ArrayList<>();

        JsonArray extend = classDependencies.asObject().get("extends").asArray();
        for(JsonValue extended : extend)
            classExtends.add(extended.asString());

        return classExtends;
    }

    public static List<String> implementsClassifier(JsonObject classDependencies){
        List<String> classImplementations = new ArrayList<>();

        JsonArray implement = classDependencies.asObject().get("implements").asArray();
        for(JsonValue implemented : implement)
            classImplementations.add(implemented.asString());

        return classImplementations;
    }

    public static void checkCyclicDependencies(String className, List<String> importDependencies){
        System.out.println("className: "+ className + ", importDependencies: " + importDependencies);

        if(cyclicDependencies.size() == 0){
            //System.out.println("cyclicDependencies.size() == 0");
            for(String importDependency : importDependencies){
                List<String> newDependencyChain = new ArrayList<>(Arrays.asList(className, importDependency));
                cyclicDependencies.add(newDependencyChain);
            }
            printCyclicDependencies();
            return;
        }


        List<String> lastClassNames = new ArrayList<>();
        for(List<String> dependencyChain : cyclicDependencies) {
            String lastClassName = dependencyChain.get(dependencyChain.size() - 1);
            lastClassNames.add(lastClassName);
        }
        HashMap<Integer, List<String>> indexes = getIndexes(lastClassNames, className, importDependencies);

        System.out.println("indexes: " + indexes);
        if(indexes.size() == 0){
            for(String dependency : importDependencies){
                List<String> dependencies = new ArrayList<>();
                dependencies.add(className);
                dependencies.add(dependency);
                List<String> newDependencyChain = new ArrayList<>(dependencies);
                //System.out.println("new: " + className + importDependency);
                cyclicDependencies.add(newDependencyChain);
            }
        }

        Iterator it = indexes.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();

            List<String> importDependencies_ = (List<String>) pair.getValue();
            int index = (Integer) pair.getKey();

            List<List<String>> prev = null;
            List<List<String>> next = null;
            List<String> indexItem = null;
            for(String importDependency : importDependencies_){
                if (index != -1){
                    String lastClassName = lastClassNames.get(index);
                    System.out.println("lastClassName: " + lastClassName + ", adding: " + importDependency + " in if");
                    indexItem = cyclicDependencies.get(index);
                    cyclicDependencies.add(index++, indexItem);
                    
                    List<List<String>> cyclicDependencies_ = new ArrayList<>();
                    Iterator<List<String>> it_ = cyclicDependencies.iterator();
                    while(it_.hasNext()){
                        List<String> item = it_.next();
                        cyclicDependencies_.add(item);
                    }

                    cyclicDependencies = cyclicDependencies_;
                    cyclicDependencies.get(index).add(importDependency);
                }
            }
            //cyclicDependencies.remove(iterIndex);
        }
        printCyclicDependencies();
    }

    public static HashMap<Integer, List<String>> getIndexes(List<String> lastClassNames, String className, List<String> importDependencies){
        HashMap<Integer, List<String>> indexes = new HashMap<>();
        List<String> classNames = new ArrayList<>();

        System.out.println("lastClassNames: " + lastClassNames);
        for(int i=0; i<lastClassNames.size(); i++){
            String lastClassName = lastClassNames.get(i);
            if(className.equals(lastClassName)){
                classNames.addAll(importDependencies);
                indexes.put(i, classNames);
            }
        }
        return indexes;
    }

    public static void printCyclicDependencies(){
        for(List<String> innerList : cyclicDependencies){
            String temp = "";
            for(String className_ : innerList)
                temp = temp + className_.substring(className_.lastIndexOf('.')+1) + " -> ";
            System.out.println("\t" + temp.substring(0,temp.length()-4));
        }
        System.out.println("");
    }
}
