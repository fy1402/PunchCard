package hello.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hello.service.CreateExcel;
import hello.service.CustomExcel;
import hello.model.Card;
import hello.storage.StorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.storage.StorageFileNotFoundException;
import hello.storage.StorageService;

import javax.servlet.http.HttpServletResponse;

@EnableConfigurationProperties(StorageProperties.class)
@Controller
public class FileUploadController {

    private final StorageService storageService;
//    private final Path rootLocation;
    private List<Card> cardList;

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final CreateExcel.FileRepository bookRepository;


    @Autowired
    public FileUploadController(StorageService storageService, CreateExcel.FileRepository bookRepository) {
        this.bookRepository = bookRepository;
        System.out.println("FileUploadController");
        logger.debug("FileUploadController");
        this.cardList = new ArrayList<Card>();
        this.storageService = storageService;
    }

    // 上传文件列表
    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    // 下载
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws Exception {

        Resource file = storageService.loadAsResource(filename);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

//    public void outPutCard(String filepath) {
//        try {
//
//            logger.info(filepath);
//
//            CustomExcel excelReader = new CustomExcel(filepath);
//            // 对读取Excel表格内容测试
//
//            if (cardList.isEmpty()) {
//                System.out.println("excelReader.readExcelContentForCard()");
//                cardList = excelReader.readExcelContentForCard();
////                int i = 0;
////                for (Card card: cardList) {
////                    logger.info( i++ + card.toString());
////                }
//            } else {
//                System.out.println("excelReader.readExcelContentForAddCard(cardList);");
//                cardList = excelReader.readExcelContentForAddCard(cardList);
//                int i = 0;
//                for (Card card: cardList) {
//                    logger.info( i++ + card.toString());
//                }
//            }
//
//            System.out.println("____________" + cardList.size());
//
//            logger.info("readExcelContentForCard________________");
//
////            for (Card card: cardList) {
////                logger.info(card.toString());
////                System.out.println("____________" + cardList.size());
////            }
//
////            bookRepository.getByFileName(filepath, cardList);
//
//        } catch (FileNotFoundException e) {
//            System.out.println("未找到指定路径的文件!");
//            e.printStackTrace();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void excel(String filepath) {
        try {
//            String filepath = "F:test.xls";
            CustomExcel excelReader = new CustomExcel(filepath);
            // 对读取Excel表格标题测试
//          String[] title = excelReader.readExcelTitle();
//          System.out.println("获得Excel表格的标题:");
//          for (String s : title) {
//              System.out.print(s + " ");
//          }

            // 对读取Excel表格内容测试
            Map<Integer, Map<Integer,Object>> map = excelReader.readExcelContent();
            System.out.println("获得Excel表格的内容:");
            for (int i = 1; i <= map.size(); i++) {
                System.out.println(map.get(i));
            }
        } catch (FileNotFoundException e) {
            System.out.println("未找到指定路径的文件!");
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }




    // 手动下载
//    @GetMapping("/download")
//    public String handleFileDownload(HttpServletRequest request, HttpServletResponse response) {
//
////        String imagesPath = request.getSession().getServletContext()
////                .getRealPath("images");
//        String docsPath = request.getSession().getServletContext()
//                .getRealPath("docs");
//
//        System.out.println(docsPath);
//
//        (new ExportExcel()).test(cardList, docsPath);
//        String fileName = "export2003_a.xls";
//        String filePath = docsPath + FILE_SEPARATOR + fileName;
//
//        System.out.println(filePath);
//
//        download(filePath, response);
//        return "download";
//    }

    private void download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes(),"ISO-8859-1"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    // 处理文件上传
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

//        logger.info("upload-dir/" + file.getOriginalFilename());
//        SimpleFileRepository fileRepository = new SimpleFileRepository();
//        fileRepository.getByFileName("upload-dir/" + file.getOriginalFilename());
//        outPutCard("upload-dir/" + file.getOriginalFilename());

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {

        System.out.println("StorageFileNotFoundException");

        return ResponseEntity.notFound().build();
    }

}
