package hello.service;

import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;

/**
 * Created by i-feng on 2017/9/4.
 */


@Service
public class StudentExportService {
//    String[] excelHeader = { "Sno", "Name", "Age"};
//    public HSSFWorkbook export(List<Campaign> list) {
//        HSSFWorkbook wb = new HSSFWorkbook();
//        HSSFSheet sheet = wb.createSheet("Campaign");
//        HSSFRow row = sheet.createRow((int) 0);
//        HSSFCellStyle style = wb.createCellStyle();
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        for (int i = 0; i < excelHeader.length; i++) {
//            HSSFCell cell = row.createCell(i);
//            cell.setCellValue(excelHeader[i]);
//            cell.setCellStyle(style);
//            sheet.autoSizeColumn(i);
//        }
//        for (int i = 0; i < list.size(); i++) {
//            row = sheet.createRow(i + 1);
//            Student student = list.get(i);
//            row.createCell(0).setCellValue(student.getSno());
//            row.createCell(1).setCellValue(student.getName());
//            row.createCell(2).setCellValue(student.getAge());
//        }
//        return wb;
//    }
}
