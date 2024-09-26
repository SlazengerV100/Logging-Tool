package nz.ac.wgtn.swen301.a3.server;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

public class TestPostLogs {
    private LogsServlet logsServlet;

    @BeforeEach
    public void setUp() {
        logsServlet = new LogsServlet();
        Persistency.reset();
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

    private void doPost(JsonObject jsonBody, int expectedStatus) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setMethod("POST");
        request.setContentType("application/json");
        request.setContent(jsonBody.toString().getBytes());

        logsServlet.doPost(request, response);
        assertEquals(expectedStatus, response.getStatus());
        assertEquals("application/json", response.getContentType());
    }

    @Test
    public void testPostLogsStatus201() throws Exception {
        doPost(createJsonObject("d290f1ee-6c54-4b01-90e6-d701748f0851","application started", "04-05-2021 13:30:45", "main", "com.example.Foo", "debug", "string"), 201);
        Log log = Persistency.DB.get(0);
        assertEquals("d290f1ee-6c54-4b01-90e6-d701748f0851", log.getId());
        assertEquals("application started", log.getMessage());
        assertEquals("04-05-2021 13:30:45", log.getTimestamp());
        assertEquals("main", log.getThread());
        assertEquals("com.example.Foo", log.getLogger());
        assertEquals("debug", log.getLevel());
        assertEquals("string", log.getErrorDetails());
    }

    @Test
    public void testPostLogsStatus400_invalidId() throws Exception {
        doPost(createJsonObject("d290f1ee-6c544b01-90e6-d701748f0851","application started", "04-05-2021 13:30:45", "main", "com.example.Foo", "debug", "string"), 400);
    }

    @Test
    public void testPostLogsStatus400_nullId() throws Exception {
        doPost(createJsonObject(null,"application started", "04-05-2021 13:30:45", "main", "com.example.Foo", "debug", "string"), 400);
    }

    @Test
    public void testPostLogsStatus400_nullMessage() throws Exception {
        doPost(createJsonObject("d290f1ee-6c54-4b01-90e6-d701748f0851",null, "04-05-2021 13:30:45", "main", "com.example.Foo", "debug", "string"), 400);
    }

    @Test
    public void testPostLogsStatus400_invalidTimestamp() throws Exception {
        doPost(createJsonObject("d290f1ee-6c54-4b01-90e6-d701748f0851","application started", "2021-04-05 13:30:45", "main", "com.example.Foo", "debug", "string"), 400);
    }

    @Test
    public void testPostLogsStatus400_nullTimestamp() throws Exception {
        doPost(createJsonObject("d290f1ee-6c54-4b01-90e6-d701748f0851","application started", null, "main", "com.example.Foo", "debug", "string"), 400);
    }

    @Test
    public void testPostLogsStatus400_nullThread() throws Exception {
        doPost(createJsonObject("d290f1ee-6c54-4b01-90e6-d701748f0851","application started", "04-05-2021 13:30:45", null, "com.example.Foo", "debug", "string"), 400);
    }

    @Test
    public void testPostLogsStatus400_nullLogger() throws Exception {
        doPost(createJsonObject("d290f1ee-6c54-4b01-90e6-d701748f0851","application started", "04-05-2021 13:30:45", "main", null, "debug", "string"), 400);
    }

    @Test
    public void testPostLogsStatus400_invalidLevel() throws Exception {
        doPost(createJsonObject("d290f1ee-6c54-4b01-90e6-d701748f0851","application started", "04-05-2021 13:30:45", "main", "com.example.Foo", "some", "string"), 400);
    }

    @Test
    public void testPostLogsStatus400_nullLevel() throws Exception {
        doPost(createJsonObject("d290f1ee-6c54-4b01-90e6-d701748f0851","application started", "04-05-2021 13:30:45", "main", "com.example.Foo", null, "string"), 400);
    }

    @Test
    public void testPostLogsStatus400_nullErrorDetails() throws Exception {
        doPost(createJsonObject("d290f1ee-6c54-4b01-90e6-d701748f0851","application started", "04-05-2021 13:30:45", "main", "com.example.Foo", "debug", null), 400);
    }

    @Test
    public void testPostLogsStatus409_duplicateId() throws Exception {
        testPostLogsStatus201();
        doPost(createJsonObject("d290f1ee-6c54-4b01-90e6-d701748f0851","application started", "04-05-2021 13:30:45", "main", "com.example.Foo", "debug", "string"), 409);
        assertEquals(1, Persistency.DB.size());
    }
}
