package JavaParser;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class PackageDeclarationVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(PackageDeclaration packageDecleration, Void arg) {
        super.visit(packageDecleration, arg);
        System.out.println("packageDecleration: " + packageDecleration);
    }
}
