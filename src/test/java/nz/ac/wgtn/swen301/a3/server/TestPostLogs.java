package nz.ac.wgtn.swen301.a3.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestPostLogs {
    private LogsServlet logsServlet;
    private Gson gson;

    @BeforeEach
    public void setUp() {
        logsServlet = new LogsServlet();
        Persistency.reset();
        gson = new Gson();
    }

    private JsonObject createJsonObject(String id, String message, String timestamp, String thread, String logger, String level, String errorDetails) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("timestamp", timestamp);
        jsonObject.addProperty("thread", thread);
        jsonObject.addProperty("logger", logger);
        jsonObject.addProperty("level", level);
        jsonObject.addProperty("errorDetails", errorDetails);
        return jsonObject;
    }

    @Test
    public void testPostLogsStatus201() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setMethod("POST");
        request.setContentType("application/json");
        JsonObject jsonBody = createJsonObject("d290f1ee-6c54-4b01-90e6-d701748f0851","application started", "04-05-2021 13:30:45", "main", "com.example.Foo", "debug", "string");
        request.setContent(jsonBody.toString().getBytes());

        logsServlet.doPost(request, response);
        assertEquals(201, response.getStatus());
        Log log = Persistency.DB.get(0);
        assertEquals("d290f1ee-6c54-4b01-90e6-d701748f0851", log.getId());
        assertEquals("application started", log.getMessage());
        assertEquals("04-05-2021 13:30:45", log.getTimestamp());
        assertEquals("main", log.getThread());
        assertEquals("com.example.Foo", log.getLogger());
        assertEquals("debug", log.getLevel());
        assertEquals("string", log.getErrorDetails());
    }
}
