package cn.edu.j2graph.qvog.j2graph.g2db.gremlin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHost {
    private static final Logger logger = Logger.getAnonymousLogger();

    static {
        logger.setLevel(Level.INFO);
    }

    public static void setLevel(Level level) {
        logger.setLevel(level);
    }


    public static Logger getLogger() {
        return logger;
    }
}
