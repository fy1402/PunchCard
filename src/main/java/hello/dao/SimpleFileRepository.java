package hello.dao;

import hello.model.Card;
import hello.service.CreateExcel;
import hello.service.CustomExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by i-feng on 2017/9/6.
 */
@Component
public class SimpleFileRepository implements CreateExcel.FileRepository {

    //    @Override
//    public Student getByIsbn(String isbn) {
//        simulateSlowService();
//        return new Student(isbn, "Some book");
//    }
//    @Override
//    @Cacheable("books")
//    public List<Student> getByFileName(String fileName, List<Card> list); {
////        simulateSlowService();
//        return list;
//    }

    private static final Logger logger = LoggerFactory.getLogger(SimpleFileRepository.class);


    @Override
    @Cacheable("cards")
    public List<Card> getByFileName(String type) {

//        upload-dir/7月份指纹打卡明细.xls
        logger.info("getByFileName  " + type);
//        simulateSlowService();


        logger.info("缓存后。。。。。。。。。。。。");

        return outPutCard(type);
    }

    public List<Card> outPutCard(String filepath) {
        try {

            logger.info(filepath);

            CustomExcel excelReader = new CustomExcel(filepath);
            // 对读取Excel表格内容测试

            List<Card> cardList = new ArrayList<Card>();

            cardList = excelReader.readExcelContentForCard();
            logger.info("cardlist_count" + cardList.size());

//            int i = 0;
//            for (Card card : cardList) {
//                logger.info(i++ + card.toString());
//            }

            return cardList;

        } catch (FileNotFoundException e) {
            System.out.println("未找到指定路径的文件!");
            e.printStackTrace();

            return null;

        } catch (Exception e) {

            System.out.println("未找到指定路径的文件!");

            e.printStackTrace();

            return null;
        }

    }



//     Don't do this at home
    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
