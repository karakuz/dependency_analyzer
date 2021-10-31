package JavaParser;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ObjectCreationExprVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(ObjectCreationExpr objectCreationExpr, Void arg) {
        super.visit(objectCreationExpr, arg);
        System.out.println("objectCreationExpr: " + objectCreationExpr.toString());
    }
}
