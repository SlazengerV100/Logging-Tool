package nz.ac.wgtn.swen301.a3.server;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestGetLogs {
    private LogsServlet logsServlet;
    private Logger logger;
    private Gson gson;
    @BeforeEach
    public void setUp() {
        logsServlet = new LogsServlet();
        logger = Logger.getLogger("TestGetLogs");
        Persistency persistency = new Persistency();
        logger.addAppender(persistency);
        gson = new Gson();
    }

    @Test
    public void testGetLogsStatus200_limit() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("level", "all");
        request.addParameter("limit", "10");
        for (int i = 1; i <= 20; i++) {
            logger.warn("Test Message " + i);
        }
        logsServlet.doGet(request, response);
        assertEquals(200, response.getStatus());
        JsonArray jsonArray = gson.fromJson(response.getContentAsString(), JsonArray.class);
        assertEquals(10, jsonArray.size());
        JsonElement latestLogJson = jsonArray.get(0);
        Map latestLog = gson.fromJson(latestLogJson, Map.class);
        assertEquals("Test Message 20", latestLog.get("message"));
        assertEquals(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Instant.now().toEpochMilli()), latestLog.get("timestamp"));
        assertEquals(Thread.currentThread().getName(), latestLog.get("thread"));
        assertEquals("TestGetLogs", latestLog.get("logger"));
        assertEquals("warn", latestLog.get("level"));
        assertEquals("", latestLog.get("errorDetails"));
    }

    @Test
    public void testGetLogsStatus200_level() {

    }
}
