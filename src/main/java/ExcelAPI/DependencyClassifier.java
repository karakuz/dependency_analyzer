package ExcelAPI;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.*;

public class DependencyClassifier {
    private static final String[] forbiddenFileExtensions = {".java", ".javax"};
    public static List<List<String>> cyclicDependencies = new ArrayList<>();
    public static HashMap<String, HashMap<String, Integer>> commitStats = new HashMap<>();

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

    public static void commitClassifier(JsonValue commits){
        boolean firstIter = true;
        for(JsonValue commit : commits.asArray()){
            JsonArray changesOnCommit = commit.asObject().get("changes").asArray();
            for(int i=0; i<changesOnCommit.size(); i++){
                String commitChange = changesOnCommit.get(i).asString();
                if(commitChange.contains("Main.java"))
                    continue;

                for(int x=0; x<changesOnCommit.size(); x++){
                    String changedWith = changesOnCommit.get(x).asString();
                    if(i == x || changedWith.contains("Main.java"))
                        continue;

                    HashMap<String, Integer> changedWithStat = commitStats.get(commitChange);

                    if(changedWithStat == null){
                        HashMap<String, Integer> newData = new HashMap<>();
                        newData.put(changedWith, 1);
                        commitStats.put(commitChange, newData);
                    }
                    else{
                        if(firstIter){
                            HashMap<String, Integer> newData = new HashMap<>();
                            newData.put(changedWith, 1);
                            changedWithStat.putAll(newData);
                        }
                        else{
                            if(changedWithStat.get(changedWith) == null){
                                HashMap<String, Integer> newData = new HashMap<>();
                                newData.put(changedWith, 1);
                            }
                            else{
                                int numOfChanges = changedWithStat.get(changedWith);
                                changedWithStat.put(changedWith, ++numOfChanges);
                            }
                        }
                    }
                }
            }
            firstIter = false;
        }

        Iterator it = commitStats.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String commitChange = (String) pair.getKey();
            HashMap<String, Integer> changedWithAll = (HashMap<String, Integer>) pair.getValue();

            Iterator it_ = changedWithAll.entrySet().iterator();
            while(it_.hasNext()){
                Map.Entry pair_ = (Map.Entry) it_.next();
                String changedWith = (String) pair_.getKey();
                int numOfChanges = (int) pair_.getValue();

                if(commitStats.get(changedWith).get(commitChange) == null){
                    HashMap<String, Integer> newData = new HashMap<>();
                    newData.put(commitChange, numOfChanges);
                    commitStats.get(changedWith).putAll(newData);
                }
            }
        }
    }

    public static void checkCyclicDependencies(String className, List<String> importDependencies){
        //System.out.println("className: "+ className + ", importDependencies: " + importDependencies);

        for(String importDependency : importDependencies){
            List<String> newDependencyChain = new ArrayList<>(Arrays.asList(className, importDependency));
            cyclicDependencies.add(newDependencyChain);
        }
        /*List<String> lastClassNames = new ArrayList<>();
        for(List<String> dependencyChain : cyclicDependencies) {
            String lastClassName = dependencyChain.get(dependencyChain.size() - 1);
            lastClassNames.add(lastClassName);
        }
        System.out.println("lastClassNames: " + lastClassNames);*/

        //printCyclicDependencies();
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

    public static void printCommitStats(){
        //HashMap<String, HashMap<String, Integer>>

    }
}
