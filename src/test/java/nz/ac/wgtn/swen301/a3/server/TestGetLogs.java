package nz.ac.wgtn.swen301.a3.server;


import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestGetLogs {
    @BeforeEach
    public void setUp() {
        Persistency persistency = new Persistency();
        Logger logger = Logger.getLogger("TestGetLogs");
        logger.addAppender(persistency);
        for (int i = 0; i < 20; i++) {
            logger.warn("Test Message " + i);
        }
    }

    @Test
    public void testGetLogs_StatusOK() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("level", "all");
        request.addParameter("limit", "10");
        LogsServlet logsServlet = new LogsServlet();
        logsServlet.doGet(request, response);
        assertEquals(200, response.getStatus());
    }
}
