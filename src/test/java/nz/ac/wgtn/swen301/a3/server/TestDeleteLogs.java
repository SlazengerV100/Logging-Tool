package nz.ac.wgtn.swen301.a3.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeleteLogs {
    private LogsServlet logsServlet;
    @BeforeEach
    public void setUp() {
        logsServlet = new LogsServlet();
        Persistency.reset();
    }

    private void doDelete() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setMethod("DELETE");
        request.setContentType("application/json");

        logsServlet.doDelete(request, response);
        assertEquals(200, response.getStatus());
        assertTrue(Persistency.DB.isEmpty());
    }

    @Test
    public void testDeleteLogsStatus200_logsPresent() {
        TestGetLogs.createLogs();
        doDelete();
    }

    @Test
    public void testDeleteLogsStatus200_logsNotPresent() {
        doDelete();
    }
}
