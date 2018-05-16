package hello.service;

import hello.controller.GreetingController;
import hello.model.Card;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by i-feng on 2018/5/16.
 */
public class ExcelService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);


    public List<Card> outPutCard(String file1, String file2, String file3) {
        return null;
    }

//    public List<Card> selectPunchCard(String filepath) {
//        try {
//            logger.info(filepath);
//            CustomExcel excelReader = new CustomExcel(filepath);
//            // 对读取Excel表格内容测试
//            List<Card> cardList = new ArrayList<Card>();
//            cardList = readExcelContentForCard();
//            logger.info("cardlist_count" + cardList.size());
//            return cardList;
//        } catch (FileNotFoundException e) {
//            logger.info("未找到指定路径的文件!");
//            e.printStackTrace();
//            return null;
//        } catch (Exception e) {
//            logger.info("未找到指定路径的文件!");
//            e.printStackTrace();
//            return null;
//        }
//    }


}
