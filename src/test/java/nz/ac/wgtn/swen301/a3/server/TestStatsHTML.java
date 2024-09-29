package nz.ac.wgtn.swen301.a3.server;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStatsHTML {
    private StatsHTMLServlet statsHTMLServlet;

    @BeforeEach
    public void setUp() {
        statsHTMLServlet = new StatsHTMLServlet();
        Persistency.reset();
    }

    @Test
    public void testStatsHTMLStatus200_empty() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        statsHTMLServlet.doGet(request, response);

        assertEquals(200, response.getStatus());
        assertEquals("text/html", response.getContentType());
        Document htmlDocument = Jsoup.parse(response.getContentAsString());
        assertEquals(1, htmlDocument.getElementsByTag("body").size());
        assertEquals(1, htmlDocument.getElementsByTag("table").size());
        assertEquals(1, htmlDocument.getElementsByTag("tbody").size());
        assertEquals(1, htmlDocument.getElementsByTag("tr").size());
        assertEquals(9, htmlDocument.getElementsByTag("th").size());
        assertEquals(Persistency.initialiseTable(), parseHtmlTable(htmlDocument));
    }

    @Test
    public void testStatsHTMLStatus200() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        TestGetLogs.createLogs();
        statsHTMLServlet.doGet(request, response);

        assertEquals(200, response.getStatus());
        assertEquals("text/html", response.getContentType());
        Document htmlDocument = Jsoup.parse(response.getContentAsString());
        assertEquals(1, htmlDocument.getElementsByTag("body").size());
        assertEquals(1, htmlDocument.getElementsByTag("table").size());
        assertEquals(1, htmlDocument.getElementsByTag("tbody").size());
        assertEquals(13, htmlDocument.getElementsByTag("tr").size());
        assertEquals(9, htmlDocument.getElementsByTag("th").size());
        assertEquals(Persistency.initialiseTable(), parseHtmlTable(htmlDocument));
    }

    private Map<String, Map<String, Integer>> parseHtmlTable(Document htmlDocument) {
        Map<String, Map<String, Integer>> tableMap = new TreeMap<>();

        Elements rows = htmlDocument.getElementsByTag("tr");
        List<String> headers = rows.get(0).getElementsByTag("th").stream()
                .map(Element::text)
                .toList();

        assertEquals("logger", headers.get(0));
        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cells = row.getElementsByTag("td");
            String logger = cells.get(0).text();

            Map<String, Integer> levelMap = new HashMap<>();
            for (int j = 1; j < cells.size(); j++) {
                String levelName = headers.get(j);
                int count = Integer.parseInt(cells.get(j).text());
                levelMap.put(levelName, count);
            }
            tableMap.put(logger, levelMap);
        }
        return tableMap;
    }
}
