package JavaParser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class FieldVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(FieldDeclaration field, Void arg) {
        super.visit(field, arg);
        System.out.println("field: " + field.toString());
        System.out.println("getElementType(): " + field.getElementType());

        System.out.println(" ");
    }
}
