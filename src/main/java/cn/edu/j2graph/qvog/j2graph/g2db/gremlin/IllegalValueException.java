package cn.edu.j2graph.qvog.j2graph.g2db.gremlin;

public class IllegalValueException extends Exception {
    public IllegalValueException(String message) {
        super(message);
    }

    public IllegalValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
