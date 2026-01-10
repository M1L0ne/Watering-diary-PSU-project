package tsygvintsev.watering_diary.util;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tsygvintsev.watering_diary.entity.WateringRecord;

/**
 * Экспортирует записи полива в Excel (.xlsx).
 */
public class WateringRecordExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<WateringRecord> recordList;

    /**
     * Создаёт экспортёр с заданным списком записей.
     *
     * @param recordList список записей для экспорта
     */
    public WateringRecordExcelExporter(List<WateringRecord> recordList) {
        this.recordList = recordList;
        this.workbook = new XSSFWorkbook();
    }

    /**
     * Создаёт строку заголовка таблицы.
     */
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Записи полива");
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Имя растения", style);
        createCell(row, 1, "Тип растения", style);
        createCell(row, 2, "Дата", style);
        createCell(row, 3, "Время", style);
        createCell(row, 4, "Объём полива (мл)", style);
        createCell(row, 5, "Погрешность (мл)", style);
    }

    /**
     * Создаёт ячейку в строке.
     *
     * @param row строка таблицы
     * @param columnCount номер колонки
     * @param value значение ячейки
     * @param style стиль оформления
     */
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }

    /**
     * Записывает данные всех записей полива.
     */
    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (WateringRecord record : recordList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            String plantName = (record.getUserPlant() != null)
                    ? record.getUserPlant().getName()
                    : "Неизвестно";
            createCell(row, columnCount++, plantName, style);

            String plantType = (record.getUserPlant() != null && record.getUserPlant().getPlantType() != null)
                    ? record.getUserPlant().getPlantType().getName()
                    : "Неизвестно";
            createCell(row, columnCount++, plantType, style);

            createCell(row, columnCount++, record.getDate().toString(), style);
            createCell(row, columnCount++, record.getTime().toString(), style);
            createCell(row, columnCount++, record.getVolumeWatering(), style);
            createCell(row, columnCount++, record.getErrorRateK(), style);
        }
    }

    /**
     * Экспортирует данные в Excel и отправляет в HTTP-ответ.
     *
     * @param response HTTP-ответ
     * @throws IOException при ошибке записи
     */
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
