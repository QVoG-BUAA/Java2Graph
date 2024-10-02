package cn.edu.j2graph.qvog.j2graph.g2db.print;

public class Vertex {
    String label;
    int vertexNo;
    String ASTJson;

    public Vertex(String label, String ASTJson) {
        this.label = label;
        this.ASTJson = ASTJson;
    }
}
