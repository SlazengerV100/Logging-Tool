package nz.ac.wgtn.swen301.a3.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestGetLogs {
    private LogsServlet logsServlet;
    private Gson gson;

    @BeforeEach
    public void setUp() {
        logsServlet = new LogsServlet();
        Persistency.reset();
        gson = new Gson();
        createLogs();
    }

    private void validateLatestLog(JsonArray jsonArray, int expectedSize, String expectedMessage, String expectedDate, String expectedLogger, String expectedThread, String expectedLevel, String expectedErrorDetails) {
        assertEquals(expectedSize, jsonArray.size());
        JsonElement latestLogJson = jsonArray.get(0);
        Map latestLog = gson.fromJson(latestLogJson, Map.class);

        assertDoesNotThrow(() -> UUID.fromString(latestLog.get("id").toString()));
        assertEquals(expectedMessage, latestLog.get("message"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        assertEquals(expectedDate, formatter.format(formatter.parse(latestLog.get("timestamp").toString())));
        assertEquals(expectedThread, latestLog.get("thread"));
        assertEquals(expectedLogger, latestLog.get("logger"));
        assertEquals(expectedLevel, latestLog.get("level"));
        assertEquals(expectedErrorDetails, latestLog.get("errorDetails"));
    }

    private void createLogs() {
        Persistency.DB.addAll(Arrays.asList(
                new Log("Warning 1", LocalDateTime.of(2024, 9, 16, 0, 0, 0),
                        "Thread-1", "nz.ac.wgtn.swen301.a3.server.Logger-001",
                        Log.Level.WARN, "Error details: A001"),
                new Log("Error 1", LocalDateTime.of(2024, 9, 17, 0, 0, 0),
                        "Thread-2", "nz.ac.wgtn.swen301.a3.server.Logger-002",
                        Log.Level.ERROR, "Error details: A002"),
                new Log("Fatal 1", LocalDateTime.of(2024, 9, 18, 0, 0, 0),
                        "Thread-3", "nz.ac.wgtn.swen301.a3.server.Logger-003",
                        Log.Level.FATAL, "Error details: A003"),
                new Log("Fatal 2", LocalDateTime.of(2024, 9, 19, 0, 0, 0),
                        "Thread-4", "nz.ac.wgtn.swen301.a3.server.Logger-004",
                        Log.Level.FATAL, "Error details: A004"),
                new Log("Error 2", LocalDateTime.of(2024, 9, 20, 0, 0, 0),
                        "Thread-5", "nz.ac.wgtn.swen301.a3.server.Logger-005",
                        Log.Level.ERROR, "Error details: A005"),
                new Log("Info 1", LocalDateTime.of(2024, 9, 21, 0, 0, 0),
                        "Thread-6", "nz.ac.wgtn.swen301.a3.server.Logger-006",
                        Log.Level.INFO, "Error details: A006"),
                new Log("Debug 1", LocalDateTime.of(2024, 9, 22, 0, 0, 0),
                        "Thread-7", "nz.ac.wgtn.swen301.a3.server.Logger-007",
                        Log.Level.DEBUG, "Error details: A007"),
                new Log("Warning 2", LocalDateTime.of(2024, 9, 23, 0, 0, 0),
                        "Thread-8", "nz.ac.wgtn.swen301.a3.server.Logger-008",
                        Log.Level.WARN, "Error details: A008"),
                new Log("Info 2", LocalDateTime.of(2024, 9, 24, 0, 0, 0),
                        "Thread-9", "nz.ac.wgtn.swen301.a3.server.Logger-009",
                        Log.Level.INFO, "Error details: A009"),
                new Log("Debug 2", LocalDateTime.of(2024, 9, 25, 0, 0, 0),
                        "Thread-10", "nz.ac.wgtn.swen301.a3.server.Logger-010",
                        Log.Level.DEBUG, "Error details: A010"),
                new Log("Trace 1", LocalDateTime.of(2024, 9, 26, 0, 0, 0),
                        "Thread-11", "nz.ac.wgtn.swen301.a3.server.Logger-011",
                        Log.Level.TRACE, "Error details: A011"),
                new Log("Trace 2", LocalDateTime.of(2024, 9, 27, 0, 0, 0),
                        "Thread-12", "nz.ac.wgtn.swen301.a3.server.Logger-012",
                        Log.Level.TRACE, "Error details: A012")
        ));
    }

    private JsonArray doGet(String limit, String level, int expectedStatus) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("limit", limit);
        request.addParameter("level", level);

        logsServlet.doGet(request, response);

        assertEquals(expectedStatus, response.getStatus());
        assertEquals("application/json", response.getContentType());
        return gson.fromJson(response.getContentAsString(), JsonArray.class);
    }

    @Test
    public void testGetLogsStatus200_limit() throws Exception {
        JsonArray jsonArray = doGet("10", "all", 200);
        validateLatestLog(jsonArray, 10, "Trace 2", "27-09-2024 00:00:00", "nz.ac.wgtn.swen301.a3.server.Logger-012", "Thread-12", "trace", "Error details: A012");
    }

    @Test
    public void testGetLogsStatus200_levelOff() throws Exception {
        JsonArray jsonArray = doGet("100", "off", 200);
        assertTrue(jsonArray.isEmpty());
    }

    @Test
    public void testGetLogsStatus200_levelFatal() throws Exception {
        JsonArray jsonArray = doGet("100", "fatal", 200);
        validateLatestLog(jsonArray, 2, "Fatal 2", "19-09-2024 00:00:00", "nz.ac.wgtn.swen301.a3.server.Logger-004", "Thread-4", "fatal", "Error details: A004");
    }

    @Test
    public void testGetLogsStatus200_levelError() throws Exception {
        JsonArray jsonArray = doGet("100", "error", 200);
        validateLatestLog(jsonArray, 4, "Error 2", "20-09-2024 00:00:00", "nz.ac.wgtn.swen301.a3.server.Logger-005", "Thread-5", "error", "Error details: A005");
    }

    @Test
    public void testGetLogsStatus200_levelWarn() throws Exception {
        JsonArray jsonArray = doGet("100", "warn", 200);
        validateLatestLog(jsonArray, 6, "Warning 2", "23-09-2024 00:00:00", "nz.ac.wgtn.swen301.a3.server.Logger-008", "Thread-8", "warn", "Error details: A008");
    }

    @Test
    public void testGetLogsStatus200_levelInfo() throws Exception {
        JsonArray jsonArray = doGet("100", "info", 200);
        validateLatestLog(jsonArray, 8, "Info 2", "24-09-2024 00:00:00", "nz.ac.wgtn.swen301.a3.server.Logger-009", "Thread-9", "info", "Error details: A009");
    }

    @Test
    public void testGetLogsStatus200_levelDebug() throws Exception {
        JsonArray jsonArray = doGet("100", "debug", 200);
        validateLatestLog(jsonArray, 10, "Debug 2", "25-09-2024 00:00:00", "nz.ac.wgtn.swen301.a3.server.Logger-010", "Thread-10", "debug", "Error details: A010");
    }

    @Test
    public void testGetLogsStatus200_levelTrace() throws Exception {
        JsonArray jsonArray = doGet("100", "trace", 200);
        validateLatestLog(jsonArray, 12, "Trace 2", "27-09-2024 00:00:00", "nz.ac.wgtn.swen301.a3.server.Logger-012", "Thread-12", "trace", "Error details: A012");
    }

    @Test
    public void testGetLogsStatus400_limit1() throws Exception {
        JsonArray jsonArray = doGet("100", "limit", 400);
    }

    @Test
    public void testGetLogsStatus400_limit2() throws Exception {
        JsonArray jsonArray = doGet("2147483648", "all", 400);
    }

    @Test
    public void testGetLogsStatus400_level() throws Exception {
        JsonArray jsonArray = doGet("10", "invalid", 400);
    }
}
