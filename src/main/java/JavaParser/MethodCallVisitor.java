package JavaParser;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.types.ResolvedType;

import java.util.Optional;

public class MethodCallVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(MethodCallExpr methodCall, Void arg) {
        String scope = methodCall.getScope().toString();//OPTIONAL[methodName]
        scope = scope.substring(9, scope.length()-1);

        NodeList<Expression> arguments = methodCall.getArguments();
        String methodArgs = "";
        for(Expression argument : arguments){
            if(argument.isStringLiteralExpr())
                methodArgs += "String,";
            if(argument.isIntegerLiteralExpr())
                methodArgs += "int,";

            if(argument.isNameExpr() || argument.isBinaryExpr()){
                System.out.println("");
                System.out.println(argument.toString());
                System.out.println(argument.getClass());
                System.out.println("");
            }

            if(argument.isBinaryExpr()){
                /*ResolvedType resolvedType = argument.calculateResolvedType();
                System.out.println(argument.toString() + " is a: " + resolvedType);*/
                System.out.println(((BinaryExpr) argument).getLeft());
                System.out.println(((BinaryExpr) argument).getRight());
            }
        }
        if(!methodArgs.equals(""))
            methodArgs = methodArgs.substring(0, methodArgs.length()-1);

        System.out.println(scope + "." + methodCall.getName() + "(" + methodArgs + ")");
        // Don't forget to call super, it may find more method calls inside the arguments of this method call, for example.
        super.visit(methodCall, arg);
    }
}
