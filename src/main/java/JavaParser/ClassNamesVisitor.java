package JavaParser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Arrays;
import java.util.List;

public class ClassNamesVisitor extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration md, List<String> className) {
        super.visit(md, className);
        String className_ = md.getNameAsString();

        Object[] extends_ = md.getExtendedTypes().toArray();
        String allExtendsOfClass = "";
        for(Object extendsName : extends_)
            allExtendsOfClass += extendsName.toString() + ",";

        allExtendsOfClass = (allExtendsOfClass.length() > 1)
            ? allExtendsOfClass.substring(0, allExtendsOfClass.length()-1)
            : "_";


        Object[] implements_ = md.getImplementedTypes().toArray();
        String allImplementsOfClass = "";
        for(Object implementsName : implements_)
            allImplementsOfClass += implementsName.toString() + ",";

        allImplementsOfClass = (allImplementsOfClass.length() > 1)
                ? allImplementsOfClass.substring(0, allImplementsOfClass.length()-1)
                : "_";

        className.add(className_ + "|" + allExtendsOfClass + "|" + allImplementsOfClass);
        //System.out.println("Class Data: " + className_ + "|" + allExtendsOfClass + "|" + allImplementsOfClass);
    }
}

/*for (String className : classNames) {
    if(className == null || className.equals("Main"))
        continue;

    //System.out.println("javaFileLocation: " + javaFileLocation + ", directory: " + directory);
    //System.out.println("\tClass Name: " + className);

    VoidVisitor<List<String>> packageDeclarationVisitor = new PackageDeclarationVisitor();
    packageDeclarationVisitor.visit(cu, packageName);

    String foundImportedClassName = "";
    if(packageName.size() > 0){
        foundImportedClassName = packageName.get(0) + "." + className;
        classes.add(foundImportedClassName, Json.object());
    }

    VoidVisitor<List<String>> importDeclarationVisitor = new ImportDeclarationVisitor();
    importDeclarationVisitor.visit(cu, imports_);

    JsonArray importsArray = Json.array(imports_.toArray(String[]::new));
    JsonObject classInfos = Json.object();

    VoidVisitor<List<String>> classExtendsVisitor = new ClassExtendsVisitor();
    classExtendsVisitor.visit(cu, extends_);
    //System.out.println("\t\textends_: " + extends_);

    String[] extends__ = Extracter.extendsExtracter(extends_, imports_, directory);
    JsonArray extendsArray = Json.array(extends__);


    VoidVisitor<List<String>> classImplementsVisitor = new ClassImplementsVisitor();
    classImplementsVisitor.visit(cu, implements_);
    //System.out.println("\t\timplements_: " + implements_);

    String[] implements__ = Extracter.implementsExtracter(implements_, imports_, directory);
    JsonArray implementsArray = Json.array(implements__);

    //System.out.println("implementsArray: ");
    //System.out.println(implementsArray);

    classInfos
            .add("imports", importsArray)
            .add("extends", extendsArray)
            .add("implements", implementsArray);

    if(!foundImportedClassName.equals(""))
        classes.set(foundImportedClassName, classInfos);

    //System.out.println("\n");
}*/