
package hello.controller;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import hello.service.CustomExcel;
import hello.model.Student;
import hello.model.Card;
import hello.storage.StorageProperties;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController
@EnableConfigurationProperties(StorageProperties.class)
public class GreetingController {


    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @RequestMapping("/download")
    public String download(@RequestParam(value="fileName", defaultValue = "打卡") String name, @RequestParam(value = "fileName1", defaultValue = "外勤") String name1, @RequestParam(value = "fileName2", defaultValue = "请假") String name2, @RequestParam(value = "fileName3", defaultValue = "签卡")String name3) {

//        ?fileName=打卡&fileName1=外勤&fileName2=请假&fileName3=签卡

        List<Card> list = outPutCard("upload-dir/" + name + ".xls");
//        List<Card> outlist = outPutCardoutworker("upload-dir/" + name1 + ".xls", list);
        List<List<Card>> list1 = outPutCard1("upload-dir/" + name2 + ".xls", outPutCard2(list));
        List<Card> list2 = outPutCard3("upload-dir/" + name3 + ".xls", list1);

        logger.info(list2.size() + " ________________");

        createExcel(list2);

        logger.info(list1.size() + " ________________");

        return "下载成功";
//        return new Greeting(counter.incrementAndGet(),
//                String.format(template, name));
    }

    @RequestMapping("/clean")
    public String clean(){
        return "清除文件失败";
    }


//    @RequestMapping("/greeting")
//    @Cacheable("cards")
//    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
//        model.addAttribute("name", name);
//
////        bookRepository.getByFileName(filepath, cardList);
//
//        return "greeting.html";
//    }

    // 上传文件列表
    @GetMapping("/greeting")
    public String greeting(Model model) throws IOException {


        return "greeting";
    }


    /**
        * 初始化数据
        *
        * @return 数据
        */

    private static List<Student> getStudent() {
        List<Student> list = new ArrayList<Student>();
        Student student1 = new Student("小明", 8, "二年级");
        Student student2 = new Student("小光", 9, "三年级");
        Student student3 = new Student("小花", 10, "四年级");
        list.add(student1);
        list.add(student2);
        list.add(student3);
        return list;
    }
    /**
    * 创建Excel
    *
    * @param list
    *      数据
    */
    private static void createExcel(List<Card> list) {

// 创建一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
// 创建一个工作表
        HSSFSheet sheet = workbook.createSheet("员工表一");
// 添加表头行
        HSSFRow hssfRow = sheet.createRow(0);
// 设置单元格格式居中
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

// 添加表头内容
        HSSFCell headCell = hssfRow.createCell(0);
        headCell.setCellValue("部门名称");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(1);
        headCell.setCellValue("员工编号");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(2);
        headCell.setCellValue("姓名");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(3);
        headCell.setCellValue("开始日期");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(4);
        headCell.setCellValue("结束日期");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(5);
        headCell.setCellValue("打卡次数");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(6);
        headCell.setCellValue("请假开始时间");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(7);
        headCell.setCellValue("请假时长");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(8);
        headCell.setCellValue("请假类别");
        headCell.setCellStyle(cellStyle);
        headCell = hssfRow.createCell(9);
        headCell.setCellValue("描述");
        headCell.setCellStyle(cellStyle);

        logger.info("create excel listSize = " + list.size());

// 添加数据内容
        for (int i = 0; i < list.size(); i++) {
            hssfRow = sheet.createRow((int) i + 1);
            Card card = list.get(i);

            logger.info(card.toString());

// / 创建单元格，并设置值
            HSSFCell cell = hssfRow.createCell(0);
            String department = card.getDepartment();
            cell.setCellValue(department);
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(1);
            String Id = card.getId().toString();
            cell.setCellValue(Id);
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(2);
            String name = card.getName();
            cell.setCellValue(name);
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(3);
            String startTime = card.getStartTime();
//            logger.info(startTime);
            cell.setCellValue(startTime);
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(4);
            String endTime = card.getEndTime();
//            logger.info(endTime);
            cell.setCellValue(endTime);
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(5);
            String count = card.getCount();
            if (count == null){
                count = " ";
            }
            cell.setCellValue(count);
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(6);
            String timeOutStart = card.getTimeOutStart();
            if (timeOutStart == null){
                timeOutStart = " ";
            }
            cell.setCellValue(timeOutStart);
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(7);
            String length = card.getTimeLength();
            if (length == null){
                length = " ";
            }
            cell.setCellValue(length);
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(8);
            String type = card.getLeaveType();
            if (type == null){
                type = " ";
            }
            cell.setCellValue(type);
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(9);
            String description = card.getDescription();
            if (description == null){
                description = " ";
            }
            cell.setCellValue(description);
            cell.setCellStyle(cellStyle);


        }

// 保存Excel文件
        try {
            logger.info("/Users/i-feng/Downloads/students.xls  ---》》》 生成中");
            OutputStream outputStream = new FileOutputStream("/Users/i-feng/Downloads/students.xls");
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            logger.info("/Users/i-feng/Downloads/students.xls  ---》》》 生成错误");
            e.printStackTrace();
        }
    }



    /**
     * 读取Excel
     *
     * @return 数据集合
     */
    private static List<Student> readExcel() {
        List<Student> list = new ArrayList<Student>();
        HSSFWorkbook workbook = null;

        try {
            // 读取Excel文件
            InputStream inputStream = new FileInputStream("/Users/i-feng/Downloads/students.xls");
            workbook = new HSSFWorkbook(inputStream);
            inputStream.close();
        } catch (Exception e) {
            System.out.println("读取excel失败");
            e.printStackTrace();
        }

        // 循环工作表
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = workbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }

                // 将单元格中的内容存入集合
                Student student = new Student();

                HSSFCell cell = hssfRow.getCell(0);
                if (cell == null) {
                    continue;
                }
                student.setName(cell.getStringCellValue());

                cell = hssfRow.getCell(1);
                if (cell == null) {
                    continue;
                }
                student.setAge((int) cell.getNumericCellValue());

                cell = hssfRow.getCell(2);
                if (cell == null) {
                    continue;
                }
                student.setGrade(cell.getStringCellValue());

                list.add(student);

                System.out.println("studen" + list);
            }
        }
        return list;
    }


    public List<Card> outPutCard(String filepath) {
        try {
            logger.info(filepath);
            CustomExcel excelReader = new CustomExcel(filepath);
            logger.info("");
            // 对读取Excel表格内容测试
            List<Card> cardList = new ArrayList<Card>();
            cardList = excelReader.readExcelContentForCard();
            logger.info("cardlist_count" + cardList.size());
            return cardList;
        } catch (FileNotFoundException e) {
            logger.info("未找到指定路径的文件!");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            logger.info("未找到指定路径的文件!");
            e.printStackTrace();
            return null;
        }
    }

    public List<Card> outPutCardoutworker(String filepath, List<Card> list) {
        try {
            logger.info(filepath);
            CustomExcel excelReader = new CustomExcel(filepath);
            List<Card> cardList = new ArrayList<>();

            logger.info("cardlist == nil???");

            cardList = excelReader.readExcelContentForoutworker(list);

            logger.info(cardList.size() + "cardlist");

            return cardList;
        }  catch (Exception e) {
            logger.info("未找到指定路径的文件!  外勤");
            e.printStackTrace();
            return null;
        }
    }

    public List<List<Card>> outPutCard1(String filepath, List<List<Card>>cardList) {
        try {
            logger.info(filepath);

            CustomExcel excelReader = new CustomExcel(filepath);
            // 对读取Excel表格内容测试

            List<List<Card>> cardList1 = new ArrayList<>();

            cardList1 = excelReader.readExcelContentForAddCard(cardList);
            logger.info("cardlist_count   " + cardList1.size());

            return cardList1;

        } catch (FileNotFoundException e) {
            System.out.println("未找到指定路径的文件!");

            logger.info("未找到指定路径的文件!");

            e.printStackTrace();

            return null;

        } catch (Exception e) {

            System.out.println("未找到指定路径的文件!");
            logger.info("未找到指定路径的文件!");

            e.printStackTrace();

            return null;
        }

    }

    public List<List<Card>> outPutCard2(List<Card> list) {
        List<List<Card>> lists = new ArrayList<>();

        String name = "";
        int i = 0;
        int j = 0;
        for (Card card : list) {

            if (!card.getName().equals(name)){
                name = card.getName();
                List<Card> namelist = new ArrayList<Card>();
                lists.add(namelist);
                i++;
            }

            if (card.getName().equals(name)) {
                List<Card> namelist = lists.get(i - 1);
                namelist.add(card);
                j++;
            }
        }

        logger.info("outPutCard2       " + lists.size());
        logger.info("listSize = " + list.size() + ".  j  =   " + j);
        return lists;
    }

    public List<Card> outPutCard3(String filepath, List<List<Card>> cardList) {
        try {
            logger.info(filepath);

            CustomExcel excelReader = new CustomExcel(filepath);
            // 对读取Excel表格内容测试

            List<Card> cardList1 = new ArrayList<>();

            cardList1 = excelReader.readExcelContentForRegistrationCard(cardList);
            logger.info("cardlist_count   " + cardList1.size());

            return cardList1;

        } catch (FileNotFoundException e) {
            System.out.println("未找到指定路径的文件! ___");
            logger.info("未找到指定路径的文件!");
            e.printStackTrace();

            return null;

        } catch (Exception e) {

            System.out.println("未找到指定路径的文件!");

            logger.info("未找到指定路径的文件!");
            e.printStackTrace();

            return null;
        }

    }


    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    public boolean isOneDay(String dateStr, String dateStr1) {

        if (dateStr.substring(0, 10).equals(dateStr1.substring(0, 10))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public long IntegerToDate(String str, SimpleDateFormat format) {
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.debug(str);
        }
        return date.getTime();
    }

}

