package nz.ac.wgtn.swen301.a3.server;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;
import java.util.Map;

public class StatsCSVServlet extends HttpServlet {
    private final Map<String, Map<String, Integer>> table;

    public StatsCSVServlet() {
        table = initialiseTable();
    }

    private Map<String, Map<String, Integer>> initialiseTable() {
        Map<String, Map<String, Integer>> loggerTable = new HashMap<>();

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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/tab-separated-values");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print("Logger");
        for (String header : table.get("logger1").keySet()) {
            out.print("\t" + header);
        }
        out.println("");

        for (Map.Entry<String, Map<String, Integer>> entry : table.entrySet()) {
            out.print(entry.getKey());
            Map<String, Integer> levels = entry.getValue();
            for (Log.Level level : Log.Level.values()) {
                out.print("\t" + levels.get(level.toString()));
            }
            out.println("");
        }

        out.flush();
        out.close();
    }
}
