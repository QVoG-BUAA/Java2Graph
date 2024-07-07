package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.mynode.MyASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.mynode.MyMethodNode;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class ASTGenerator {

    public List<MyMethodNode> methodNodeList = new ArrayList<MyMethodNode>();

    public ASTGenerator(CompilationUnit cu, List<MethodDeclaration> methodDecs) {
        parse(cu, methodDecs);
    }

    public void parse(CompilationUnit cu, List<MethodDeclaration> methodDecs) {
		/*ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(srcStr.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		// find the MethodDeclaration node, MethodNodeVisitor
		MethodNodeVisitor methodNodeVisitor = new MethodNodeVisitor();
		cu.accept(methodNodeVisitor);*/
        // traverse all child nodes, NodeVisitor
        int FIRST = 0, SECOND = 0;
        for (MethodDeclaration m : methodDecs) {
            MyMethodNode mNode = new MyMethodNode();
            ASTNode tmpNode = m;
            while (tmpNode.getClass() != TypeDeclaration.class) {
                tmpNode = tmpNode.getParent();
            }
            mNode.classNode = (TypeDeclaration) tmpNode;
            mNode.methodNode = m;
            mNode.startLine = cu.getLineNumber(m.getStartPosition());
            mNode.endLine = cu.getLineNumber(m.getStartPosition() + m.getLength());
            NodeVisitor nv = new NodeVisitor();
            m.accept(nv);

            List<ASTNode> astnodes = nv.getASTNodes();
            for (ASTNode node : astnodes) {
                MyASTNode myNode = new MyASTNode();
                myNode.astNode = node;
                myNode.startLineNum = cu.getLineNumber(node.getStartPosition());
                myNode.endLineNum = cu.getLineNumber(node.getStartPosition() + node.getLength());
                // add to nodeList
                mNode.nodeList.add(myNode);
                // add to mapping
                // in case, I need to exclude root node
                if (node.equals(m)) {
                    continue;
                }
                if (node.getParent().getProperty("number") == null) {
                    node.getParent().setProperty("number", FIRST + "." + SECOND++);
                }
                node.setProperty("number", FIRST + "." + SECOND++);
                String[] link = { (String) node.getParent().getProperty("number"), (String) node.getProperty("number") };
                mNode.mapping.add(link);
            }
            FIRST++;
            SECOND = 0;
            methodNodeList.add(mNode);
        }
        // System.out.print(ast);
    }

    /**
     * get function for methodNodeList
     *
     * @return
     */
    public List<MyMethodNode> getMethodNodeList() {
        return methodNodeList;
    }

	/*public void ParseFile(File f) {
		String filePath = f.getAbsolutePath();
		if (f.isFile()) {
			try {
				parse(FileUtil.readFileToString(filePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Not a File!");
		}
	}*/
}
