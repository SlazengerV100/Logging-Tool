package nz.ac.wgtn.swen301.a3.server;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestGetLogs {
    private LogsServlet logsServlet;
    private Logger logger;
    private Gson gson;

    @BeforeEach
    public void setUp() {
        logsServlet = new LogsServlet();
        logger = Logger.getLogger("TestGetLogs");
        logger.setLevel(Level.ALL);
        Persistency.reset();
        Persistency persistency = new Persistency();
        logger.removeAllAppenders();
        logger.addAppender(persistency);
        gson = new Gson();
    }

    private void validateLatestLog(JsonArray jsonArray, int expectedSize, String expectedMessage, String expectedLevel) {
        assertEquals(expectedSize, jsonArray.size());
        JsonElement latestLogJson = jsonArray.get(0);
        Map latestLog = gson.fromJson(latestLogJson, Map.class);

        assertDoesNotThrow(() -> UUID.fromString(latestLog.get("id").toString()));
        assertEquals(expectedMessage, latestLog.get("message"));
        assertDoesNotThrow(() -> new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(latestLog.get("timestamp").toString()));
        assertEquals(Thread.currentThread().getName(), latestLog.get("thread"));
        assertEquals("TestGetLogs", latestLog.get("logger"));
        assertEquals(expectedLevel, latestLog.get("level"));
        assertEquals("", latestLog.get("errorDetails"));
    }

    private JsonArray createLogs(String limit, String level) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("limit", limit);
        request.addParameter("level", level);

        logger.warn("Warning 1");
        logger.error("Error 1");
        logger.fatal("Fatal 1");
        logger.fatal("Fatal 2");
        logger.error("Error 2");
        logger.info("Info 1");
        logger.debug("Debug 1");
        logger.warn("Warning 2");
        logger.info("Info 2");
        logger.debug("Debug 2");
        logger.trace("Trace 1");
        logger.trace("Trace 2");

        logsServlet.doGet(request, response);

        assertEquals(200, response.getStatus());
        return gson.fromJson(response.getContentAsString(), JsonArray.class);
    }

    @Test
    public void testGetLogsStatus200_limit() throws Exception {
        JsonArray jsonArray = createLogs("10", "all");
        validateLatestLog(jsonArray, 10, "Trace 2", "trace");
    }

    @Test
    public void testGetLogsStatus200_levelOff() throws Exception {
        JsonArray jsonArray = createLogs("100", "off");
        assertTrue(jsonArray.isEmpty());
    }

    @Test
    public void testGetLogsStatus200_levelFatal() throws Exception {
        JsonArray jsonArray = createLogs("100", "fatal");
        validateLatestLog(jsonArray, 2, "Fatal 2", "fatal");
    }

    @Test
    public void testGetLogsStatus200_levelError() throws Exception {
        JsonArray jsonArray = createLogs("100", "error");
        validateLatestLog(jsonArray, 4, "Error 2", "error");
    }

    @Test
    public void testGetLogsStatus200_levelWarn() throws Exception {
        JsonArray jsonArray = createLogs("100", "warn");
        validateLatestLog(jsonArray, 6, "Warning 2", "warn");
    }

    @Test
    public void testGetLogsStatus200_levelInfo() throws Exception {
        JsonArray jsonArray = createLogs("100", "info");
        validateLatestLog(jsonArray, 8, "Info 2", "info");
    }

    @Test
    public void testGetLogsStatus200_levelDebug() throws Exception {
        JsonArray jsonArray = createLogs("100", "debug");
        validateLatestLog(jsonArray, 10, "Debug 2", "debug");
    }

    @Test
    public void testGetLogsStatus200_levelTrace() throws Exception {
        JsonArray jsonArray = createLogs("100", "trace");
        validateLatestLog(jsonArray, 12, "Trace 2", "trace");
    }

    @Test
    public void testGetLogsStatus400_limit1() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("limit", "0");
        request.addParameter("level", "all");

        logsServlet.doGet(request, response);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testGetLogsStatus400_limit2() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("limit", "2147483648");
        request.addParameter("level", "all");

        logsServlet.doGet(request, response);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testGetLogsStatus400_level() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("limit", "10");
        request.addParameter("level", "invalid");

        logsServlet.doGet(request, response);

        assertEquals(400, response.getStatus());
    }
}
