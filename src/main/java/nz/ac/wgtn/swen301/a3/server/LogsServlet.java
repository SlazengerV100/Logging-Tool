package nz.ac.wgtn.swen301.a3.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class LogsServlet extends HttpServlet {
    private final Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String limitString = request.getParameter("limit");
        String levelString = request.getParameter("level");

        int limit;
        Log.Level level;

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

        if ((level = Log.Level.toLevel(levelString.toUpperCase())) == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int i = Persistency.DB.size() - 1;
        int limitCount = 0;
        JsonArray responseBody = new JsonArray();

        while (limitCount < limit && i >= 0) {
            Log log = Persistency.DB.get(i);
            if (log.getLevelAsObject().isGreaterOrEqual(level)) {
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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

        try {
            UUID id = UUID.fromString(jsonObject.get("id").getAsString());
            String message = jsonObject.get("message").getAsString();
            LocalDateTime timestamp = LocalDateTime.parse(jsonObject.get("timestamp").getAsString(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            String thread = jsonObject.get("thread").getAsString();
            String logger = jsonObject.get("logger").getAsString();
            Log.Level level = Log.Level.valueOf(jsonObject.get("level").getAsString().toUpperCase());
            String errorDetails = jsonObject.get("errorDetails").getAsString();

            for (Log log : Persistency.DB) {
                if (log.getId().equals(id.toString())) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    return;
                }
            }

            Log newLog = new Log(id, message, timestamp, thread, logger, level, errorDetails);
            Persistency.DB.add(newLog);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) {

    }
}
