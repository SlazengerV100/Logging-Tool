package nz.ac.wgtn.swen301.a3.server;

import java.util.*;

public class Persistency {
    public static List<Log> DB = new ArrayList<>();

    public static void reset() {
        DB = new ArrayList<>();
    }

    public static Map<String, Map<String, Integer>> initialiseTable() {
        Map<String, Map<String, Integer>> loggerTable = new TreeMap<>();

        Map<String, Integer> logger1Levels = new HashMap<>();
        logger1Levels.put("ALL", 0);
        logger1Levels.put("TRACE", 0);
        logger1Levels.put("DEBUG", 3);
        logger1Levels.put("INFO", 6);
        logger1Levels.put("WARN", 1);
        logger1Levels.put("ERROR", 1);
        logger1Levels.put("FATAL", 0);
        logger1Levels.put("OFF", 0);

        Map<String, Integer> logger2Levels = new HashMap<>();
        logger2Levels.put("ALL", 0);
        logger2Levels.put("TRACE", 12);
        logger2Levels.put("DEBUG", 4);
        logger2Levels.put("INFO", 0);
        logger2Levels.put("WARN", 0);
        logger2Levels.put("ERROR", 1);
        logger2Levels.put("FATAL", 0);
        logger2Levels.put("OFF", 0);

        Map<String, Integer> logger3Levels = new HashMap<>();
        logger3Levels.put("ALL", 0);
        logger3Levels.put("TRACE", 0);
        logger3Levels.put("DEBUG", 0);
        logger3Levels.put("INFO", 0);
        logger3Levels.put("WARN", 5);
        logger3Levels.put("ERROR", 1);
        logger3Levels.put("FATAL", 1);
        logger3Levels.put("OFF", 0);

        loggerTable.put("logger1", logger1Levels);
        loggerTable.put("logger2", logger2Levels);
        loggerTable.put("logger3", logger3Levels);

        return loggerTable;
    }
}
