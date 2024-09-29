package nz.ac.wgtn.swen301.a3.server;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class StatsExcelServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Table");
        Row headerRow = sheet.createRow(0);
        createHeaderRow(headerRow);

        int rowNum = 1;
        for (Map.Entry<String, Map<String, Integer>> entry : Persistency.initialiseTable().entrySet()) {
            createDataRow(sheet.createRow(rowNum++), entry.getKey(), entry.getValue());
        }

        try (OutputStream out = response.getOutputStream()) {
            workbook.write(out);
        }
        workbook.close();
    }

    private void createHeaderRow(Row headerRow) {
        int colNum = 0;
        headerRow.createCell(0).setCellValue("Logger");
        for (Log.Level level : Log.Level.values()) {
            headerRow.createCell(colNum++).setCellValue(level.toString());
        }
    }

    private void createDataRow(Row row, String logger, Map<String, Integer> levels) {
        row.createCell(0).setCellValue(logger);
        int cellIndex = 1;
        for (Log.Level level : Log.Level.values()) {
            row.createCell(cellIndex++).setCellValue(levels.get(level.toString()));
        }
    }
}
