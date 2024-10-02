package cn.edu.j2graph.qvog.j2graph.g2db.gremlin;

import org.apache.tinkerpop.shaded.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GremlinConfig {
    private String host;
    private int port;

    public static GremlinConfig load(String filename) throws IllegalValueException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
            String json = String.join(System.lineSeparator(), lines);
            //String json = Files.readString(Paths.get(filename));
            return objectMapper.readValue(json, GremlinConfig.class);
        } catch (Exception e) {
            throw new IllegalValueException("Failed to load config file: " + filename, e);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
