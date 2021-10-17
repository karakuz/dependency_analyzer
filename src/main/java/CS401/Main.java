package CS401;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;


public class Main {
    private static final String FILE_PATH = "src/main/java/CS401/Person.java";

    public static void main(String[] args) throws FileNotFoundException {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Person person = new Person("emre", 23, 3000);

        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));

        VoidVisitor<Void> methodNameVisitor = new MethodNamePrinter();
        methodNameVisitor.visit(cu, null);
    }

}
