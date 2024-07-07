# J2CPG

> Using JDT to analyse Java code, author ppz

## 项目结构

Java CPG 的构建主要分为三个模块：

- `j2cpg` 负责将 Java 代码转换为基础的 CPG 图文件，包括 `ast`、`cfg`、`pdg` 这三种图。
- `j2cg` 负责将 Java 代码转换为函数调用图文件。
- `g2db` 负责解析前两者生成的图文件并将图存入数据库。

> 由于解析 AST 的工具（JDT）和生成函数调用图的工具（Spoon）冲突，因此分为了不同的模块。

## 项目运行

首先先执行前两个模块的主函数：

j2astcfgpg.src.main.java.graphviz.Writer.main()

j2cg.src.main.java.cg.SpoonCGProcessor.main()

再执行最后一个模块的主函数

g2db.src.main.ParseAndStore.main()

即可实现将java代码转换为数据库中的图。

#### 注明

需要检测的整个java项目或java代码放在./input下，四种中间图文件将储存在./output下。

./spooned文件夹下存储的是冗余信息，后续考虑删除。
