package cn.edu.j2graph.qvog.j2graph.g2db.database;

import java.util.ArrayList;
import java.util.List;

public class SingleFileTotal {
    public String fileName;
    public String packageName;
    public List<String> importNameList = new ArrayList<>();
    public List<Code> codeList = new ArrayList<>();
    public List<CFG> cfgList = new ArrayList<>();
    public List<PDG> pdgList = new ArrayList<>();
    public List<Method> methodList = new ArrayList<>();
}
