package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 * This is a class that implements IController. This controller class runs with a
 * SoupSpider model and uses the Email View. The controller keeps model and view seperate and
 * manages data flow between them. To give context, SoupSpider model uses a recursive depths first
 * search algorithm and checks the response code of a url at each iteration of the recursive crawl.
 */
public class SoupController extends AbstractController implements  IController{

/**
 * A constructor for SoupControler. It supers AbstractController.
 *
 * @param baseUrls - A set of base urls that will passed to and used in model for crawling.
 */
public SoupController (HashSet<String> baseUrls) {
   super(baseUrls);
}


private void writeCrawlerResultToMap () throws IOException {
   BufferedReader crawlerResultLog = openCrawlerResultLog(
      ""
         + this.resourcesFolderPath
         + "pagesChecked/pages_checked_"
         + this.dateStamp
         + ".txt");
   mapAllStatusCodes(crawlerResultLog);
}


private BufferedReader openCrawlerResultLog (String pathToCrawlerResultLog) throws IOException {
   return new BufferedReader(
      new FileReader(pathToCrawlerResultLog));
}


private void mapAllStatusCodes (BufferedReader logOfStatusCodes) throws IOException {
   String line;
   while ((line = logOfStatusCodes.readLine()) != null) {
      if (line.contains("Status Code:    ")) {
         try {
            Integer statusCode = Integer.valueOf(
               line.split("Status Code:    ")[1].trim());
            evalResponseAndWriteToMap(statusCode);
         } catch (Exception e) {
         }
      }
   }
}

}



