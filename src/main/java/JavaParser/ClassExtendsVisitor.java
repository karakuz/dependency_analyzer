package JavaParser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class ClassExtendsVisitor extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration md, List<String> extends_) {
        super.visit(md, extends_);
        if(md.getExtendedTypes().size() > 0){
            extends_.add(md.getExtendedTypes().get(0).toString());
        }
    }
}
