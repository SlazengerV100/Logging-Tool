package nz.ac.wgtn.swen301.a3.server;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Map;

public class StatsCSVServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/tab-separated-values");
        PrintWriter out = response.getWriter();
        out.print("logger");
        for (Log.Level header : Log.Level.values()) {
            out.print("\t" + header.toString());
        }
        out.print("\n");

        for (Map.Entry<String, Map<String, Integer>> entry : Persistency.initialiseTable().entrySet()) {
            out.print(entry.getKey());
            Map<String, Integer> levels = entry.getValue();
            for (Log.Level level : Log.Level.values()) {
                out.print("\t" + levels.get(level.toString()));
            }
            out.print("\n");
        }

        out.flush();
        out.close();
    }
}
