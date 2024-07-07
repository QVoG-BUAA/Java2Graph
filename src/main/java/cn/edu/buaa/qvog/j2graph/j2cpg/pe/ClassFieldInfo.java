package cn.edu.buaa.qvog.j2graph.j2cpg.pe;

import java.util.ArrayList;
import java.util.List;

public class ClassFieldInfo {
    public String className;
    public List<FieldInfo> field = new ArrayList<>();

    public ClassFieldInfo(String className) {
        this.className = className;
    }
}
