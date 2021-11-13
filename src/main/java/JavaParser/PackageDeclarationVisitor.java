package JavaParser;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class PackageDeclarationVisitor extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(PackageDeclaration packageDecleration, List<String> packageName) {
        super.visit(packageDecleration, packageName);
        packageName.add(packageDecleration.getNameAsString());
        System.out.println("packageDeclaration: " + packageDecleration);
    }
}
