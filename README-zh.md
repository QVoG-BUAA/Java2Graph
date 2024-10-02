# Java2Graph

> Using JDT to analyse Java code, author ppz

## 项目构建

在 IDEA 中，执行 `maven install` 即可构建项目，生成的 jar 包位于 `target` 目录下。

Java CPG 的构建主要分为三个模块：

- `j2cpg` 负责将 Java 代码转换为基础的 CPG 图文件，包括 `ast`、`cfg`、`pdg` 这三种图。
- `j2cg` 负责将 Java 代码转换为函数调用图文件。
- `g2db` 负责解析前两者生成的图文件并将图存入数据库。

> 由于解析 AST 的工具（JDT）和生成函数调用图的工具（Spoon）冲突，因此分为了不同的模块。

## 项目运行

项目运行时，需要提供配置文件。在 `Java2Graph.jar` 同级目录下，创建 `config.json`，格式如下，其中指定了 Gremlin 服务器的地址。

```json
{
    "host": <ip>,
    "port": 8182
}
```

该配置文件的路径可以通过命令行参数指定，完整的命令行参数如下。

```
usage: Java2Graph
 -c,--config <arg>      configuration file path
 -h,--help              print this message
 -i,--input <input>     input directory path
 -o,--output <output>   output directory path
```

将待测项目放入 `input` 目录下，执行 `java -jar Java2Graph.jar -i input -o output` 即构建 CPG，并存储至 Gremlin 服务器。
`output` 目录中存储了生成过程中的临时文件。
