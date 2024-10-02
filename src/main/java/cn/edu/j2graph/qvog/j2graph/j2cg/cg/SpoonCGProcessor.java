package cn.edu.j2graph.qvog.j2graph.j2cg.cg;

import cn.edu.j2graph.qvog.j2graph.j2cpg.graphviz.Config;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtMethod;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;


/**
 * Spoon处理器
 *
 * @author 罗贤超
 * @since 2023/10/30 11:34
 */
public class SpoonCGProcessor extends AbstractProcessor<CtMethod<?>> { // 泛型CtMethod 表示对方法级别的处理

    private final SpoonCGVisitor spoonCGVisitor = new SpoonCGVisitor();

    public static void start() {
        try {
            String inputPath = Path.of(Config.INPUT_PATH).toString();
            String outputPath = Path.of(Config.OUTPUT_PATH, "CG").toString();
            delete(outputPath);
            SpoonCGProcessor spoonCGProcessor = new SpoonCGProcessor();

            spoonCGProcessor.build(inputPath, outputPath);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
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

    public void build(String inputPath, String outputPath) throws IOException {

        Launcher launcher = new Launcher();

        // 设置源码目录
        launcher.addInputResource(inputPath);
        // 设置输出目录（可选）  该输出就是你对代码修改过后的结果，如果你只是对代码进行读取操作那么不需要设备该目录
        launcher.setSourceOutputDirectory(Path.of(Config.OUTPUT_PATH, "spoon").toString());

        // 添加自定义Processor (MySpoonProcessor)
        launcher.addProcessor(this);

        // 运行Spoon
        launcher.run();

        // 获取方法节点数据
        Map<String, SpoonCGNode> spoonCGNodeMap = this.spoonCGVisitor.getSpoonCGNodeMap();

        FileUtils.writeDotFile(inputPath, outputPath, spoonCGNodeMap);
    }

    @Override
    public void process(CtMethod element) {
        element.accept(spoonCGVisitor);
    }
}