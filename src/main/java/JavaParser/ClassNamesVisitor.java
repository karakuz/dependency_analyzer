package JavaParser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ClassNamesVisitor extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration md, List<String> className) {
        super.visit(md, className);
        className.add(md.getNameAsString());
        //System.out.println("Class Name: " + className);
    }
}
