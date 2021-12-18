package JavaParser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ClassImplementsVisitor extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration md, List<String> implements_) {
        super.visit(md, implements_);
        if(md.getImplementedTypes().size() > 0){
            implements_.add(md.getImplementedTypes().get(0).toString());
        }
    }
}
