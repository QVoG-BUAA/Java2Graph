package cn.edu.buaa.qvog.j2graph.j2cpg.graphviz;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.TinyPDGASTVisitor;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.ASTGenerator;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.ASTtoJSON;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler.FileStartHandler;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.initialast.InitialAST;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.mynode.MyMethodNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.FileStart;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.util.FileUtil;
import cn.edu.buaa.qvog.j2graph.j2cpg.cfg.CFG;
import cn.edu.buaa.qvog.j2graph.j2cpg.cfg.edge.CFGEdge;
import cn.edu.buaa.qvog.j2graph.j2cpg.cfg.node.CFGNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.cfg.node.CFGNodeFactory;
import cn.edu.buaa.qvog.j2graph.j2cpg.pdg.PDG;
import cn.edu.buaa.qvog.j2graph.j2cpg.pdg.edge.PDGEdge;
import cn.edu.buaa.qvog.j2graph.j2cpg.pdg.node.PDGNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.pdg.node.PDGNodeFactory;
import cn.edu.buaa.qvog.j2graph.j2cpg.pe.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Writer {
    public static List<FileStart> start() throws IOException {
        List<FileStart> fileStartList = new ArrayList<>();
        delete(Path.of(Config.OUTPUT_PATH, "AST").toString());
        delete(Path.of(Config.OUTPUT_PATH, "CFG").toString());
        delete(Path.of(Config.OUTPUT_PATH, "PDG").toString());
        delete(Path.of(Config.OUTPUT_PATH, "newAST").toString());
        final String inputPath = Path.of(Config.INPUT_PATH).toString();
        File inputDirectory = new File(inputPath);
        traverse(inputDirectory, fileStartList);
        System.out.println("building and outputting ASTs, CFGs and PDGs...");
        System.out.println("successfully finished.");
        return fileStartList;
    }

    public static void delete(String path) throws IOException {
        Path dir = Paths.get(path);
        if (!Files.exists(dir)) {
            return;
        }
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (!dir.equals(Paths.get(path))) {
                    Files.delete(dir);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void traverse(File file, List<FileStart> fileStartList) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    traverse(childFile, fileStartList);
                }
            }
        } else if (file.isFile() && file.getPath().endsWith(".java")) {
            fileStartList.add(write(file));
        }
    }

    private static FileStart write(File file) throws IOException {
        FileStart fileStart = null;
        final List<MethodInfo> methods = new ArrayList<MethodInfo>();
        final CompilationUnit unit = TinyPDGASTVisitor.createAST(file);
        final List<MethodInfo> m = new ArrayList<MethodInfo>();
        final TinyPDGASTVisitor visitor = new TinyPDGASTVisitor(file.getAbsolutePath(), unit, methods);
        unit.accept(visitor);
        methods.addAll(m);


        FldPkgImpInfo fldPkgImpInfo = GetFldPkgImp.getFldPkgImp(file.getCanonicalPath());

        // ensure the output directory exists
        final File outputDirectory = new File(Path.of(Config.OUTPUT_PATH, "CFG").toString());
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
        final BufferedWriter CFGWriter = new BufferedWriter(new FileWriter(Path.of(Config.OUTPUT_PATH, "CFG", file.getCanonicalPath().hashCode() + ".txt").toString()));
        CFGWriter.write("FilePath:");
        CFGWriter.write(file.getCanonicalPath());
        CFGWriter.newLine();
        CFGWriter.write("FileName:");
        CFGWriter.write(file.getCanonicalPath().substring(file.getCanonicalPath().lastIndexOf("\\") + 1));
        CFGWriter.newLine();
        CFGWriter.write("package:" + fldPkgImpInfo.packageName);
        CFGWriter.newLine();
        for (String importName : fldPkgImpInfo.importName) {
            CFGWriter.write("import:" + importName);
            CFGWriter.newLine();
        }

        final CFGNodeFactory nodeFactory = new CFGNodeFactory();
        String tmpClassName = "";
        int createdGraphNumber = 0;
        int classNumber = 0;
        for (final MethodInfo method : methods) {
            if (!method.className.equals(tmpClassName)) {
                tmpClassName = method.className;
                if (fldPkgImpInfo.classFieldInfos.get(classNumber).field.size() != 0) {
                    CFGWriter.write("classfield:" + tmpClassName);
                    CFGWriter.newLine();
                }
                CFGWriter.write("{");
                CFGWriter.newLine();
                for (FieldInfo field : fldPkgImpInfo.classFieldInfos.get(classNumber).field) {
                    CFGWriter.write("v: " + field.field);
                    CFGWriter.write(" " + field.fieldLine);
                    CFGWriter.newLine();
                }
                for (int i = 0; i < fldPkgImpInfo.classFieldInfos.get(classNumber).field.size() - 1; i++) {
                    CFGWriter.write("estart: " + fldPkgImpInfo.classFieldInfos.get(classNumber).field.get(i).field);
                    CFGWriter.newLine();
                    CFGWriter.write("eend: " + fldPkgImpInfo.classFieldInfos.get(classNumber).field.get(i + 1).field);
                    CFGWriter.write(" ");
                    CFGWriter.write(fldPkgImpInfo.classFieldInfos.get(classNumber).field.get(i).fieldLine);
                    CFGWriter.write("!");
                    CFGWriter.write(fldPkgImpInfo.classFieldInfos.get(classNumber).field.get(i + 1).fieldLine);
                    CFGWriter.newLine();
                }
                classNumber++;
                CFGWriter.write("}");
                CFGWriter.newLine();
            }
            CFGWriter.write("class:" + method.className);
            CFGWriter.newLine();
            CFGWriter.write("method:" + method.name + " ");
            for (VariableInfo parameter : method.getParameters()) {
                CFGWriter.write(parameter.type.name + ":");
                CFGWriter.write(parameter.name + " ");
            }
            CFGWriter.write(method.startLine + ":" + method.endLine);
            CFGWriter.newLine();
            final CFG cfg = new CFG(method, nodeFactory);
            cfg.build();
            cfg.removeSwitchCases();
            cfg.removeJumpStatements();
            writeMethodCFG(cfg, createdGraphNumber++, CFGWriter);
        }
        CFGWriter.close();
        // ensure the output directory exists
        final File outputDirectoryPDG = new File(Path.of(Config.OUTPUT_PATH, "PDG").toString());
        if (!outputDirectoryPDG.exists()) {
            outputDirectoryPDG.mkdirs();
        }
        final BufferedWriter PDGWriter = new BufferedWriter(new FileWriter(Path.of(Config.OUTPUT_PATH, "PDG", file.getCanonicalPath().hashCode() + ".txt").toString()));
        PDGWriter.write("FilePath:");
        PDGWriter.write(file.getCanonicalPath());
        PDGWriter.newLine();
        PDGWriter.write("FileName:");
        PDGWriter.write(file.getCanonicalPath().substring(file.getCanonicalPath().lastIndexOf("\\") + 1));
        PDGWriter.newLine();
        PDGWriter.write("package:" + fldPkgImpInfo.packageName);
        PDGWriter.newLine();
        for (String importName : fldPkgImpInfo.importName) {
            PDGWriter.write("import:" + importName);
            PDGWriter.newLine();
        }

        tmpClassName = "";
        createdGraphNumber = 0;
        classNumber = 0;
        for (final MethodInfo method : methods) {
            if (!method.className.equals(tmpClassName)) {
                tmpClassName = method.className;
                if (fldPkgImpInfo.classFieldInfos.get(classNumber).field.size() != 0) {
                    PDGWriter.write("classfield:" + tmpClassName);
                    PDGWriter.newLine();
                }
                PDGWriter.write("{");
                PDGWriter.newLine();
                for (FieldInfo field : fldPkgImpInfo.classFieldInfos.get(classNumber).field) {
                    PDGWriter.write("v: " + field.field);
                    PDGWriter.write(" " + field.fieldLine);
                    PDGWriter.newLine();
                }
                for (int i = 0; i < fldPkgImpInfo.classFieldInfos.get(classNumber).field.size() - 1; i++) {
                    PDGWriter.write("estart: " + fldPkgImpInfo.classFieldInfos.get(classNumber).field.get(i).field);
                    PDGWriter.newLine();
                    PDGWriter.write("eend: " + fldPkgImpInfo.classFieldInfos.get(classNumber).field.get(i + 1).field);
                    PDGWriter.write(" ");
                    PDGWriter.write(fldPkgImpInfo.classFieldInfos.get(classNumber).field.get(i).fieldLine);
                    PDGWriter.write("!");
                    PDGWriter.write(fldPkgImpInfo.classFieldInfos.get(classNumber).field.get(i + 1).fieldLine);
                    PDGWriter.newLine();
                }
                classNumber++;
                PDGWriter.write("}");
                PDGWriter.newLine();
            }
            PDGWriter.write("class:" + method.className);
            PDGWriter.newLine();
            PDGWriter.write("method:" + method.name + " ");
            for (VariableInfo parameter : method.getParameters()) {
                PDGWriter.write(parameter.type.name + ":");
                PDGWriter.write(parameter.name + " ");
            }
            PDGWriter.write(method.startLine + ":" + method.endLine);
            PDGWriter.newLine();
            final PDG pdg = new PDG(method, new PDGNodeFactory(), new CFGNodeFactory(), true, true, true);
            pdg.build();
            writePDG(pdg, createdGraphNumber++, PDGWriter);
        }
        PDGWriter.close();

        ASTGenerator astGenerator = new ASTGenerator(unit, visitor.getMethodDecs());
        List<MyMethodNode> methodNodeList = astGenerator.getMethodNodeList();
        String canonicalPath = file.getCanonicalPath();
        InitialAST initialAST = ASTtoJSON.ASTtoJsonParser(canonicalPath, methodNodeList);
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        FileUtil.writeFile(Path.of(Config.OUTPUT_PATH, "AST", canonicalPath.hashCode() + ".json").toString(), gson.toJson(initialAST));
        fileStart = new FileStartHandler(initialAST).build();
        fileStart.hashCode = String.valueOf(canonicalPath.hashCode());
        FileUtil.writeFile(Path.of(Config.OUTPUT_PATH, "newAST", canonicalPath.hashCode() + ".json").toString(), gson.toJson(fileStart));
        //FileUtil.writeFile("output/AST/" + canonicalPath.hashCode() + ".json", new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(fileStart));
        return fileStart;
    }

    static private void writeMethodCFG(final CFG cfg, final int createdGraphNumber, final BufferedWriter writer) throws IOException {
        writer.write("{");
        writer.newLine();
        HashMap<String, LineInfo> hashMap = new HashMap<>();

        final SortedMap<CFGNode<? extends ProgramElementInfo>, Integer> nodeLabels = new TreeMap<CFGNode<? extends ProgramElementInfo>, Integer>();
        for (final CFGNode<?> node : cfg.getAllNodes()) {
            nodeLabels.put(node, nodeLabels.size());
        }

        for (final Map.Entry<CFGNode<? extends ProgramElementInfo>, Integer> entry : nodeLabels.entrySet()) {

            final CFGNode<? extends ProgramElementInfo> node = entry.getKey();
            final Integer label = entry.getValue();

            String line = node.getText().replace("\"", "\\\"").replace("\\\\\"", "\\\\\\\"");
            int leftjiankuohao = line.lastIndexOf('<');
            int rightjiankuohao = line.lastIndexOf('>');
            LineInfo lineInfo = new LineInfo(line.substring(leftjiankuohao + 1, rightjiankuohao), line.substring(0, leftjiankuohao - 1));
            hashMap.put(createdGraphNumber + "." + label, lineInfo);
            writer.write("v: ");
            writer.write(lineInfo.label.replace("\n", "").replace("\r", ""));
            //writer.newLine();
            writer.write(" ");
            writer.write(lineInfo.lineNo);
            writer.newLine();

        }

		/*writer.write("subgraph cluster");
		writer.write(Integer.toString(createdGraphNumber));
		writer.write(" {");
		writer.newLine();

		writer.write("label = \"");
		writer.write(getMethodSignature((MethodInfo) cfg.core));
		writer.write("\";");
		writer.newLine();

		final SortedMap<CFGNode<? extends ProgramElementInfo>, Integer> nodeLabels = new TreeMap<CFGNode<? extends ProgramElementInfo>, Integer>();
		for (final CFGNode<?> node : cfg.getAllNodes()) {
			nodeLabels.put(node, nodeLabels.size());
		}

		for (final Map.Entry<CFGNode<? extends ProgramElementInfo>, Integer> entry : nodeLabels
				.entrySet()) {

			final CFGNode<? extends ProgramElementInfo> node = entry.getKey();
			final Integer label = entry.getValue();

			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(label));
			writer.write(" [label = \"");
			writer.write(node.getText().replace("\"", "\\\"")
					.replace("\\\\\"", "\\\\\\\""));
			writer.write("\"");

			final CFGNode<? extends ProgramElementInfo> enterNode = cfg
					.getEnterNode();
			final SortedSet<CFGNode<? extends ProgramElementInfo>> exitNodes = cfg
					.getExitNodes();

			if (enterNode == node) {
				writer.write(", fillcolor = aquamarine");
			} else if (exitNodes.contains(node)) {
				writer.write(", fillcolor = deeppink");
			} else {
				writer.write(", fillcolor = white");
			}

			if (node instanceof CFGControlNode) {
				writer.write(", shape = diamond");
			} else {
				writer.write(", shape = ellipse");
			}

			writer.write("];");
			writer.newLine();
		}*/

        writeCFGEdges(cfg, nodeLabels, createdGraphNumber, writer, hashMap);

        writer.write("}");
        writer.newLine();
    }

    static private void writeCFGEdges(final CFG cfg, final Map<CFGNode<? extends ProgramElementInfo>, Integer> nodeLabels, final int createdGraphNumber, final BufferedWriter writer, HashMap<String, LineInfo> hashMap) throws IOException {

        if (null == cfg) {
            return;
        }

        final SortedSet<CFGEdge> edges = new TreeSet<CFGEdge>();
        for (final CFGNode<?> node : cfg.getAllNodes()) {
            edges.addAll(node.getBackwardEdges());
            edges.addAll(node.getForwardEdges());
        }

        for (final CFGEdge edge : edges) {
            LineInfo fromNode = hashMap.get(createdGraphNumber + "." + nodeLabels.get(edge.fromNode));
            LineInfo toNode = hashMap.get(createdGraphNumber + "." + nodeLabels.get(edge.toNode));
            writer.write("estart: ");
            writer.write(fromNode.label.replace("\n", "").replace("\r", ""));
            writer.newLine();
            writer.write("eend: ");
            writer.write(toNode.label.replace("\n", "").replace("\r", ""));
            writer.write(" ");
            writer.write(fromNode.lineNo);
            writer.write("!");
            writer.write(toNode.lineNo);
            writer.newLine();
        }

		/*if (null == cfg) {
			return;
		}

		final SortedSet<CFGEdge> edges = new TreeSet<CFGEdge>();
		for (final CFGNode<?> node : cfg.getAllNodes()) {
			edges.addAll(node.getBackwardEdges());
			edges.addAll(node.getForwardEdges());
		}

		for (final CFGEdge edge : edges) {
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(nodeLabels.get(edge.fromNode)));
			writer.write(" -> ");
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(nodeLabels.get(edge.toNode)));
			writer.write(" [label=\""
					+ edge.getDependenceString() + "\"];");
			writer.newLine();
		}*/
    }

    static private void writePDG(final PDG pdg, final int createdGraphNumber, final BufferedWriter writer) throws IOException {

        final MethodInfo method = pdg.unit;

        writer.write("{");
        writer.newLine();
        HashMap<String, LineInfo> hashMap = new HashMap<>();

        final Map<PDGNode<?>, Integer> nodeLabels = new HashMap<PDGNode<?>, Integer>();
        for (final PDGNode<?> node : pdg.getAllNodes()) {
            nodeLabels.put(node, nodeLabels.size());
        }

        for (final Map.Entry<PDGNode<?>, Integer> entry : nodeLabels.entrySet()) {
            String line = entry.getKey().getText().replace("\"", "\\\"").replace("\\\\\"", "\\\\\\\"");
            int leftjiankuohao = line.lastIndexOf('<');
            int rightjiankuohao = line.lastIndexOf('>');
            LineInfo lineInfo;
            if (line.substring(0, leftjiankuohao - 1).equals("Enter")) {
                int shengluehao = line.lastIndexOf("...");
                if (shengluehao == -1) {
                    lineInfo = new LineInfo(line.substring(leftjiankuohao + 1, rightjiankuohao), "Enter Enter");
                } else {
                    lineInfo = new LineInfo(line.substring(leftjiankuohao + 1, shengluehao), "Enter Enter");
                    lineInfo.secondLineNo = Integer.parseInt(line.substring(shengluehao + 3, rightjiankuohao));
                }
            } else {
                lineInfo = new LineInfo(line.substring(leftjiankuohao + 1, rightjiankuohao), line.substring(0, leftjiankuohao - 1));
            }

            hashMap.put(createdGraphNumber + "." + entry.getValue(), lineInfo);
            writer.write("v: ");
            writer.write(lineInfo.label.replace("\n", "").replace("\r", ""));
            //writer.newLine();
            writer.write(" ");
            writer.write(lineInfo.lineNo);
            if (lineInfo.label.equals("Enter Enter")) {
                writer.write(".");
                writer.write(Integer.toString(lineInfo.secondLineNo));
            }
            writer.newLine();
        }
		/*final MethodInfo method = pdg.unit;

		writer.write("subgraph cluster");
		writer.write(Integer.toString(createdGraphNumber));
		writer.write(" {");
		writer.newLine();

		writer.write("label = \"");
		writer.write(getMethodSignature(method));
		writer.write("\";");
		writer.newLine();

		final Map<PDGNode<?>, Integer> nodeLabels = new HashMap<PDGNode<?>, Integer>();
		for (final PDGNode<?> node : pdg.getAllNodes()) {
			nodeLabels.put(node, nodeLabels.size());
		}

		for (final Map.Entry<PDGNode<?>, Integer> entry : nodeLabels.entrySet()) {
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(entry.getValue()));
			writer.write(" [label = \"");
			writer.write(entry.getKey().getText().replace("\"", "\\\"")
					.replace("\\\\\"", "\\\\\\\""));
			writer.write("\"");

			final PDGNode<?> node = entry.getKey();
			if (node instanceof PDGMethodEnterNode) {
				writer.write(", fillcolor = aquamarine");
			} else if (pdg.getExitNodes().contains(node)) {
				writer.write(", fillcolor = deeppink");
			} else if (node instanceof PDGParameterNode) {
				writer.write(", fillcolor = tomato");
			} else {
				writer.write(", fillcolor = white");
			}

			if (node instanceof PDGControlNode) {
				writer.write(", shape = diamond");
			} else if (node instanceof PDGParameterNode) {
				writer.write(", shape = box");
			} else {
				writer.write(", shape = ellipse");
			}

			writer.write("];");
			writer.newLine();
		}*/

        for (final PDGEdge edge : pdg.getAllEdges()) {
            if (!edge.getDependenceString().equals("")) {
                LineInfo fromNode = hashMap.get(createdGraphNumber + "." + nodeLabels.get(edge.fromNode));
                LineInfo toNode = hashMap.get(createdGraphNumber + "." + nodeLabels.get(edge.toNode));
                writer.write("estart: ");
                writer.write(fromNode.label.replace("\n", "").replace("\r", ""));
                writer.newLine();
                writer.write("eend: ");
                writer.write(toNode.label.replace("\n", "").replace("\r", ""));
                writer.write(" ");
                writer.write(fromNode.lineNo);
                writer.write("!");
                writer.write(toNode.lineNo);
                writer.newLine();
            }
        }
		/*for (final PDGEdge edge : pdg.getAllEdges()) {
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(nodeLabels.get(edge.fromNode)));
			writer.write(" -> ");
			writer.write(Integer.toString(createdGraphNumber));
			writer.write(".");
			writer.write(Integer.toString(nodeLabels.get(edge.toNode)));
			if (edge instanceof PDGDataDependenceEdge) {
				writer.write(" [label=\""
						+ edge.getDependenceString() + "\"]");
			} else if (edge instanceof PDGControlDependenceEdge) {
				writer.write(" [label=\""
						+ edge.getDependenceString() + "\"]");
			} else if (edge instanceof PDGExecutionDependenceEdge) {
				writer.write(" [label=\""
						+ edge.getDependenceString() + "\"]");
			}
			writer.write(";");
			writer.newLine();
		}*/

        writer.write("}");
        writer.newLine();
    }

    static private List<File> getFiles(final File file) {

        final List<File> files = new ArrayList<File>();

        if (file.isFile() && file.getName().endsWith(".java")) {
            files.add(file);
        } else if (file.isDirectory()) {
            for (final File child : file.listFiles()) {
                final List<File> children = getFiles(child);
                files.addAll(children);
            }
        }

        return files;
    }

    static private String getMethodSignature(final MethodInfo method) {

        String text = method.name + " <" + method.startLine + "..." + method.endLine + ">";

        return text;
    }
}
