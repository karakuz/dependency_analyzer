package ExcelAPI;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.*;

public class DependencyClassifier {
    private static final String[] forbiddenFileExtensions = {".java", ".javax"};

    public static List<String> importClassifier(JsonObject className_){
        List<String> importDependencies = new ArrayList<>();

        JsonArray imports = className_.asObject().get("imports").asArray();
        for(JsonValue imported : imports)
            if(!Arrays.asList(forbiddenFileExtensions).contains(imported.asString()))
                importDependencies.add(imported.asString());

        return importDependencies;
    }

    public static List<String> extendClassifier(JsonObject className_){
        List<String> classExtends = new ArrayList<>();

        JsonArray extend = className_.asObject().get("extends").asArray();
        for(JsonValue extended : extend)
            classExtends.add(extended.asString());

        return classExtends;
    }

    public static List<String> implementsClassifier(JsonObject className_){
        List<String> classImplementations = new ArrayList<>();

        JsonArray implement = className_.asObject().get("implements").asArray();
        for(JsonValue implemented : implement)
            classImplementations.add(implemented.asString());

        return classImplementations;
    }

    public static void cyclicDependencyClassifier(){

    }
}
