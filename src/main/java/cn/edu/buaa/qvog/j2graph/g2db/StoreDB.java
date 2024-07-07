package cn.edu.buaa.qvog.j2graph.g2db;

import cn.edu.buaa.qvog.j2graph.g2db.database.*;
import cn.edu.buaa.qvog.j2graph.g2db.gremlin.GremlinInstance;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.*;
import java.util.concurrent.*;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.otherV;

public class StoreDB {

    private final static int BATCH_SIZE = 1000;
    private final static int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private final static ExecutorService vertexExecutor = Executors.newFixedThreadPool(NUM_THREADS);
    private final static ExecutorService edgeExecutor = Executors.newFixedThreadPool(NUM_THREADS / 2);

    private static final Map<String, Long> vertexMap = new ConcurrentHashMap<>();

    public static void oldStoreDB(AllFileTotal allFileTotal) {
        final int NUM_THREADS = 100;
        final int NUM_SUBTASKS = 100;
        GraphTraversalSource g = GremlinInstance.getInstance().get();
        long startStoreTime = System.currentTimeMillis();
        g.V().drop().iterate();
        try {
            ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
            //ExecutorService subtaskExecutor = Executors.newFixedThreadPool(NUM_SUBTASKS);
            ExecutorService cgExecutor = Executors.newFixedThreadPool(NUM_SUBTASKS);
            HashMap<String, String> nodeMap = new HashMap<>();
            for (SingleFileTotal singleFileTotal : allFileTotal.singleFileTotalList) {
                HashMap<String, String> singleFileNodeMap = new HashMap<>();
                ExecutorService subtaskExecutor = Executors.newFixedThreadPool(NUM_SUBTASKS);
                ExecutorService finalSubtaskExecutor1 = subtaskExecutor;
                Future<?> task1 = executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (Code code : singleFileTotal.codeList) {
                            finalSubtaskExecutor1.execute(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(singleFileTotal.fileName + "_code: " + code.codeLabel);
                                    System.out.println(singleFileTotal.fileName + "_code: " + code.json);
                                    System.out.println(singleFileTotal.fileName + "_code: " + code.lineno);
                                    g.addV("code").property("json", code.json).property("lineno", Integer.parseInt(code.lineno)).property("belongFile", code.belongFile).property("functionDefName", code.functionDefName).property("code", code.codeLabel).id().as(code.nodeId).next();
                                    String hash = code.json + code.lineno + code.belongFile;
                                    singleFileNodeMap.put(hash, code.nodeId);
                                }
                            });
                        }
                        System.out.println("CodeOver");
                    }
                });

                //g.tx().commit();
                task1.get();

                subtaskExecutor.shutdown();
                while (!subtaskExecutor.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                    //System.out.println("等待cpg提交");
                }
                subtaskExecutor = Executors.newFixedThreadPool(100);
                ExecutorService finalSubtaskExecutor2 = subtaskExecutor;

                Future<?> task2 = executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (Method method : singleFileTotal.methodList) {
                            finalSubtaskExecutor2.execute(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(singleFileTotal.fileName + "_method: " + method.methodAST);
                                    System.out.println(singleFileTotal.fileName + "_method: " + method.methodLineNo);
                                    System.out.println(singleFileTotal.fileName + "_method: " + method.belongFile);
                                    g.addV("code").property("json", method.methodAST).property("lineno", Integer.parseInt(method.methodLineNo)).property("belongFile", method.belongFile).property("functionDefName", "").property("code", method.methodCode).id().as(method.nodeId).next();
                                    String hash = method.methodAST + method.methodLineNo + method.belongFile;
                                    singleFileNodeMap.put(hash, method.nodeId);
                                    hash = method.methodAST + method.belongFile;
                                    singleFileNodeMap.put(hash, method.nodeId);
                                    hash = method.methodCode + method.belongFile;
                                    System.out.println(hash);
                                    System.out.println(666);
                                    singleFileNodeMap.put(hash, method.nodeId);
                                }
                            });
                        }
                        System.out.println("MethodOver");
                    }
                });

                g.tx().commit();

                task2.get();

                subtaskExecutor.shutdown();
                while (!subtaskExecutor.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                    //System.out.println("等待cpg提交");
                }
                subtaskExecutor = Executors.newFixedThreadPool(100);
                ExecutorService finalSubtaskExecutor3 = subtaskExecutor;

                Future<?> task3 = executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (CFG cfg : singleFileTotal.cfgList) {
                            finalSubtaskExecutor3.execute(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(singleFileTotal.fileName + "_cfg: " + cfg.fromCodeLabel);
                                    System.out.println(singleFileTotal.fileName + "_cfg: " + cfg.fromCodeAST);
                                    System.out.println(singleFileTotal.fileName + "_cfg: " + cfg.fromCodeLabelLine);
                                    System.out.println(singleFileTotal.fileName + "_cfg: " + cfg.toCodeLabel);
                                    System.out.println(singleFileTotal.fileName + "_cfg: " + cfg.toCodeAST);
                                    System.out.println(singleFileTotal.fileName + "_cfg: " + cfg.toCodeLabelLine);
                                    String fromNodeHash = cfg.fromCodeAST + cfg.fromCodeLabelLine + singleFileTotal.fileName;
                                    String toNodeHash = cfg.toCodeAST + cfg.toCodeLabelLine + singleFileTotal.fileName;
                                    String fromId = singleFileNodeMap.get(fromNodeHash);
                                    String toId = singleFileNodeMap.get(toNodeHash);
                                    g.addE("cfg").from(__.V(fromId)).to(__.V(toId)).next();
                                    /*g.V().hasLabel("code")
                                            .has("belongFile",singleFileTotal.fileName)
                                            .has("lineno",cfg.fromCodeLabelLine)
                                            .has("json",cfg.fromCodeAST)
                                            .as("fromNode")
                                            .V()
                                            .hasLabel("code")
                                            .has("belongFile",singleFileTotal.fileName)
                                            .has("lineno",cfg.toCodeLabelLine)
                                            .has("json",cfg.toCodeAST)
                                            .addE("cfg")
                                            .from("fromNode")
                                            .next();*/
                                }
                            });
                        }
                        System.out.println("cfgOver");
                    }
                });

                g.tx().commit();

                task3.get();

                subtaskExecutor.shutdown();
                while (!subtaskExecutor.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                    //System.out.println("等待cpg提交");
                }
                subtaskExecutor = Executors.newFixedThreadPool(100);
                ExecutorService finalSubtaskExecutor4 = subtaskExecutor;

                Future<?> task4 = executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (PDG pdg : singleFileTotal.pdgList) {
                            finalSubtaskExecutor4.execute(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(singleFileTotal.fileName + "_dfg: " + pdg.fromCodeLabel);
                                    System.out.println(singleFileTotal.fileName + "_dfg: " + pdg.fromCodeAST);
                                    System.out.println(singleFileTotal.fileName + "_dfg: " + pdg.fromCodeLabelLine);
                                    System.out.println(singleFileTotal.fileName + "_dfg: " + pdg.toCodeLabel);
                                    System.out.println(singleFileTotal.fileName + "_dfg: " + pdg.toCodeAST);
                                    System.out.println(singleFileTotal.fileName + "_dfg: " + pdg.toCodeLabelLine);
                                    String fromNodeHash = pdg.fromCodeAST + pdg.fromCodeLabelLine + singleFileTotal.fileName;
                                    String toNodeHash = pdg.toCodeAST + pdg.toCodeLabelLine + singleFileTotal.fileName;
                                    String fromId = singleFileNodeMap.get(fromNodeHash);
                                    String toId = singleFileNodeMap.get(toNodeHash);
                                    g.addE("dfg").from(__.V(fromId)).to(__.V(toId)).next();
                                    /*g.V().hasLabel("code")
                                            .has("belongFile",singleFileTotal.fileName)
                                            .has("lineno",pdg.fromCodeLabelLine)
                                            .has("json",pdg.fromCodeAST)
                                            .as("fromNode")
                                            .V()
                                            .hasLabel("code")
                                            .has("belongFile",singleFileTotal.fileName)
                                            .has("lineno",pdg.toCodeLabelLine)
                                            .has("json",pdg.toCodeAST)
                                            .addE("dfg")
                                            .from("fromNode")
                                            .next();*/
                                }
                            });
                        }
                        System.out.println("pdgOver");
                    }
                });

                g.tx().commit();

                nodeMap.putAll(singleFileNodeMap);
                singleFileNodeMap.clear();
                task4.get();

                subtaskExecutor.shutdown();
                while (!subtaskExecutor.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                    //System.out.println("等待cpg提交");
                }
            }


            Future<?> task5 = executor.submit(new Runnable() {
                @Override
                public void run() {
                    for (CG cg : allFileTotal.cgList) {
                        cgExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("cg: " + cg.fromCodeBelongFile);
                                System.out.println("cg: " + cg.fromCodeAST);
                                System.out.println("cg: " + cg.fromCodeLineNo);
                                System.out.println("cg: " + cg.toCodeBelongFile);
                                System.out.println("cg: " + cg.toCodeAST);
                                String fromNodeHash = cg.fromCodeAST + cg.fromCodeLineNo + cg.fromCodeBelongFile;
                                String toNodeHash = cg.toCodeAST + cg.toCodeBelongFile;
                                String fromId = nodeMap.get(fromNodeHash);
                                String toId = nodeMap.get(toNodeHash);
                                if (fromId != null && toId != null) {
                                    g.addE("cg").from(__.V(fromId)).to(__.V(toId)).next();
                                }
                                //Optional<>
                                /*Optional<Vertex> fromNode = g.V()
                                        .hasLabel("code")
                                        .has("belongFile",cg.fromCodeBelongFile)
                                        .has("json",cg.fromCodeAST)
                                        .has("lineno",cg.fromCodeLineNo)
                                        .tryNext();
                                Optional<Vertex> toNode = g.V()
                                        .hasLabel("code")
                                        .has("belongFile",cg.toCodeBelongFile)
                                        .has("json",cg.toCodeAST)
                                        .tryNext();
                                if (fromNode.isPresent() && toNode.isPresent()) {
                                    g.V(fromNode.get().id())
                                            .as("fromNode")
                                            .V(toNode.get().id())
                                            .addE("cg")
                                            .from("fromNode")
                                            .next();
                                }
                                if (fromNode.isPresent() && toNode.isPresent()) {
                                    boolean edgeExists = g.V(fromNode.get().id()).bothE("cg").where(otherV().is(toNode.get().id())).hasNext();
                                    if (!edgeExists) {
                                        g.V(fromNode.get().id())
                                                .as("fromNode")
                                                .V(toNode.get().id())
                                                .addE("cg")
                                                .from("fromNode")
                                                .next();
                                    }
                                }*/
                            }
                        });
                    }
                    System.out.println("cgOver");
                }
            });

            g.tx().commit();

            task5.get();

            cgExecutor.shutdown();
            while (!cgExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                System.out.println("等待cg提交");
            }
            executor.shutdown();

            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                System.out.println("等待任务提交");
            }
            g.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Store finished");
        long endStoreTime = System.currentTimeMillis();
        System.out.print("存入数据库用时:");
        System.out.println(endStoreTime - startStoreTime);
        System.exit(0);
    }

    public static void store(AllFileTotal allFileTotal) {
        GraphTraversalSource g = GremlinInstance.getInstance().get();

        // clear old graph
        g.V().drop().iterate();

        // add all vertices
        List<VertexInfo> vertices = new ArrayList<>();
        for (SingleFileTotal single : allFileTotal.singleFileTotalList) {
            GraphTraversal<Vertex, Vertex> traversal = g.addV("file").property("name", single.fileName);
            for (Code code : single.codeList) {
                vertices.add(new VertexInfo(code.codeLabel, code.json, getLineNumber(code.lineno), code.belongFile, code.functionDefName));
                if (vertices.size() >= BATCH_SIZE) {
                    vertexExecutor.submit(new VertexDaemon(vertices, g, vertexMap));
                    vertices = new ArrayList<>();
                }
            }
            for (Method method : single.methodList) {
                vertices.add(new VertexInfo(method.methodCode, method.methodAST, getLineNumber(method.methodLineNo), method.belongFile, ""));
                if (vertices.size() >= BATCH_SIZE) {
                    vertexExecutor.submit(new VertexDaemon(vertices, g, vertexMap));
                    vertices = new ArrayList<>();
                }
            }
        }
        if (!vertices.isEmpty()) {
            vertexExecutor.submit(new VertexDaemon(vertices, g, vertexMap));
        }

        // wait for all vertices to be added
        vertexExecutor.shutdown();
        try {
            if (!vertexExecutor.awaitTermination(1, TimeUnit.HOURS)) {
                System.err.println("Timeout while adding vertices");
                System.exit(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Vertices added");

        // add all edges
        List<EdgeInfo> edges = new ArrayList<>();
        for (SingleFileTotal single : allFileTotal.singleFileTotalList) {
            for (CFG cfg : single.cfgList) {
                String fromKey = getKey(single.fileName, getLineNumber(cfg.fromCodeLabelLine));
                String toKey = getKey(single.fileName, getLineNumber(cfg.toCodeLabelLine));
                Long fromId = vertexMap.get(fromKey);
                Long toId = vertexMap.get(toKey);
                if (fromId != null && toId != null) {
                    edges.add(new EdgeInfo("cfg", fromId, toId));
                    if (edges.size() >= BATCH_SIZE) {
                        edgeExecutor.submit(new EdgeDaemon(edges, g, vertexMap));
                        edges = new ArrayList<>();
                    }
                }
            }
            for (PDG pdg : single.pdgList) {
                String fromKey = getKey(single.fileName, getLineNumber(pdg.fromCodeLabelLine));
                String toKey = getKey(single.fileName, getLineNumber(pdg.toCodeLabelLine));
                Long fromId = vertexMap.get(fromKey);
                Long toId = vertexMap.get(toKey);
                if (fromId != null && toId != null) {
                    // The actual direction of the edge is from the toNode to the fromNode
                    edges.add(new EdgeInfo("dfg", toId, fromId));
                    if (edges.size() >= BATCH_SIZE) {
                        edgeExecutor.submit(new EdgeDaemon(edges, g, vertexMap));
                        edges = new ArrayList<>();
                    }
                }
            }
        }

        // TODO: Add CG support

        if (!edges.isEmpty()) {
            edgeExecutor.submit(new EdgeDaemon(edges, g, vertexMap));
        }

        // wait for all edges to be added
        edgeExecutor.shutdown();
        try {
            if (!edgeExecutor.awaitTermination(1, TimeUnit.HOURS)) {
                System.err.println("Timeout while adding edges");
                System.exit(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Edges added");
    }

    private static int getLineNumber(String lineno) {
        int index = lineno.indexOf(".");
        if (index != -1) {
            return Integer.parseInt(lineno.substring(0, index));
        } else {
            return Integer.parseInt(lineno);
        }
    }

    private static String getKey(String file, int lineno) {
        return file + ":" + lineno;
    }

    public static void oldStoreDB_2(AllFileTotal allFileTotal) {
        long startStoreTime = System.currentTimeMillis();
        GraphTraversalSource g = GremlinInstance.getInstance().get();
        g.V().drop().iterate();
        try {
            for (SingleFileTotal singleFileTotal : allFileTotal.singleFileTotalList) {
                for (Code code : singleFileTotal.codeList) {
                    int index = code.lineno.indexOf(".");
                    int lineno;
                    if (index != -1) {
                        lineno = Integer.parseInt(code.lineno.substring(0, index));
                    } else {
                        lineno = Integer.parseInt(code.lineno);
                    }
                    g.addV("code").property("json", code.json).property("lineno", lineno).property("belongFile", code.belongFile).property("functionDefName", code.functionDefName).property("code", code.codeLabel).next();
                    System.out.println(code.codeLabel);
                    System.out.println(code.json);
                    System.out.println(code.lineno);
                }
                System.out.println("CodeOver");
                for (Method method : singleFileTotal.methodList) {
                    System.out.println(method.methodAST);
                    System.out.println(method.methodLineNo);
                    System.out.println(method.belongFile);
                    System.out.println(method.methodCode);
                    g.addV("code").property("json", method.methodAST).property("lineno", Integer.parseInt(method.methodLineNo)).property("belongFile", method.belongFile).property("functionDefName", "").property("code", method.methodCode).next();
                }
                System.out.println("MethodOver");
                for (CFG cfg : singleFileTotal.cfgList) {
                    System.out.println(cfg.fromCodeLabel);
                    System.out.println(cfg.fromCodeAST);
                    System.out.println(cfg.fromCodeLabelLine);
                    System.out.println(cfg.toCodeLabel);
                    System.out.println(cfg.toCodeAST);
                    System.out.println(cfg.toCodeLabelLine);
                    int index = cfg.fromCodeLabelLine.indexOf(".");
                    int lineno;
                    if (index != -1) {
                        lineno = Integer.parseInt(cfg.fromCodeLabelLine.substring(0, index));
                    } else {
                        lineno = Integer.parseInt(cfg.fromCodeLabelLine);
                    }
                    g.V().hasLabel("code").has("lineno", lineno).has("json", cfg.fromCodeAST).has("belongFile", singleFileTotal.fileName).as("fromNode").V().hasLabel("code").has("lineno", cfg.toCodeLabelLine).has("json", cfg.toCodeAST).has("belongFile", singleFileTotal.fileName).addE("cfg").from("fromNode").next();
                }
                System.out.println("cfgOver");
                for (PDG pdg : singleFileTotal.pdgList) {
                    g.V().hasLabel("code").has("lineno", pdg.fromCodeLabelLine).has("json", pdg.fromCodeAST).has("belongFile", singleFileTotal.fileName).as("fromNode").V().hasLabel("code").has("lineno", pdg.toCodeLabelLine).has("json", pdg.toCodeAST).has("belongFile", singleFileTotal.fileName).addE("dfg").from("fromNode").next();
                    System.out.println(pdg.fromCodeLabel);
                    System.out.println(pdg.fromCodeAST);
                    System.out.println(pdg.fromCodeLabelLine);
                    System.out.println(pdg.toCodeLabel);
                    System.out.println(pdg.toCodeAST);
                    System.out.println(pdg.toCodeLabelLine);
                }
            }
            System.out.println("pdgOver");
            for (CG cg : allFileTotal.cgList) {
                System.out.println(cg.fromCodeBelongFile);
                System.out.println(cg.fromCodeAST);
                System.out.println(cg.fromCodeLineNo);
                System.out.println(cg.toCodeBelongFile);
                System.out.println(cg.toCodeAST);
                System.out.println(cg.toCodeLabel + "()");
                Optional<Vertex> fromNode = g.V().hasLabel("code").has("belongFile", cg.fromCodeBelongFile).has("json", cg.fromCodeAST).has("lineno", cg.fromCodeLineNo).tryNext();
                Optional<Vertex> toNode = g.V().hasLabel("code").has("belongFile", cg.toCodeBelongFile).has("json", cg.toCodeAST).tryNext();
                if (fromNode.isPresent() && toNode.isPresent()) {
                    System.out.println(fromNode.get().id());
                    System.out.println(toNode.get().id());
                    g.V(fromNode.get().id()).as("fromNode").V(toNode.get().id()).addE("cg").from("fromNode").next();
                }
                if (fromNode.isPresent() && toNode.isPresent()) {
                    boolean edgeExists = g.V(fromNode.get().id()).bothE("cg").where(otherV().is(toNode.get().id())).hasNext();
                    if (!edgeExists) {
                        g.V(fromNode.get().id()).as("fromNode").V(toNode.get().id()).addE("cg").from("fromNode").next();
                    }
                }
            }
            System.out.println("cgOver");
            g.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endStoreTime = System.currentTimeMillis();
        System.out.println(endStoreTime - startStoreTime);
        System.out.println("Store finished");
        System.exit(0);
    }

    private record VertexInfo(String code, String json, int lineno, String file, String functionDefName) {}

    private record EdgeInfo(String label, Long from, Long to) {}

    private static class VertexDaemon implements Runnable {
        private final List<VertexInfo> vertices;
        private final GraphTraversalSource g;
        private final Map<String, Long> vertexMap;

        public VertexDaemon(List<VertexInfo> vertices, GraphTraversalSource g, Map<String, Long> vertexMap) {
            this.vertices = vertices;
            this.g = g;
            this.vertexMap = vertexMap;
        }

        @Override
        public void run() {
            System.err.printf("Adding %d vertices%n", vertices.size());

            // convert traversal source to traversal
            var it = vertices.iterator();
            var vertex = it.next();
            List<String> keys = new ArrayList<>();
            GraphTraversal<Vertex, Vertex> traversal = g.addV("code").property("json", vertex.json).property("lineno", vertex.lineno).property("file", vertex.file).property("functionDefName", vertex.functionDefName).property("code", vertex.code);
            String key = getKey(vertex.file, vertex.lineno);
            traversal.id().as(key);
            keys.add(key);
            while (it.hasNext()) {
                vertex = it.next();
                key = getKey(vertex.file, vertex.lineno);
                traversal.addV("code").property("json", vertex.json).property("lineno", vertex.lineno).property("file", vertex.file).property("functionDefName", vertex.functionDefName).property("code", vertex.code);
                traversal.id().as(key);
                keys.add(key);
            }
            List<Map<String, Object>> indexes = traversal.select(keys.get(0), keys.get(1), keys.subList(2, keys.size()).toArray(new String[0])).toList();
            for (Map<String, Object> index : indexes) {
                for (var entry : index.entrySet()) {
                    if (entry.getValue() instanceof Long) {
                        vertexMap.put(entry.getKey(), (Long) entry.getValue());
                    }
                }
            }
        }
    }

    private static class EdgeDaemon implements Runnable {
        private final List<EdgeInfo> edges;
        private final GraphTraversalSource g;
        private final Map<String, Long> vertexMap;

        public EdgeDaemon(List<EdgeInfo> edges, GraphTraversalSource g, Map<String, Long> vertexMap) {
            this.edges = edges;
            this.g = g;
            this.vertexMap = vertexMap;
        }

        @Override
        public void run() {
            System.err.printf("Adding %d edges%n", edges.size());

            var it = edges.iterator();
            var edge = it.next();
            var traversal = g.addE(edge.label()).from(__.V(edge.from())).to(__.V(edge.to()));
            while (it.hasNext()) {
                edge = it.next();
                traversal.addE(edge.label()).from(__.V(edge.from())).to(__.V(edge.to()));
            }
            traversal.iterate();
        }
    }
}