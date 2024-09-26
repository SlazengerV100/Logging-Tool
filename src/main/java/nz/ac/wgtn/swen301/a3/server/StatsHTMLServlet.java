package nz.ac.wgtn.swen301.a3.server;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class StatsHTMLServlet extends HttpServlet {
    private final Map<String, Map<String, Integer>> table;

    public StatsHTMLServlet() {
        table = Persistency.initialiseTable();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<body>");

        out.println("<table>");
        out.println("<tbody>");
        out.println("<tr>");
        out.print("<th>Logger</th>");

        for (Log.Level header : Log.Level.values()) {
            out.printf("\n<th>%s</th>", header);
        }
        out.print("\n");
        out.println("</tr>");

        for (Map.Entry<String, Map<String, Integer>> entry : table.entrySet()) {
            out.println("<tr>");
            out.printf("<td>%s</td>", entry.getKey());
            Map<String, Integer> levels = entry.getValue();
            for (Log.Level level : Log.Level.values()) {
                out.printf("\n<td>%d</td>", levels.get(level.toString()));
            }
            out.print("\n");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("</tbody");
        out.println("</body>");
        out.println("</html>");

        out.flush();
        out.close();
    }
}
