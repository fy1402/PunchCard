package hello.service;

/**
 * Created by i-feng on 2017/8/30.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import hello.model.Card;
import hello.model.UserCard;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomExcel {

    private Logger logger = LoggerFactory.getLogger(CustomExcel.class);
    private Workbook wb;
    private Sheet sheet;
    private Row row;

    public CustomExcel(String filepath) {
        if(filepath==null){
            return;
        }
        String ext = filepath.substring(filepath.lastIndexOf("."));
        try {
            InputStream is = new FileInputStream(filepath);

            logger.info("filePath:   " + filepath);

            if(".xls".equals(ext)){
                logger.info(".xls");
                wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(ext)){
                logger.info(".xlsx");
                wb = new XSSFWorkbook(is);
            }else{
                logger.info("nil");
                wb=null;
            }
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
    }

    /**
     * 读取Excel表格表头的内容
     */
    public String[] readExcelTitle() throws Exception{
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            // title[i] = getStringCellValue(row.getCell((short) i));
            System.out.println(row.getCell(0));
            title[i] = row.getCell(i).getCellFormula();
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     */
    public Map<Integer, Map<Integer,Object>> readExcelContent() throws Exception{
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        Map<Integer, Map<Integer,Object>> content = new HashMap<Integer, Map<Integer,Object>>();

        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 2; i <= rowNum; i++) {
            row = sheet.getRow(i);

            if (row.getPhysicalNumberOfCells() >= 7 ) {
                return null;
            }

            Object obj0 = getCellFormatValue(row.getCell(0));
            Object obj1 = getCellFormatValue(row.getCell(1));
            Object obj2 = getCellFormatValue(row.getCell(2));
            Object obj3 = getCellFormatValue(row.getCell(3));
            Object obj4 = getCellFormatValue(row.getCell(4));
            Object obj5 = getCellFormatValue(row.getCell(5));

            String timeStr = (String)obj5;
            String[] times = timeStr.split(",");

            String startTime = "";
            String endTime = "";
            if (times.length > 0) {
                startTime = (String) obj3 + " " + times[0];

               if (times.length >= 2) {
                   String str = times[times.length - 1];
                   if (str != null) {
                       endTime = (String) obj3 + " " + str;
                   }
               }
            }

            Card card = new Card();
            card.setId(new Integer((String)obj1));
            card.setDepartment((String)obj0);
            card.setName((String)obj2);
            card.setCount((String)obj4);
            card.setEndTime(endTime);
            card.setStartTime(startTime);

            int j = 0;
            Map<Integer,Object> cellValue = new HashMap<Integer, Object>();
            while (j < colNum) {
                Object obj = getCellFormatValue(row.getCell(j));

                cellValue.put(j, obj);
                j++;
            }


            content.put(i, cellValue);
        }
        return content;
    }

    // 请假
    public List<List<Card>> readExcelContentForAddCard(List<List<Card>> cards) throws Exception{
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
//        int colNum = row.getPhysicalNumberOfCells();

        logger.info("请假rowNum       " + rowNum);
//        List<Card> list = new ArrayList<>();

        List<List<Card>> list = new ArrayList<>();

        List<Card> list1 = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);

            logger.info(row.toString() + "  i=" + i);

            Object obj0 = getCellFormatValue(row.getCell(0));  // name
            Object obj1 = getCellFormatValue(row.getCell(3));  // type
            Object obj2 = getCellFormatValue(row.getCell(5));  // start
            Object obj3 = getCellFormatValue(row.getCell(6));  // time
            Object obj4 = getCellFormatValue(row.getCell(7));  // end
            Object obj5 = getCellFormatValue(row.getCell(8));  // time
            Object obj6 = getCellFormatValue(row.getCell(9));  //请假时长
            Object obj8 = getCellFormatValue(row.getCell(11));  //  结束 为 正确消息

            String startTime = obj2 + " " + obj3;
            String endTime = obj4 + " " + obj5;
            String name = (String)obj0;
            String timeLenth = (String)obj6;
            String leType = (String)obj1;

            String endTimeOut = (String)obj8;
//            logger.info(leType);
            if (endTimeOut.equals("结束") && !name.equals("申请人") && !name.equals("")){
                Card card1 = new Card(0, name, "ake", startTime + ":00", endTime + ":00", leType, timeLenth, "", startTime + ":00");
                list1.add(card1);
            }
        }

        logger.info("员工请假人数 list1.size() = " + list1.size());

        for (List<Card> cardList : cards) {  // 人-总

            List<Card> tempList = new ArrayList<>();
            for (Card card : cardList) {  // 打卡-天
                tempList.add(card);
            }

            for (Card card1 : list1) {  // 打卡-天
                for (int i = 0; i < cardList.size(); i++) {
                    Card card = cardList.get(i);
                    if (card1.getName().equals(card.getName()) && isOneMonth(card.getStartTime(), card1.getStartTime())) { // 同名同月

                        if (card1.getName().equals(card.getName()) && isOneDay(card1.getStartTime(), card.getStartTime())) {   // 同名同日
                            tempList.remove(card);
                            card.setLeaveType(card.getLeaveType() + ", " + card1.getLeaveType());
                            card.setDescription("半天");
                            card.setTimeLength(card.getTimeLength() + ", " +card1.getTimeLength());
                            card.setTimeOutStart(card1.getTimeOutStart());

                            // 比较（更新）开始时间和结束时间
                            Date d = format.parse(card.getStartTime());
                            Date d1 = format.parse(card1.getStartTime());

                            if (d.getTime() > d1.getTime()) {
                                card.setStartTime(card1.getStartTime());
                            }
                            Date d2 = format.parse(card.getStartTime());
                            if (d2.getTime() < d1.getTime()) {
                                card.setEndTime(card1.getEndTime());
                            }
                            tempList.add(card);
                            break;
                        } else {
                            if (i + 1 == cardList.size()) {
                                card1.setDescription("全天");
                                card1.setDepartment(card.getDepartment());
                                card1.setId(card.getId());
                                tempList.add(card1);
                            }
                        }
                    }
                }
            }
            list.add(tempList);
        }
        return list;
    }


    /*
    读取 打卡 详情
     */
    public List<Card> readExcelContentForCard() throws Exception{
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
//        int colNum = row.getPhysicalNumberOfCells();

        List<Card> cardList = new ArrayList<>();
//        List<UserCard> userCardList = new ArrayList<>();

        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 2; i <= rowNum; i++) {
            row = sheet.getRow(i);

            if (row.getPhysicalNumberOfCells() >= 7) {
                return null;
            }

            Short lastNum = row.getLastCellNum();

            Object obj0 = getCellFormatValue(row.getCell(0));   // 部门名称
            Object obj1 = getCellFormatValue(row.getCell(1));   // 人员编号
            Object obj2 = getCellFormatValue(row.getCell(2));   // 姓名
            Object obj3 = getCellFormatValue(row.getCell(3));   // 日期
            Object obj4 = getCellFormatValue(row.getCell(4));   // 打卡次数
            Object obj5 = getCellFormatValue(row.getCell(5));   // 打卡时间
            String timeStr = (String) obj5;
            String[] times = timeStr.split(",");

            String startTime = "";
            String endTime = "";

            if (times.length > 0) {
                startTime = obj3 + " " + times[0];

                if (times.length >= 2) {
                    String str = times[times.length - 1];
                    if (str != null) {
                        endTime = obj3 + " " + str;
                    }
                }
            }

            Card card = new Card();
            card.setId(new Integer((String) obj1));
            card.setDepartment((String) obj0);
            card.setName((String) obj2);
            card.setCount((String)obj4);
            card.setEndTime(endTime);
            card.setStartTime(startTime);
            card.setDescription("");
            cardList.add(card);


        }
        return cardList;
    }

    /*签卡*/
    public List<Card> readExcelContentForRegistrationCard(List<List<Card>> cards) throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
//        int colNum = row.getPhysicalNumberOfCells();

        logger.info("rowNum       " + rowNum);
        List<Card> list = new ArrayList<>();
        List<Card> list1 = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 2; i <= rowNum; i++) {
            row = sheet.getRow(i);

            logger.info(row.toString() + "        i=" + i);

            Object obj0 = getCellFormatValue(row.getCell(0));  // name
            Object obj1 = getCellFormatValue(row.getCell(1));  // department
            Object obj2 = getCellFormatValue(row.getCell(2));  // day
            Object obj3 = getCellFormatValue(row.getCell(3));  // time
            Object obj4 = getCellFormatValue(row.getCell(4));  // reseon

            String startTime = obj2 + " " + obj3;
            String name = (String) obj0;
            String reseon = (String) obj4;
            String department = (String) obj1;

            if (!name.equals("申请人") || !name.equals("")) {
                Card card1 = new Card(0, name, department, startTime + ":00", "", "", "", reseon, startTime + ":00");
                list1.add(card1);
            }
        }

        for (List<Card> cardList : cards) {  // 人-总

            for (Card card : cardList) {  // 打卡-天

                for (Card card1 : list1) {  // 签卡-天
                    if (card1.getName().equals(card.getName()) && isOneMonth(card.getStartTime(), card1.getStartTime())) { // 同名同月
                        if (card1.getName().equals(card.getName()) && isOneDay(card1.getStartTime(), card.getStartTime())) {   // 同名同日
                            if (card.getEndTime().equals("")) {
                                card.setEndTime(card1.getStartTime());
                            }
                            if (card.getStartTime().equals("")) {
                                card.setStartTime(card.getStartTime());
                            }
                            card.setDescription(card1.getDescription());
                            card.setLeaveType(card.getLeaveType() + "签卡");
                        }
                    }
                }
                list.add(card);
            }
        }

        logger.info("readExcelContentForRegistrationCard");

        return list;
    }

    public List<Card> readExcelContentForoutworker(List<Card> list) throws Exception {
        if(wb==null){
            logger.info("Workbook对象为空 外勤");
            throw new Exception("Workbook对象为空！");
        }
        logger.info("PPPPPPPPPPPP");

        sheet = wb.getSheetAt(1);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();

        logger.info("rowNum" + rowNum);

        row = sheet.getRow(0);
        List<Card> cardList = new ArrayList<>();

        logger.info("——————————————");

        String countend = "";

        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 3; i <= rowNum; i++) {

            logger.info("——————————————");

            row = sheet.getRow(i);

            Object obj0 = getCellFormatValue(row.getCell(1)); // name
            Object obj1 = getCellFormatValue(row.getCell(2)); // id
            Object obj2 = getCellFormatValue(row.getCell(3)); // department
            Object obj3 = getCellFormatValue(row.getCell(6)); // date
            Object obj4 = getCellFormatValue(row.getCell(7)); // time
            Object obj5 = getCellFormatValue(row.getCell(0)); // description 签到地点
            Object obj6 = getCellFormatValue(row.getCell(9)); // 打卡次数
            logger.info(obj5 + "_" + obj0 + "_" + obj1 + "_"  + obj2 + "_"  + obj3 + "_"  + obj4 + "_"  + obj6);

            String dateStr = (String) obj3;
            String timeStr = (String) obj4;
            String countStr = (String)obj6;

            logger.info("countStr" + countStr);

            if (!countStr.equals("")) {
                countend = countStr + "";
                logger.info("_______" + countend);
            } else  {
                countStr = countend;
            }

            float countF = new Float(countStr);
            int count = (Float.valueOf(countF)).intValue();
            String count1 = count + "";
            logger.info(count + "");



            Card card = new Card();
            card.setId(new Integer((String) obj1));
            card.setDepartment((String) obj2);
            card.setName((String) obj0);
            card.setCount(count1);
            card.setStartTime((String) obj3);
            card.setEndTime((String) obj4);
            cardList.add(card);

            logger.info(card.toString());
        }

        List<Card> tempList = new ArrayList<>();
        Card newCard = new Card();
        newCard.setId(0);
        for (Card card : cardList) {

            logger.info(card.toString());

            if (newCard.getId() != 0 && newCard.getStartTime().equals(card.getStartTime())) {
                newCard.setStartTime(newCard.getStartTime() + " " + newCard.getEndTime());
                newCard.setEndTime(card.getStartTime() + " " + card.getEndTime());
                tempList.add(newCard);
            } else if (Integer.valueOf(card.getCount()) <= 1) {
                card.setStartTime(card.getStartTime() + " " + card.getEndTime());
                card.setEndTime("");
                tempList.add(card);
            } else {
                newCard = card;
            }
        }

        list.addAll(tempList);

        return list;
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public Date StrToDate(String str, SimpleDateFormat format) {

        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.debug(str);
        }

        return date;
    }


    public boolean isOneDay(String dateStr, String dateStr1) {
        if (dateStr.substring(0, 10).equals(dateStr1.substring(0, 10))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isOneMonth(String dateStr, String dateStr1) {
        if (dateStr.substring(0, 7).equals(dateStr1.substring(0, 7))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * 根据Cell类型设置数据
     *
     * @param cell
     * @return
     * @author zengwendong
     */
    private Object getCellFormatValue(Cell cell) {

        Object cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式
                        // data格式是带时分秒的：2013-7-10 0:00:00
                        // cellvalue = cell.getDateCellValue().toLocaleString();
                        // data格式是不带带时分秒的：2013-7-10
                        Date date = cell.getDateCellValue();
                        cellvalue = date;
                    } else {// 如果是纯数字

                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:// 如果当前Cell的Type为STRING
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:// 默认的Cell值
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

//    public static void main(String[] args) {
//        try {
//            String filepath = "F:test.xls";
//            CustomExcel excelReader = new CustomExcel(filepath);
//            // 对读取Excel表格标题测试
////          String[] title = excelReader.readExcelTitle();
////          System.out.println("获得Excel表格的标题:");
////          for (String s : title) {
////              System.out.print(s + " ");
////          }
//
//            // 对读取Excel表格内容测试
//            Map<Integer, Map<Integer,Object>> map = excelReader.readExcelContent();
//            System.out.println("获得Excel表格的内容:");
//            for (int i = 1; i <= map.size(); i++) {
//                System.out.println(map.get(i));
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("未找到指定路径的文件!");
//            e.printStackTrace();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
