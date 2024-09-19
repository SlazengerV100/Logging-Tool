package nz.ac.wgtn.swen301.a3.server;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Level;

import java.io.IOException;

public class LogsServlet {
    LogsServlet() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String limitString = request.getParameter("limit");
        String levelString = request.getParameter("level");
        String errorDetails = "";

        int limit;
        Level level;

        try {
            limit = Integer.parseInt(limitString);
            if (limit < 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                errorDetails = "'limit' must be greater than or equal to 1.";
                return;
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            errorDetails = "'limit' must be a valid integer.";
            return;
        }

        if (levelString == null || (level = Level.toLevel(levelString.toUpperCase(), null)) == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            errorDetails = "'level' must be a valid log level.";
            return;
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) {

    }
}
