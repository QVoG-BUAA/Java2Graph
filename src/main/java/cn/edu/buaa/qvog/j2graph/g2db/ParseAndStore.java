package cn.edu.buaa.qvog.j2graph.g2db;

import cn.edu.buaa.qvog.j2graph.g2db.database.*;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.*;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.statement.IfStatement;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.statement.Statement;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.statement.TryStatement;
import cn.edu.buaa.qvog.j2graph.j2cpg.graphviz.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.buaa.qvog.j2graph.g2db.StoreDB.store;

public class ParseAndStore {
    public static int nodeId = 1;

    public static void start(List<FileStart> fileStartList) throws IOException {
        long startTime = System.currentTimeMillis();
        AllFileTotal allFileTotal = oldReadFileAndParse(fileStartList);
        //AllFileTotal allFileTotal = newReadFileAndParse(fileStartList);
        long endTime = System.currentTimeMillis();
        System.out.print("本地解析图用时:");
        System.out.println(endTime - startTime);
        // oldStoreDB(allFileTotal);
        // oldStoreDB_2(allFileTotal);
        store(allFileTotal);
        //presentStoreDB(allFileTotal);
        //Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    }

    public static AllFileTotal oldReadFileAndParse(List<FileStart> fileStartList) throws IOException {
        File PDGDirectory = new File(Path.of(Config.OUTPUT_PATH, "PDG").toString());
        File[] PDGFiles = PDGDirectory.listFiles();
        AllFileTotal allFileTotal = new AllFileTotal();
        FileSystem fileSystem = new FileSystem();
        if (PDGFiles != null && PDGFiles.length > 0) {
            for (File PDGFile : PDGFiles) {
                //reBuild(PDGFile);
                String hashCode = PDGFile.getName().substring(0, PDGFile.getName().indexOf('.'));
                //File ASTFile = new File("output/AST/" + hashCode + ".json");
                FileStart fileStart = getAST(hashCode, fileStartList);
                File CFGFile = new File(Path.of(Config.OUTPUT_PATH, "CFG", hashCode + ".txt").toString());
                if (CFGFile.exists()) {
                    SingleFileTotal singleFileTotal = parsePDG(PDGFile, fileStart);
                    SingleFileTotal tmpSingleFileTotal = parseCFG(CFGFile, fileStart);
                    cn.edu.buaa.qvog.j2graph.g2db.database.File file = new cn.edu.buaa.qvog.j2graph.g2db.database.File(singleFileTotal.fileName.replace("\\", "/"));
                    fileSystem.fileList.add(file);
                    fileSystem.folderList.addAll(getFolderListbyFilePath(fileSystem.folderList, file.fileName));
                    for (Code CFGCode : tmpSingleFileTotal.codeList) {
                        boolean hasCode = false;
                        for (Code PDGCode : singleFileTotal.codeList) {
                            if (CFGCode.codeLabel.equals(PDGCode.codeLabel) && CFGCode.lineno.equals(PDGCode.lineno)) {
                                hasCode = true;
                                break;
                            }
                        }
                        if (!hasCode) {
                            singleFileTotal.codeList.add(CFGCode);
                        }
                    }
                    singleFileTotal.cfgList.addAll(tmpSingleFileTotal.cfgList);
                    allFileTotal.singleFileTotalList.add(singleFileTotal);
                } else {
                    System.out.println("Lack AST or CFG!");
                }
            }
        } else {
            System.out.println("Null Directory!");
        }
        File CGSumDirectory = new File(Path.of(Config.OUTPUT_PATH, "CG").toString());
        File[] CGDirectories = CGSumDirectory.listFiles();
        if (CGDirectories != null) {
            for (File CGDirectory : CGDirectories) {
                File[] CGFiles = CGDirectory.listFiles();
                if (CGFiles != null) {
                    for (File CGFile : CGFiles) {
                        allFileTotal.cgList.addAll(parseCG(CGFile, allFileTotal));
                    }
                }
            }
        }
        return allFileTotal;
    }

    /*public static AllFileTotal newReadFileAndParse(List<FileStart> fileStartList) throws IOException {
        File PDGDirectory = new File("output/PDG/");
        File[] PDGFiles = PDGDirectory.listFiles();
        AllFileTotal allFileTotal = new AllFileTotal();
        FileSystem fileSystem = new FileSystem();
        if (PDGFiles != null && PDGFiles.length > 0) {
            for (File PDGFile : PDGFiles) {
                //reBuild(PDGFile);
                String hashCode = PDGFile.getName().substring(0, PDGFile.getName().indexOf('.'));
                //File ASTFile = new File("output/AST/" + hashCode + ".json");
                FileStart fileStart = getAST(hashCode,fileStartList);
                File CFGFile = new File("output/CFG/" + hashCode + ".txt");
                if (CFGFile.exists()) {
                    SingleFileTotal singleFileTotal = parsePDG(PDGFile, fileStart);
                    SingleFileTotal tmpSingleFileTotal = parseCFG(CFGFile, fileStart, singleFileTotal.methodList);
                    g2db.database.File file = new g2db.database.File(singleFileTotal.fileName.replace("\\", "/"));
                    fileSystem.fileList.add(file);
                    fileSystem.folderList.addAll(getFolderListbyFilePath(fileSystem.folderList, file.fileName));
                    for (Code CFGCode : tmpSingleFileTotal.codeList) {
                        boolean hasCode = false;
                        for (Code PDGCode : singleFileTotal.codeList) {
                            if (CFGCode.codeLabel.equals(PDGCode.codeLabel) && CFGCode.lineno.equals(PDGCode.lineno)) {
                                hasCode = true;
                                break;
                            }
                        }
                        if (!hasCode) {
                            singleFileTotal.codeList.add(CFGCode);
                        }
                    }
                    singleFileTotal.cfgList.addAll(tmpSingleFileTotal.cfgList);
                    allFileTotal.singleFileTotalList.add(singleFileTotal);
                } else {
                    System.out.println("Lack AST or CFG!");
                }
            }
        } else {
            System.out.println("Null Directory!");
        }
        File CGSumDirectory = new File("output/CG/");
        File[] CGDirectories = CGSumDirectory.listFiles();
        if(CGDirectories != null && CGDirectories.length > 0){
            for(File CGDirectory : CGDirectories){
                File[] CGFiles = CGDirectory.listFiles();
                if(CGFiles != null && CGFiles.length > 0){
                    for(File CGFile:CGFiles){
                        allFileTotal.cgList.addAll(parseCG(CGFile,allFileTotal));
                    }
                }
            }
        }
        return allFileTotal;
    }*/

    public static FileStart getAST(String hashCode, List<FileStart> fileStartList) {
        for (FileStart fileStart : fileStartList) {
            if (fileStart.hashCode.equals(hashCode)) {
                return fileStart;
            }
        }
        System.out.println("Something in AST is wrong");
        return null;
    }

    private static List<Folder> getFolderListbyFilePath(List<Folder> folderList, String filePath) {
        String[] splitFolderList = filePath.split("\\\\");
        String Folder = "";
        List<Folder> result = new ArrayList<>();
        boolean hasFolder = false;
        for (int i = 0; i < splitFolderList.length; i++) {
            if (i != 0) {
                Folder += "/";
            }
            Folder += splitFolderList[i];
            for (Folder folder : folderList) {
                if (folder.folderName.equals(Folder)) {
                    hasFolder = true;
                    break;
                }
            }
            if (!hasFolder) {
                for (int j = i + 1; j < splitFolderList.length; j++) {
                    result.add(new Folder(Folder));
                    Folder += "/";
                    Folder += splitFolderList[j];
                }
                break;
            }
        }
        return result;
    }

    public static SingleFileTotal parsePDG(File file, FileStart fileStart) throws IOException {
        BufferedReader FileReader = new BufferedReader(new FileReader(file));
        String rootDir = System.getProperty("user.dir").replace("\\", "/") + "/";
        String Line;
        SingleFileTotal singleFileTotal = new SingleFileTotal();
        String method = "";
        String className = "";
        String classFieldName = "";
        String classFieldLabel = "";
        String classFieldLine = "";
        boolean refresh = true;
        boolean inField = false;
        while ((Line = FileReader.readLine()) != null) {
            if (Line.startsWith("classfield:") && !inField) {
                classFieldName = Line.substring(11);
                className = classFieldName;
                inField = true;
            } else if (inField && Line.equals("}")) {
                inField = false;
            } else if (Line.startsWith("v:") && !inField) {
                if (!Line.startsWith("Enter Enter", 3)) {
                    int firstSpace = Line.indexOf(" ");
                    int lastSpace = Line.lastIndexOf(" ");
                    String label = Line.substring(firstSpace + 1, lastSpace);
                    int startLineNo, endLineNo;
                    if (Line.substring(lastSpace + 1).contains("...")) {
                        startLineNo = Integer.parseInt(Line.substring(lastSpace + 1).split("\\.\\.\\.")[0]);
                        endLineNo = Integer.parseInt(Line.substring(lastSpace + 1).split("\\.\\.\\.")[1]);
                    } else {
                        startLineNo = Integer.parseInt(Line.substring(lastSpace + 1));
                        endLineNo = startLineNo;
                    }
                    String vertexASTJson = getVertexASTJson(label, startLineNo, endLineNo, fileStart);
                    singleFileTotal.codeList.add(new Code(vertexASTJson, Line.substring(lastSpace + 1), className + ":" + method,
                            singleFileTotal.fileName.replace(".java", "").replace("\\", "/"), label, String.valueOf(nodeId++)));
                } else {
                    int lastSpace = Line.lastIndexOf(" ");
                    int lastDot = Line.lastIndexOf("!");
                    String enterJson = new GsonBuilder().disableHtmlEscaping().create().toJson(new Enter());
                    /*singleFileTotal.codeList.add(new Code(enterJson,Line.substring(lastSpace+1,lastDot),className+":"+method,
                            singleFileTotal.fileName.replace(".java","").replace("\\","/"),"Enter Enter"));*/
                    if (refresh) {
                        refresh = false;
                        int methodListSize = singleFileTotal.methodList.size() - 1;
                        /*singleFileTotal.pdgList.add(new PDG(singleFileTotal.methodList.get(methodListSize).methodName,singleFileTotal.methodList.get(methodListSize).methodLineNo,"Enter Enter",Line.substring(lastSpace+1,lastDot),
                                singleFileTotal.methodList.get(methodListSize).methodAST,
                                getASTJsonbyLabelandLineno("Enter Enter",Line.substring(lastSpace+1,lastDot),singleFileTotal.codeList)
                        ));*/
                        /*singleFileTotal.pdgList.add(new PDG(singleFileTotal.methodList.get(methodListSize).methodName,singleFileTotal.methodList.get(methodListSize).methodLineNo,"Enter Enter",Line.substring(lastSpace+1,lastDot),
                                singleFileTotal.methodList.get(methodListSize).methodAST,
                                getASTJsonbyLabelandLineno("Enter Enter",Line.substring(lastSpace+1,lastDot),singleFileTotal.codeList),
                                singleFileTotal.codeList,
                                singleFileTotal.methodList
                        ));*/
                    }
                }
            } else if (Line.startsWith("v:") && inField) {
                int firstSpace = Line.indexOf(" ");
                int lastSpace = Line.lastIndexOf(" ");
                String label = Line.substring(firstSpace + 1, lastSpace);
                String vertexASTJson = label;
                singleFileTotal.codeList.add(new Code(vertexASTJson, Line.substring(lastSpace + 1), className + ":CLASSMEMBERVARIABLE",
                        singleFileTotal.fileName.replace(".java", "").replace("\\", "/"), label, String.valueOf(nodeId++)));
                classFieldLabel = label;
                classFieldLine = Line.substring(lastSpace + 1);
            } else if (Line.startsWith("FilePath:")) {
                singleFileTotal.fileName = Line.substring(9).replace(".java", "").replace("\\", "/").replace(rootDir, "");
            } else if (Line.startsWith("method:")) {
                String[] methodInfos = Line.split(" ");
                method = methodInfos[0].substring(7);
                String methodAST = new GsonBuilder().disableHtmlEscaping().create().toJson(new MethodEnter(getMethodArgs(methodInfos), methodInfos[methodInfos.length - 1].split(":")[0], methodInfos[methodInfos.length - 1].split(":")[1]));
                singleFileTotal.methodList.add(new Method(method, methodAST, methodInfos[methodInfos.length - 1].split(":")[0], singleFileTotal.fileName, getMethod(methodInfos), String.valueOf(nodeId++)));
                if (classFieldName.equals(className)) {
                    singleFileTotal.pdgList.add(new PDG(classFieldLabel, classFieldLine, method, methodInfos[methodInfos.length - 1].split(":")[0],
                            getASTJsonbyLabelandLineno(classFieldLabel, classFieldLine, singleFileTotal.codeList),
                            methodAST
                    ));
                    /*singleFileTotal.pdgList.add(new PDG(classFieldLabel,classFieldLine,method,methodInfos[methodInfos.length-1].split(":")[0],
                            getASTJsonbyLabelandLineno(classFieldLabel,classFieldLine,singleFileTotal.codeList),
                            methodAST,
                            singleFileTotal.codeList,
                            singleFileTotal.methodList
                    ));*/
                }
            } else if (Line.startsWith("estart:")) {
                if (!Line.substring(8).equals("Enter Enter")) {
                    String fromCodeLabel = Line.substring(8);
                    Line = FileReader.readLine();
                    int dotIndex = Line.lastIndexOf("!");
                    int lastSpace = Line.lastIndexOf(" ");
                    String toCodeLabel = Line.substring(6, lastSpace);
                    String fromCodeLabelLine = Line.substring(lastSpace + 1, dotIndex);
                    String toCodeLabelLine = Line.substring(dotIndex + 1);
                    singleFileTotal.pdgList.add(new PDG(fromCodeLabel, fromCodeLabelLine, toCodeLabel, toCodeLabelLine,
                            getASTJsonbyLabelandLineno(fromCodeLabel, fromCodeLabelLine, singleFileTotal.codeList),
                            getASTJsonbyLabelandLineno(toCodeLabel, toCodeLabelLine, singleFileTotal.codeList)
                    ));
                    /*singleFileTotal.pdgList.add(new PDG(fromCodeLabel,fromCodeLabelLine,toCodeLabel,toCodeLabelLine,
                            getASTJsonbyLabelandLineno(fromCodeLabel,fromCodeLabelLine,singleFileTotal.codeList),
                            getASTJsonbyLabelandLineno(toCodeLabel,toCodeLabelLine,singleFileTotal.codeList),
                            singleFileTotal.codeList,
                            singleFileTotal.methodList
                    ));*/
                } else {
                    /*String fromCodeLabel = Line.substring(8);
                    Line=FileReader.readLine();
                    int dotIndex = Line.lastIndexOf("!");
                    int lastSpace = Line.lastIndexOf(" ");
                    String toCodeLabel = Line.substring(6,lastSpace);
                    String fromCodeLabelLine = Line.substring(lastSpace+1,dotIndex);
                    String toCodeLabelLine = Line.substring(dotIndex+1);
                    singleFileTotal.pdgList.add(new PDG(fromCodeLabel,fromCodeLabelLine,toCodeLabel,toCodeLabelLine,
                            getASTJsonbyLabelandLineno(fromCodeLabel,fromCodeLabelLine,singleFileTotal.codeList),
                            getASTJsonbyLabelandLineno(toCodeLabel,toCodeLabelLine,singleFileTotal.codeList)
                    ));*/
                    /*singleFileTotal.pdgList.add(new PDG(fromCodeLabel,fromCodeLabelLine,toCodeLabel,toCodeLabelLine,
                            getASTJsonbyLabelandLineno(fromCodeLabel,fromCodeLabelLine,singleFileTotal.codeList),
                            getASTJsonbyLabelandLineno(toCodeLabel,toCodeLabelLine,singleFileTotal.codeList),
                            singleFileTotal.codeList,
                            singleFileTotal.methodList
                    ));*/
                }
            } else if (Line.startsWith("class:")) {
                className = Line.substring(6);
            } else if (Line.startsWith("{")) {
                refresh = true;
            } else if (Line.startsWith("package:")) {
                singleFileTotal.packageName = Line.substring(8);
            } else if (Line.startsWith("import:")) {
                singleFileTotal.importNameList.add(Line.substring(7));
            }
        }
        /*for(Method method1:singleFileTotal.methodList){
            singleFileTotal.codeList.add(new Code(method1.methodAST,method1.methodLineNo,method1.methodName,method1.belongFile,method1.methodCode));
        }*/
        FileReader.close();
        return singleFileTotal;
    }

    private static String getMethod(String[] methodInfos) {
        String method = methodInfos[0].substring(7);
        method = method + "(";
        for (int i = 1; i < methodInfos.length - 1; i++) {
            method = method + methodInfos[i];
            method = method + ",";
        }
        if (method.endsWith(",")) {
            method = method.substring(0, method.length() - 1);
        }
        method = method + ")";
        return method;
    }

    private static List<Arg> getMethodArgs(String[] methodInfos) {
        List<Arg> args = new ArrayList<>();
        int LineNo = Integer.parseInt(methodInfos[methodInfos.length - 1].substring(0, methodInfos[methodInfos.length - 1].indexOf(":")));
        for (int i = 1; i < methodInfos.length - 1; i++) {
            String argClass = methodInfos[i].substring(0, methodInfos[i].indexOf(":"));
            boolean argIsArray = argClass.endsWith("[]");
            if (argIsArray) {
                argClass = argClass.substring(argClass.length() - 2);
            }
            String argName = methodInfos[i].substring(methodInfos[i].indexOf(":") + 1);
            Arg arg = new Arg();
            arg.argClass = argClass;
            arg.isArray = argIsArray;
            arg.arg = argName;
            arg.lineno = LineNo;
            arg.end_lineno = LineNo;
            args.add(arg);
        }
        return args;
    }

    public static SingleFileTotal parseCFG(File file, FileStart fileStart/*,List<Method> methodList*/) throws IOException {
        BufferedReader FileReader = new BufferedReader(new FileReader(file));
        String rootDir = System.getProperty("user.dir").replace("\\", "/") + "/";
        String Line;
        SingleFileTotal singleFileTotal = new SingleFileTotal();
        String method = "";
        String className = "";
        String classFieldName = "";
        String classFieldLabel = "";
        String classFieldLine = "";
        Method methodInfo = new Method("", "", "", "", "", "");
        boolean refresh = true;
        boolean inField = false;
        while ((Line = FileReader.readLine()) != null) {
            if (Line.startsWith("classfield") && !inField) {
                classFieldName = Line.substring(11);
                className = classFieldName;
                inField = true;
            } else if (inField && Line.equals("}")) {
                inField = false;
            } else if (Line.startsWith("v:") && !inField) {
                int firstSpace = Line.indexOf(" ");
                int lastSpace = Line.lastIndexOf(" ");
                String label = Line.substring(firstSpace + 1, lastSpace);
                int startLineNo, endLineNo;
                if (Line.substring(lastSpace + 1).contains("...")) {
                    startLineNo = Integer.parseInt(Line.substring(lastSpace + 1).split("\\.\\.\\.")[0]);
                    endLineNo = Integer.parseInt(Line.substring(lastSpace + 1).split("\\.\\.\\.")[1]);
                } else {
                    startLineNo = Integer.parseInt(Line.substring(lastSpace + 1));
                    endLineNo = startLineNo;
                }
                String vertexASTJson = getVertexASTJson(label, startLineNo, endLineNo, fileStart);
                singleFileTotal.codeList.add(new Code(vertexASTJson, Line.substring(lastSpace + 1), className + ":" + method,
                        singleFileTotal.fileName.replace(".java", "").replace("\\", "/"), label, String.valueOf(nodeId++)));
                if (refresh) {
                    refresh = false;
                    singleFileTotal.cfgList.add(new CFG(methodInfo.methodName, methodInfo.methodLineNo, label, Line.substring(lastSpace + 1),
                            methodInfo.methodAST,
                            getASTJsonbyLabelandLineno(label, Line.substring(lastSpace + 1), singleFileTotal.codeList)
                    ));
                    /*singleFileTotal.cfgList.add(new CFG(methodInfo.methodName,methodInfo.methodLineNo,label,Line.substring(lastSpace+1),
                            methodInfo.methodAST,
                            getASTJsonbyLabelandLineno(label,Line.substring(lastSpace+1),singleFileTotal.codeList),
                            singleFileTotal.codeList,
                            methodList
                    ));*/
                }
            } else if (Line.startsWith("v:") && inField) {
                int firstSpace = Line.indexOf(" ");
                int lastSpace = Line.lastIndexOf(" ");
                String label = Line.substring(firstSpace + 1, lastSpace);
                String vertexASTJson = label;
                singleFileTotal.codeList.add(new Code(vertexASTJson, Line.substring(lastSpace + 1), className + ":CLASSMEMBERVARIABLE",
                        singleFileTotal.fileName.replace(".java", "").replace("\\", "/"), label, String.valueOf(nodeId++)));
                classFieldLabel = label;
                classFieldLine = Line.substring(lastSpace + 1);
            } else if (Line.startsWith("FilePath:")) {
                singleFileTotal.fileName = Line.substring(9).replace(".java", "").replace("\\", "/").replace(rootDir, "");
            } else if (Line.startsWith("method:")) {
                String[] methodInfos = Line.split(" ");
                method = methodInfos[0].substring(7);
                String methodAST = new GsonBuilder().disableHtmlEscaping().create().toJson(new MethodEnter(getMethodArgs(methodInfos), methodInfos[methodInfos.length - 1].split(":")[0], methodInfos[methodInfos.length - 1].split(":")[1]));
                methodInfo = new Method(method, methodAST, methodInfos[methodInfos.length - 1].split(":")[0], singleFileTotal.fileName, getMethod(methodInfos), String.valueOf(nodeId++));
                if (classFieldName.equals(className)) {
                    singleFileTotal.cfgList.add(new CFG(classFieldLabel, classFieldLine, method, methodInfos[methodInfos.length - 1].split(":")[0],
                            getASTJsonbyLabelandLineno(classFieldLabel, classFieldLine, singleFileTotal.codeList),
                            methodAST
                    ));
                    /*singleFileTotal.cfgList.add(new CFG(classFieldLabel,classFieldLine,method,methodInfos[methodInfos.length-1].split(":")[0],
                            getASTJsonbyLabelandLineno(classFieldLabel,classFieldLine,singleFileTotal.codeList),
                            methodAST,
                            singleFileTotal.codeList,
                            methodList
                    ));*/
                }
            } else if (Line.startsWith("estart:")) {
                String fromCodeLabel = Line.substring(8);
                Line = FileReader.readLine();
                int dotIndex = Line.lastIndexOf("!");
                int lastSpace = Line.lastIndexOf(" ");
                String toCodeLabel = Line.substring(6, lastSpace);
                String fromCodeLabelLine = Line.substring(lastSpace + 1, dotIndex);
                String toCodeLabelLine = Line.substring(dotIndex + 1);
                singleFileTotal.cfgList.add(new CFG(fromCodeLabel, fromCodeLabelLine, toCodeLabel, toCodeLabelLine,
                        getASTJsonbyLabelandLineno(fromCodeLabel, fromCodeLabelLine, singleFileTotal.codeList),
                        getASTJsonbyLabelandLineno(toCodeLabel, toCodeLabelLine, singleFileTotal.codeList)
                ));
                /*singleFileTotal.cfgList.add(new CFG(fromCodeLabel,fromCodeLabelLine,toCodeLabel,toCodeLabelLine,
                        getASTJsonbyLabelandLineno(fromCodeLabel,fromCodeLabelLine,singleFileTotal.codeList),
                        getASTJsonbyLabelandLineno(toCodeLabel,toCodeLabelLine,singleFileTotal.codeList),
                        singleFileTotal.codeList,
                        methodList
                ));*/
            } else if (Line.startsWith("class:")) {
                className = Line.substring(6);
            } else if (Line.startsWith("classfield:")) {
                className = Line.substring(11);
            } else if (Line.startsWith("{")) {
                refresh = true;
            }
        }
        FileReader.close();
        return singleFileTotal;
    }

    public static List<CG> parseCG(File file, AllFileTotal allFileTotal) throws IOException {
        BufferedReader FileReader = new BufferedReader(new FileReader(file));
        String Line;
        List<CG> cgList = new ArrayList<>();
        String filePath;
        String rootPath;
        while ((Line = FileReader.readLine()) != null) {
            if (Line.startsWith("FilePath:")) {
                filePath = Line.substring(9);
            } else if (Line.startsWith("RootPath:")) {
                rootPath = Line.substring(9);
            } else if (Line.startsWith("method:")) {
            } else if (Line.startsWith("{")) {
            } else if (Line.startsWith("}")) {
            } else {
                Line = Line.replace(" ", "").replace(";", "");
                String[] node = Line.split("->");
                String fromCode = node[0];
                String toCode = node[1];
                String fromCodeLineNo = "";
                if (node.length > 2) {
                    fromCodeLineNo = node[2];
                }
                cgList.add(getCGNode(fromCode, toCode, allFileTotal, fromCodeLineNo));
            }
        }
        FileReader.close();
        return cgList;
    }

    private static String getVertexASTJson(String label, int startLineNo, int endLineNo, FileStart fileStart) {
        String vertexASTJson = "";
        for (FuncStart funcStart : fileStart.funcStartList) {
            if (funcStart.lineno <= startLineNo && funcStart.end_lineno >= endLineNo) {
                vertexASTJson = getVertexASTJsonInASTNodeJSON(label, startLineNo, endLineNo, funcStart.FunctionDef.body);
                break;
            } else if (funcStart.lineno == startLineNo) {
                vertexASTJson = getFormalParameterVertexASTJsonInASTNodeJSON(label, funcStart.FunctionDef.args.args);
                break;
            }
        }
        return vertexASTJson;
    }

    private static String getFormalParameterVertexASTJsonInASTNodeJSON(String label, List<Arg> args) {
        String vertexASTJson = "";
        String formalParameterLabel = label.substring(label.lastIndexOf(" ") + 1);
        for (Arg arg : args) {
            if (formalParameterLabel.equals(arg.arg)) {
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                vertexASTJson = gson.toJson(arg);
                break;
            }
        }
        return vertexASTJson;
    }

    private static String getVertexASTJsonInASTNodeJSON(String label, int startLineNo, int endLineNo, List<Statement> statementList) {
        String vertexASTJson = "";
        for (Statement statement : statementList) {
            if (statement.lineno != statement.end_lineno) {
                /*if(label.startsWith("data = Byte.parseByte(stringNumber.trim());")){
                    System.out.println(startLineNo);
                    System.out.println(endLineNo);
                    //System.out.println(funcStart.lineno);
                    //System.out.println(funcStart.end_lineno);
                    System.out.println(statement.lineno);
                    System.out.println(statement.end_lineno);
                }*/
                if (statement.lineno <= startLineNo && statement.end_lineno >= endLineNo) {
                    vertexASTJson = getVertexASTJsonInASTNodeJSONInLongStatement(label, startLineNo, endLineNo, statement);
                    vertexASTJson = vertexASTJson.replace("\\\\\\", "\\");
                    break;
                } else {
                    continue;
                }
            }
            if (statement.lineno == startLineNo && statement.end_lineno == endLineNo) {
                if (label.replace(" ", "").equals(statement.name.replace(" ", ""))) {
                    vertexASTJson = getStatementJson(statement);
                    vertexASTJson = vertexASTJson.replace("\\\\\\", "\\");
                    break;
                }
            }
        }
        return vertexASTJson;
    }

    private static String getVertexASTJsonInASTNodeJSONInLongStatement(String label, int startLineNo, int endLineNo, Statement statement) {
        String vertexASTJson = "";
        if (statement instanceof IfStatement ifStatement) {
            if (ifStatement.lineno == startLineNo) {
                vertexASTJson = new GsonBuilder().disableHtmlEscaping().create().toJson(ifStatement.test);
            } else {
                vertexASTJson = getVertexASTJsonInASTNodeJSON(label, startLineNo, endLineNo, ifStatement.body);
            }
        } else if (statement instanceof TryStatement tryStatement) {
            if (tryStatement.try_lineno <= startLineNo && tryStatement.try_end_lineno >= endLineNo) {
                vertexASTJson = getVertexASTJsonInASTNodeJSON(label, startLineNo, endLineNo, tryStatement.tryBody);
            } else if (tryStatement.finally_lineno <= startLineNo && tryStatement.finally_end_lineno >= endLineNo) {
                vertexASTJson = getVertexASTJsonInASTNodeJSON(label, startLineNo, endLineNo, tryStatement.finallyBody);
            } else {
                for (ASTNode astNode : tryStatement.catchClauses) {
                    if (astNode instanceof CatchClause catchClause) {
                        if (catchClause.lineno <= startLineNo && catchClause.end_lineno >= endLineNo) {
                            vertexASTJson = getVertexASTJsonInASTNodeJSON(label, startLineNo, endLineNo, catchClause.body);
                        }
                    }
                }
            }
        }
        return vertexASTJson;
    }

    private static String getStatementJson(Statement statement) {
        String statementJson = "";
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        statementJson = gson.toJson(statement);
        return statementJson;
    }

    private static String getASTJsonbyLabelandLineno(String label, String lineno, List<Code> codeList) {
        for (Code code : codeList) {
            /*System.out.println(code.lineno);
            System.out.println(code.codeLabel);
            System.out.println(lineno);
            System.out.println(label);*/
            if (code.lineno.equals(lineno) && code.codeLabel.equals(label)) {
                return code.json;
            }
        }
        //System.out.println("NO!!!!!!!!!");
        return "Something wrong in correspond!";
    }

    private static CG getCGNode(String fromCode, String toCode, AllFileTotal allFileTotal, String fromCodeLineNo) {
        String fromCodeLabel = "";
        String fromCodeBelongFile = Path.of(Config.INPUT_PATH).toString();
        if (fromCode.startsWith(".")) {
            fromCode = fromCode.substring(1);
        }
        int leftkuohaooffromCode = fromCode.indexOf("(");
        int righkuohaooffromCode = fromCode.indexOf(")");
        String fromCodeMethod = fromCode.substring(0, leftkuohaooffromCode);
        String fromCodeParameter = fromCode.substring(leftkuohaooffromCode + 1, righkuohaooffromCode);
        String[] fromPathSplit = fromCodeMethod.split("\\.");
        String[] fromParameterSplit = fromCodeParameter.split(",");
        for (int i = 0; i < fromPathSplit.length - 2; i++) {
            fromCodeBelongFile += fromPathSplit[i];
            fromCodeBelongFile += "/";
        }
        fromCodeBelongFile += fromPathSplit[fromPathSplit.length - 2];
        fromCodeLabel = fromPathSplit[fromPathSplit.length - 1];
        String toCodeLabel = "";
        String toCodeBelongFile = Path.of(Config.INPUT_PATH).toString();
        String toCodeAST = "";
        if (toCode.startsWith(".")) {
            toCode = toCode.substring(1);
        }
        int leftkuohaooftoCode = toCode.indexOf("(");
        int righkuohaooftoCode = toCode.indexOf(")");
        String toCodeMethod = toCode.substring(0, leftkuohaooftoCode);
        String toCodeParameter = toCode.substring(leftkuohaooftoCode + 1, righkuohaooftoCode);
        String[] toPathSplit = toCodeMethod.split("\\.");
        String[] toParameterSplit = toCodeParameter.split(",");
        for (int i = 0; i < toPathSplit.length - 2; i++) {
            toCodeBelongFile += toPathSplit[i];
            toCodeBelongFile += "/";
        }
        if (toPathSplit.length >= 2) {
            toCodeBelongFile += toPathSplit[toPathSplit.length - 2];
        }
        toCodeLabel = toPathSplit[toPathSplit.length - 1];
        toCodeAST = toCodeBelongFile + ":" + toCodeLabel + "(";
        String parameter = toParameterSplit[0];
        toCodeAST += parameter;
        //if(parameter)else{}
        for (int i = 1; i < toParameterSplit.length; i++) {
            parameter = toParameterSplit[i];
            //if(parameter)else{}
            toCodeAST += ",";
            toCodeAST += parameter;
        }
        toCodeAST += ")";
        String fromCodeAST = getFromCodeAST(allFileTotal, fromCodeBelongFile, fromCodeLineNo);
        return new CG(fromCodeLabel, fromCodeBelongFile, toCodeLabel, fromCodeAST, toCodeAST, toCodeBelongFile, fromCodeLineNo);
    }

    private static String getFromCodeAST(AllFileTotal allFileTotal, String fromCodeBelongFile, String fromCodeLineNo) {
        String fromCodeAST = "";
        for (SingleFileTotal singleFileTotal : allFileTotal.singleFileTotalList) {
            if (singleFileTotal.fileName.equals(fromCodeBelongFile)) {
                for (Code code : singleFileTotal.codeList) {
                    if (code.lineno.equals(fromCodeLineNo)) {
                        fromCodeAST = code.json;
                    }
                }
            }
        }
        return fromCodeAST;
    }
    /*
    private static CG getFromCode(String fromCodeBelongFile,String fromCodeLabel,String fromClassName,String toCodeLabel,String toClassName,AllFileTotal allFileTotal){
        CG tmpCG = null;
        String funcDefName=fromClassName+":"+fromCodeLabel;
        Gson gson = new Gson();
        for(SingleFileTotal singleFileTotal : allFileTotal.singleFileTotalList){
            if(singleFileTotal.fileName.equals(fromCodeBelongFile)){
                HashMap<String,String> map = new HashMap<>();
                for(Code code : singleFileTotal.codeList){
                    if(code.functionDefName.equals(funcDefName)){
                        if(!code.json.equals("Enter Enter")){
                            InitialASTNode initialASTNode = gson.fromJson(code.json,InitialASTNode.class);
                            if(initialASTNode.type.equals("VariableDeclarationStatement")){
                                int space = initialASTNode.label.indexOf(" ");
                                int assign = initialASTNode.label.indexOf("=");
                                if(assign==-1){
                                    map.put(initialASTNode.label.substring(space+1).replace(" ",""),initialASTNode.label.substring(0,space));
                                }
                                else {
                                    map.put(initialASTNode.label.substring(space+1,assign).replace(" ",""),initialASTNode.label.substring(0,space));
                                }
                            }
                            else if(initialASTNode.type.equals("ExpressionStatement")&&initialASTNode.childNodeList.get(0).type.equals("MethodInvocation")){
                                if(initialASTNode.label.startsWith("new ")){
                                    int firstLeftkuohao = initialASTNode.label.indexOf("(");
                                    String className = initialASTNode.label.substring(4,firstLeftkuohao);
                                    int point = initialASTNode.label.indexOf(".");
                                    int lastLeftkuohao = initialASTNode.label.lastIndexOf("(");
                                    if(initialASTNode.label.substring(point+1,lastLeftkuohao).replace(" ","").equals(toCodeLabel)&&
                                            className.equals(toClassName)){
                                        tmpCG = new CG(code.json,code.lineno);
                                    }
                                }
                                else{
                                    int point = initialASTNode.label.indexOf(".");
                                    String varName = initialASTNode.label.substring(0,point).replace(" ","");
                                    String className = map.get(varName);
                                    int lastLeftkuohao = initialASTNode.label.lastIndexOf("(");
                                    if(initialASTNode.label.substring(point+1,lastLeftkuohao).replace(" ","").equals(toCodeLabel)&&
                                            className.equals(toClassName)){
                                        tmpCG = new CG(code.json,code.lineno);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(tmpCG==null){
            return new CG("","");
        }
        else {
            return tmpCG;
        }
    }*/
}
