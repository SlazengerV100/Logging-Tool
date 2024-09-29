package nz.ac.wgtn.swen301.a3.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStatsXLS {
    private StatsExcelServlet statsExcelServlet;

    @BeforeEach
    public void setUp() {
        statsExcelServlet = new StatsExcelServlet();
        Persistency.reset();
    }

    @Test
    public void testStatsHTMLStatus200_empty() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        statsExcelServlet.doGet(request, response);

        assertEquals(200, response.getStatus());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", response.getContentType());
        assertEquals(Persistency.initialiseTable(), parseExcelTable(new ByteArrayInputStream(response.getContentAsByteArray())));
    }

    @Test
    public void testStatsHTMLStatus200() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        TestGetLogs.createLogs();
        statsExcelServlet.doGet(request, response);

        assertEquals(200, response.getStatus());
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", response.getContentType());
        assertEquals(Persistency.initialiseTable(), parseExcelTable(new ByteArrayInputStream(response.getContentAsByteArray())));
    }

    private Map<String, Map<String, Integer>> parseExcelTable(InputStream excel) throws IOException {
        Map<String, Map<String, Integer>> tableMap = new TreeMap<>();
        Workbook workbook = new XSSFWorkbook(excel);
        Sheet sheet = workbook.getSheet("Table");
        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        assertEquals("logger", headerRow.getCell(0).getStringCellValue());

        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            headers.add(headerRow.getCell(i).getStringCellValue());
        }

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            String logger = row.getCell(0).getStringCellValue();

            Map<String, Integer> levelMap = new HashMap<>();
            for (int j = 1; j < row.getPhysicalNumberOfCells(); j++) {
                String levelName = headers.get(j);
                int count = (int) (row.getCell(j).getNumericCellValue());
                levelMap.put(levelName, count);
            }
            tableMap.put(logger, levelMap);
        }
        return tableMap;
    }
}
