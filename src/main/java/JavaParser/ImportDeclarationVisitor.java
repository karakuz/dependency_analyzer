package JavaParser;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ImportDeclarationVisitor extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(ImportDeclaration importDeclaration, List<String> imports) {
        super.visit(importDeclaration, imports);
        imports.add(importDeclaration.getName().toString());
        //System.out.println("importDeclaration: " + importDeclaration.getName());
    }
}
