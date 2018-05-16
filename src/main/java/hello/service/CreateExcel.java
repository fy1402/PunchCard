package hello.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import hello.model.Card;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by i-feng on 2017/9/1.
 */
public class CreateExcel {

    public void createExcel(OutputStream os, List<Card> list) throws WriteException,IOException {
        //创建工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(os);
        //创建新的一页
        WritableSheet sheet = workbook.createSheet("First Sheet", 0);
        //创建要显示的具体内容
        Label formate = new Label(0,0,"部门名称");
        sheet.addCell(formate);
        Label formate1 = new Label(1,0,"员工编号");
        sheet.addCell(formate1);
        Label formate2 = new Label(2,0,"姓名");
        sheet.addCell(formate2);
        Label formate3 = new Label(3,0,"开始日期");
        sheet.addCell(formate3);
        Label formate4 = new Label(4,0,"结束日期");
        sheet.addCell(formate4);
        Label formate5 = new Label(5,0,"打卡次数");
        sheet.addCell(formate5);
        Label formate6 = new Label(6,0,"请假时长");
        sheet.addCell(formate6);
        Label formate7 = new Label(7,0,"请假类别");
        sheet.addCell(formate7);

        int row = 1;
        for (Card card : list) {
            Label label = new Label(0, row, card.getDepartment());
            sheet.addCell(label);
            Label label1 = new Label(1, row, card.getId().toString());
            sheet.addCell(label1);
            Label label2 = new Label(2, row, card.getName());
            sheet.addCell(label2);
            Label label3 = new Label(3, row, card.getStartTime().toString());
            sheet.addCell(label3);
            Label label4 = new Label(4, row, card.getEndTime().toString());
            sheet.addCell(label4);
            Label label5 = new Label(5, row, card.getCount());
            sheet.addCell(label5);
            Label label6 = new Label(6, row, card.getTimeLength());
            sheet.addCell(label6);
            Label label7 = new Label(7, row, card.getLeaveType());
            sheet.addCell(label7);

        }

//        Label example = new Label(0,1,"数据示例");
//        sheet.addCell(example);
//        //浮点数据
//        Number number = new Number(1,1,3.1415926535);
//        sheet.addCell(number);
//        //整形数据
//        Number ints = new Number(2,1,15042699);
//        sheet.addCell(ints);
//        Boolean bools = new Boolean(3,1,true);
//        sheet.addCell(bools);
//        //日期型数据
//        Calendar c = Calendar.getInstance();
//        Date date = c.getTime();
//        WritableCellFormat cf1 = new WritableCellFormat(DateFormats.FORMAT1);
//        DateTime dt = new DateTime(4,1,date,cf1);
//        sheet.addCell(dt);
//        //把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        os.close();

    }

    /**
     * Created by i-feng on 2017/9/6.
     */


    public static interface FileRepository {
        List<Card> getByFileName(String type);


    }
}
