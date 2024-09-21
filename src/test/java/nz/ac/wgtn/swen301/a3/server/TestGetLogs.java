package nz.ac.wgtn.swen301.a3.server;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

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
    public void testGetLogsStatus200_limit() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("level", "all");
        request.addParameter("limit", "10");
        for (int i = 0; i < 20; i++) {
            logger.warn("Test Message " + i);
        }
        logsServlet.doGet(request, response);
        assertEquals(200, response.getStatus());
        JsonArray jsonArray = gson.fromJson(response.getContentAsString(), JsonArray.class);
        assertEquals(10, jsonArray.size());
    }

    @Test
    public void testGetLogsStatus200_level() {

    }
}
