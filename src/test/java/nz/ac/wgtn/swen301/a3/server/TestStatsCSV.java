package nz.ac.wgtn.swen301.a3.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStatsCSV {
    private StatsCSVServlet statsCSVServlet;

    @BeforeEach
    public void setUp() {
        statsCSVServlet = new StatsCSVServlet();
    }

    @Test
    public void testStatsCSVStatus200() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        statsCSVServlet.doGet(request, response);

        assertEquals(200, response.getStatus());
        assertEquals("text/tab-separated-values", response.getContentType());

        String content = response.getContentAsString();
        String[] rows = content.split("\n");

        String[] headerRow = rows[0].split("\t");
        assertEquals("Logger", headerRow[0]);
        assertEquals("ALL", headerRow[1]);
        assertEquals("TRACE", headerRow[2]);
        assertEquals("DEBUG", headerRow[3]);
        assertEquals("INFO", headerRow[4]);
        assertEquals("WARN", headerRow[5]);
        assertEquals("ERROR", headerRow[6]);
        assertEquals("FATAL", headerRow[7]);
        assertEquals("OFF", headerRow[8]);

        String[] logger1Row = rows[1].split("\t");
        assertEquals("logger1", logger1Row[0]);
        assertEquals("0", logger1Row[1]);
        assertEquals("0", logger1Row[2]);
        assertEquals("3", logger1Row[3]);
        assertEquals("6", logger1Row[4]);
        assertEquals("1", logger1Row[5]);
        assertEquals("1", logger1Row[6]);
        assertEquals("0", logger1Row[7]);
        assertEquals("0", logger1Row[8]);

        String[] logger2Row = rows[2].split("\t");
        assertEquals("logger2", logger2Row[0]);
        assertEquals("0", logger2Row[1]);
        assertEquals("12", logger2Row[2]);
        assertEquals("4", logger2Row[3]);
        assertEquals("0", logger2Row[4]);
        assertEquals("0", logger2Row[5]);
        assertEquals("1", logger2Row[6]);
        assertEquals("0", logger2Row[7]);
        assertEquals("0", logger2Row[8]);

        String[] logger3Row = rows[3].split("\t");
        assertEquals("logger3", logger3Row[0]);
        assertEquals("0", logger3Row[1]);
        assertEquals("0", logger3Row[2]);
        assertEquals("0", logger3Row[3]);
        assertEquals("0", logger3Row[4]);
        assertEquals("5", logger3Row[5]);
        assertEquals("1", logger3Row[6]);
        assertEquals("1", logger3Row[7]);
        assertEquals("0", logger3Row[8]);
    }
}
