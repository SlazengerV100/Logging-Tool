package nz.ac.wgtn.swen301.a3.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStatsCSV {
    private StatsCSVServlet statsCSVServlet;

    @BeforeEach
    public void setUp() {
        statsCSVServlet = new StatsCSVServlet();
        Persistency.reset();
    }

    @Test
    public void testStatsCSVStatus200_emptyLogs() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        statsCSVServlet.doGet(request, response);

        assertEquals(200, response.getStatus());
        assertEquals("text/tab-separated-values", response.getContentType());

        assertEquals(parseCsv(response.getContentAsString()), Persistency.initialiseTable());
    }

    @Test
    public void testStatsCSVStatus200() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        TestGetLogs.createLogs();
        statsCSVServlet.doGet(request, response);

        assertEquals(200, response.getStatus());
        assertEquals("text/tab-separated-values", response.getContentType());

        assertEquals(parseCsv(response.getContentAsString()), Persistency.initialiseTable());
    }

    private Map<String, Map<String, Integer>> parseCsv(String csv) {
        Map<String, Map<String, Integer>> resultMap = new HashMap<>();

        String[] rows = csv.split("\n");
        String[] headers = rows[0].split("\t");
        assertEquals("logger", headers[0]);
        List<String> logLevels = List.of(headers);

        for (int i = 1; i < rows.length; i++) {
            String[] values = rows[i].split("\t");
            String logger = values[0];
            Map<String, Integer> levelsMap = new HashMap<>();

            for (int j = 1; j < values.length; j++) {
                int count = Integer.parseInt(values[j]);
                levelsMap.put(logLevels.get(j), count);
            }
            resultMap.put(logger, levelsMap);
        }
        return resultMap;
    }
}
