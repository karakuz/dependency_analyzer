package JavaParser;

import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class VariableDeclerationExprVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(VariableDeclarationExpr variableDeclarationExpr, Void arg) {
        super.visit(variableDeclarationExpr, arg);
        System.out.println("variableDeclarationExpr: " + variableDeclarationExpr);

    }
}
