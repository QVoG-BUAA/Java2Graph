package cn.edu.j2graph.qvog.j2graph;

import cn.edu.j2graph.qvog.j2graph.g2db.ParseAndStore;
import cn.edu.j2graph.qvog.j2graph.j2cg.cg.SpoonCGProcessor;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.FileStart;
import cn.edu.j2graph.qvog.j2graph.j2cpg.graphviz.Config;
import cn.edu.j2graph.qvog.j2graph.j2cpg.graphviz.Writer;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        var cmd = parseArgs(args);

        Config.INPUT_PATH = cmd.getOptionValue("input");
        Config.OUTPUT_PATH = cmd.getOptionValue("output");
        Config.CONFIG_PATH = cmd.getOptionValue("config", Config.CONFIG_PATH);

        long startTime = System.currentTimeMillis();
        List<FileStart> fileStartList = Writer.start();
        SpoonCGProcessor.start();
        long endTime = System.currentTimeMillis();
        System.out.printf("Graph construction time: %dms\n", endTime - startTime);

        ParseAndStore.start(fileStartList);

        System.exit(0);
    }

    private static CommandLine parseArgs(String[] args) {
        Options options = new Options();

        // set input directory
        options.addOption(Option.builder("i")
                .longOpt("input")
                .desc("input directory path")
                .hasArg()
                .argName("input")
                .required()
                .build());

        // set temporary output directory
        options.addOption(Option.builder("o")
                .longOpt("output")
                .desc("output directory path")
                .hasArg()
                .argName("output")
                .required()
                .build());

        // set configuration file
        options.addOption(new Option("c", "config", true, "configuration file path"));

        options.addOption(new Option("h", "help", false, "print this message"));

        CommandLineParser parser = new DefaultParser();
        try {
            var cmd = parser.parse(options, args);
            if (cmd.hasOption("help")) {
                new HelpFormatter().printHelp("Java2Graph", options);
                System.exit(0);
            }
            return cmd;
        } catch (ParseException e) {
            new HelpFormatter().printHelp("Java2Graph", options);
            System.exit(1);
        }

        return null;
    }
}
