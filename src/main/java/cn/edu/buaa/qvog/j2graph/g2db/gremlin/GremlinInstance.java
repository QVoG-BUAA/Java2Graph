package cn.edu.buaa.qvog.j2graph.g2db.gremlin;

import cn.edu.buaa.qvog.j2graph.j2cpg.graphviz.Config;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.logging.Logger;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class GremlinInstance {
    private static GremlinInstance instance;

    private final GremlinConfig config;
    private GraphTraversalSource g = null;
    private DriverRemoteConnection connection = null;

    private GremlinInstance(GremlinConfig config, GraphTraversalSource g, DriverRemoteConnection connection) {
        this.config = config;
        this.g = g;
        this.connection = connection;
    }

    public static GremlinInstance getInstance() {
        if (instance == null) {
            try {
                instance = create(GremlinConfig.load(Config.CONFIG_PATH));
            } catch (IllegalValueException e) {
                LogHost.getLogger().severe(e.getMessage());
            }
        }

        return instance;
    }

    private static GremlinInstance create(GremlinConfig config) {
        Logger logger = LogHost.getLogger();
        GraphTraversalSource g;

        logger.info("Establishing connection to " + config.getHost() + ":" + config.getPort() + "...");
        DriverRemoteConnection connection;
        try {
            connection = DriverRemoteConnection.using(
                    config.getHost(),
                    config.getPort(),
                    "g");
            g = traversal().withRemote(connection);
        } catch (Exception e) {
            logger.severe("Failed to establish connection: " + e.getMessage());
            return null;
        }
        logger.info("Connection established.");

        return new GremlinInstance(config, g, connection);
    }

    public GraphTraversalSource get() {
        return g;
    }

    public GraphTraversal<Vertex, Vertex> getTraversal() {
        return g.V();
    }

    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
