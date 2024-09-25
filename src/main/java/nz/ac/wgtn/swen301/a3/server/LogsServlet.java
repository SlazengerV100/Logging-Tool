package nz.ac.wgtn.swen301.a3.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Level;

import java.io.IOException;

@WebServlet(name = "LogsServlet", urlPatterns = "/logs")
public class LogsServlet extends HttpServlet {
    private final Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String limitString = request.getParameter("limit");
        String levelString = request.getParameter("level");

        int limit;
        Level level;

        try {
            limit = Integer.parseInt(limitString);
            if (limit < 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (levelString == null || (level = Level.toLevel(levelString.toUpperCase(), null)) == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int i = Persistency.DB.size() - 1;
        int limitCount = 0;
        JsonArray responseBody = new JsonArray();

        while (limitCount < limit && i >= 0) {
            Log log = Persistency.DB.get(i);
            if (log.getLoggingEvent().getLevel().isGreaterOrEqual(level)) {
                responseBody.add(getJsonObject(log));
                limitCount++;
            }
            i--;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(gson.toJson(responseBody));
    }

    private static JsonObject getJsonObject(Log log) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", log.getId());
        jsonObject.addProperty("level", log.getLevel());
        jsonObject.addProperty("timestamp", log.getTimestamp());
        jsonObject.addProperty("thread", log.getThread());
        jsonObject.addProperty("logger", log.getLogger());
        jsonObject.addProperty("message", log.getMessage());
        jsonObject.addProperty("errorDetails", log.getErrorDetails());

        return jsonObject;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) {

    }
}
