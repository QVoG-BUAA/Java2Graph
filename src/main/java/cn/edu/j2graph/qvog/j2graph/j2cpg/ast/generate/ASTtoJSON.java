package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate;


import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialAST;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialMethod;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.mynode.MyASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.mynode.MyMethodNode;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ASTtoJSON {

    public static InitialAST ASTtoJsonParser(String filepath, List<MyMethodNode> methodNodeList) {
        //String str = "digraph " + methodNodeList.get(0).classNode.getName() + " {\n";
        // name
        List<String> statements = new ArrayList<>();
        statements.add(VariableDeclarationStatement.class.toString());
        statements.add(TypeDeclarationStatement.class.toString());
        statements.add(ThrowStatement.class.toString());
        statements.add(SynchronizedStatement.class.toString());
        statements.add(LabeledStatement.class.toString());
        statements.add(EnhancedForStatement.class.toString());
        statements.add(DoStatement.class.toString());
        statements.add(AssertStatement.class.toString());
        statements.add(IfStatement.class.toString());
        statements.add(ExpressionStatement.class.toString());
        statements.add(BreakStatement.class.toString());
        statements.add(ContinueStatement.class.toString());
        statements.add(EmptyStatement.class.toString());
        statements.add(ForStatement.class.toString());
        statements.add(ReturnStatement.class.toString());
        statements.add(SwitchStatement.class.toString());
        statements.add(TryStatement.class.toString());
        statements.add(WhileStatement.class.toString());
        InitialAST initialAST = new InitialAST();
        initialAST.filename = filepath.substring(filepath.lastIndexOf("\\") + 1);
        initialAST.path = filepath;
        HashMap<String, InitialASTNode> hashMap = new HashMap<>();
        List<InitialMethod> initialMethodList = new ArrayList<>();
        for (MyMethodNode m : methodNodeList) {
            //str += ("subgraph " + m.classNode.getName() + " {\nlabel = \"" + m.methodNode.getName() + "\";\n");
            InitialMethod initialMethod = new InitialMethod();
            initialMethod.label = String.valueOf(m.methodNode.getName());
            initialMethod.className = String.valueOf(m.classNode.getName());
            initialMethod.startLine = m.startLine;
            initialMethod.endLine = m.endLine;
            boolean flag = true;
            String firstNodeHashCode = "";
            for (MyASTNode mn : m.nodeList) {
                ASTNode astNode = mn.astNode;
                InitialASTNode initialASTNode = new InitialASTNode();
                //String hashcode = (String) astNode.getProperty("number");


				/*if(statements.contains(astNode.getClass().toString())){
					str += (String.valueOf(hashcode) +" <"
							+ String.valueOf(mn.startLineNum)+"."+ String.valueOf(mn.endLineNum)+">"
							+ "[ type=Statement," +" label=\""+buildLabel(mn)+"\"" +  " ]"+"\n");
				}
				else {
					str += (String.valueOf(hashcode) + " <"
							+ String.valueOf(mn.startLineNum)+"."+ String.valueOf(mn.endLineNum)+">"
							+"[ type="+ASTNode.nodeClassForType(mn.astNode.getNodeType()).getName().replace("org.eclipse.jdt.core.dom.", "")
							+"," +" label=\""+buildLabel(mn)+"\"" + " ]" +"\n");
				}*/
                initialASTNode.label = buildLabel(mn);
                initialASTNode.startLine = mn.startLineNum;
                initialASTNode.endLine = mn.endLineNum;
				/*if(statements.contains(astNode.getClass().toString())){
					initialASTNode.type = "Statement";
				}
				else {
					initialASTNode.type = ASTNode.nodeClassForType(mn.astNode.getNodeType()).getName().replace("org.eclipse.jdt.core.dom.", "");
				}*/
                initialASTNode.type = ASTNode.nodeClassForType(mn.astNode.getNodeType()).getName().replace("org.eclipse.jdt.core.dom.", "");
                hashMap.put((String) astNode.getProperty("number"), initialASTNode);
                //System.out.println((String) astNode.getProperty("number"));
                if (flag) {
                    firstNodeHashCode = (String) astNode.getProperty("number");
                    flag = false;
                }
            }
            for (String[] k : m.mapping) {
                String parent = k[0];
                String son = k[1];
                //System.out.println(parent);
                hashMap.get(parent).childNodeList.add(hashMap.get(son));
                //str += (parent + " -> " + son + "\n");
            }
            //str += "}\n";
            initialMethod.initialASTNode = hashMap.get(firstNodeHashCode);
            hashMap.clear();
            initialMethodList.add(initialMethod);
        }
        initialAST.initialMethodList = initialMethodList;
        //str += "}\n";

        //System.out.println(gson.toJson(initialAST));
        return initialAST;
        //return gson.toJson(fileStart);
    }

    /**
     * Configure the label, i.e., what you want to display in the visulization
     *
     * @param node
     * @return
     */
    public static String buildLabel(MyASTNode node) {
        String contentString = node.astNode.toString().replace("\n", "").replace("\"", "\\\"").replace("  ", "");
        String nodeType = ASTNode.nodeClassForType(node.astNode.getNodeType()).getName().replace("org.eclipse.jdt.core.dom.", "");
        return //"("+
                contentString
                //+","+nodeType+","+node.startLineNum+","+node.endLineNum+")"
                ;
    }
}
