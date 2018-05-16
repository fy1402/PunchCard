package hello.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by i-feng on 2017/9/6.
 */
@Component
public class AppRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final CreateExcel.FileRepository bookRepository;

    public AppRunner(CreateExcel.FileRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info(".... Fetching books");
//        logger.info("isbn-1234 -->" + bookRepository.getByFileName("isbn-1234"));
//        logger.info("isbn-4567 -->" + bookRepository.getByFileName("isbn-4567"));
//        logger.info("isbn-1234 -->" + bookRepository.getByFileName("isbn-1234"));
//        logger.info("isbn-4567 -->" + bookRepository.getByFileName("isbn-4567"));
//        logger.info("isbn-1234 -->" + bookRepository.getByFileName("isbn-1234"));
//        logger.info("isbn-1234 -->" + bookRepository.getByFileName("isbn-1234"));
    }
}
