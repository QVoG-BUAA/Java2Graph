package cn.edu.j2graph.qvog.j2graph.j2cpg.graphviz;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.*;

public class JdtAstUtil {
    /**
     * get compilation unit of source code
     *
     * @param javaFilePath
     * @return CompilationUnit
     */
    public static CompilationUnit getCompilationUnit(String javaFilePath) {

        byte[] input = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(javaFilePath));
            input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            bufferedInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    /*
    创建抽象语法书解析器，按照Java语言规范Java SE 8 Edition (JLS8)解析，
    支持操作所有jdk8版本的Java代码操作
     */
        ASTParser astParser = ASTParser.newParser(AST.JLS8);

    /*
     Java编译参数,默认astKind类型为编译单元K_COMPILATION_UNIT，解析器将源码会解析为一个编译单元类型CompilationUnit
     K_COMPILATION_UNIT：包含所有的类型声明、import、package等等节点
     K_CLASS_BODY_DECLARATIONS：仅将源码的class body（含子节点）部分解析。不包含类型、import、package等信息
     K_STATEMENTS：源码将被解析为一个语句序列statements数组，package、import节点会被解析成assert语句，其余节点正常解析
     K_EXPRESSION：源码将被解析为一个表达式，如果将一个类的源码传入，返回的将会是一对解析失败的报错，类的import等节点均不是表达式
     */
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);

    /*
    ASTParser绑定源码
     */
        astParser.setSource(new String(input).toCharArray());

    /*
    请求编译器为其创建的AST节点提供绑定信息。默认为false，如果设置为true，那么可以该去该语法树节点的全限定类名等信息。
     */
        astParser.setResolveBindings(true);

        // 如果不设置 setResolveBindings是无效的，四个属性配置其中给一个即可，如果不配置全部默认为null
        // 第1个参数 classpathEntries：当没有Java project时（直接解析某个文件），解析绑定信息使用的classpath,例如：String[] classpathEntries = new String[]{"D:\Applications\apache-maven-3.5.0\conf\repository\org\springframework\spring-beans\4.3.20.RELEASE\spring-beans-4.3.20.RELEASE.jar"};
        // 第2个参数 sourcepathEntries：当没有Java project时（直接解析某个文件），解析绑定信息使用的sourcepath,例如：String[] sourcepathEntries = new String[]{"D:\Applications\apache-maven-3.5.0\conf\repository\org\springframework\spring-beans\4.3.20.RELEASE\spring-beans-4.3.20.RELEASE-sources.jar"};
        // 第3个参数 sourcepathsEncodings：sourcepaths条目解析使用编码
        // 第4个参数 includeRunningVMBootclasspath：运行时VM的bootclasspath追加至给定的classpath
        astParser.setEnvironment(null, null, null, true);

        // 如果开启则允许编译器创建包含语法错误的语句，当解析时发现语法错误会保留statementsRecoveryData，
        // 用于语句恢复，获取恢复数据的方法不希望被客户端使用，仅供框架内部使用
        astParser.setStatementsRecovery(true);

    /*
    这个函数一定要调用，参数随便设置一个字符串都可以。不调用 在一些地方会出现resolveBinding()为空的情况。
    在使用setSource(char [])方法的时候，后面要加上setEnvironment和setUnitName方法。setEnvironment方法单独使用是可以加入具体参数，但是setUnitName方法也使用的时候，参数只能用(null, null, null, true)才跑的出结果。
     */
        astParser.setUnitName("example.java");

    /*
    createAST()方法的参数类型为IProgressMonitor，用于对AST的转换进行监控，不需要的话就填个null即可。
     */
        CompilationUnit compilationUnit = (CompilationUnit) (astParser.createAST(null));
        return compilationUnit;
    }
}