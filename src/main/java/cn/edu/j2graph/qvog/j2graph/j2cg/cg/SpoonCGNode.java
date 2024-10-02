package cn.edu.j2graph.qvog.j2graph.j2cg.cg;

import java.util.LinkedList;
import java.util.List;

/**
 * 调用图节点
 *
 * @author 罗贤超
 * @since 2023/10/30 14:34
 */
public class SpoonCGNode {
    private String methodQualifiedName;
    private List<SpoonCGNode> spoonCGNodeList;
    private String filePath;

    public SpoonCGNode(String methodQualifiedName, String filePath) {
        this.methodQualifiedName = methodQualifiedName;
        this.filePath = filePath;
        this.spoonCGNodeList = new LinkedList<>();
    }

    public SpoonCGNode(String methodQualifiedName, List<SpoonCGNode> spoonCGNodeList) {
        this.methodQualifiedName = methodQualifiedName;
        this.spoonCGNodeList = spoonCGNodeList;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMethodQualifiedName() {
        return methodQualifiedName;
    }

    public void setMethodQualifiedName(String methodQualifiedName) {
        this.methodQualifiedName = methodQualifiedName;
    }

    public List<SpoonCGNode> getSpoonCGDotList() {
        return spoonCGNodeList;
    }

    public void setSpoonCGDotList(List<SpoonCGNode> spoonCGNodeList) {
        this.spoonCGNodeList = spoonCGNodeList;
    }

    public boolean appendSpoonCGDotList(SpoonCGNode spoonCGNode) {
        return this.spoonCGNodeList.add(spoonCGNode);
    }
}