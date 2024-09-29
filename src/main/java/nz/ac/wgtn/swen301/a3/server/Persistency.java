package nz.ac.wgtn.swen301.a3.server;

import java.util.*;

public class Persistency {
    public static List<Log> DB = new ArrayList<>();

    public static void reset() {
        DB = new ArrayList<>();
    }

    public static Map<String, Map<String, Integer>> initialiseTable() {
        Map<String, Map<String, Integer>> loggerTable = new TreeMap<>();

        for (Log log : DB) {
            if (!loggerTable.containsKey(log.getLogger())) {
                Map<String, Integer> loggerCount = new HashMap<>();
                for (Log.Level level : Log.Level.values()) {
                    loggerCount.put(level.toString(), 0);
                }
                loggerTable.put(log.getLogger(), loggerCount);
            }
            int count = loggerTable.get(log.getLogger()).get(log.getLevel()) + 1;
            loggerTable.get(log.getLogger()).put(log.getLevel(), count);
        }
        return loggerTable;
    }
}
