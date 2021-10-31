package JavaParser;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class MethodVisitor extends VoidVisitorAdapter<List<String>> {
     @Override
     public void visit(MethodDeclaration method, List<String> collector) {
         super.visit(method, collector);
         //method.accept(new MethodCallVisitor(), null);

         String methodName = method.getNameAsString();

         //System.out.println("Method Name: " + method.getName());
         List<Parameter> parameters = method.getParameters();

         String params = "(";
         if(parameters.size() > 0){
             for(Parameter parameter : parameters){
                 String type = parameter.getType().toString();
                 params = params + type + ", ";
             }
             params = params.substring(0,params.length()-2);//Removes last char
         }
         params += ")";

         collector.add(methodName + params);
     }
}
