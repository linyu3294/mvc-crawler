package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import model.ISpider;
import model.queue.QueueSpider;
import view.CSV;

/**
 * This is a class that implements IController. This controller class runs with a
 * QueueSpider model and uses the Email View. The controller keeps model and view seperate and
 * manages data flow between them. To give context, QueueSpider model uses a queue-based breadth first
 * search algorithm and checks the response code of a url at each iteration of the queue crawl.
 */
public class QueueController extends AbstractController implements IController {
private HashMap<String, String[]> queueSpiderModel;
CSV pagesCheckedFile;
CSV errors;


/**
 * A constructor for QueueControler. It supers AbstractController. In addition, it instantiates a
 * empty Hashmap. This HashMap will be used to clone and keep a copy of the results after
 * QueueSpider has completed it's crawling.
 *
 * @param baseUrls - A set of base urls that will passed to and used in model for crawling.
 */
public QueueController (HashSet<String> baseUrls) {
   super(baseUrls);
   queueSpiderModel = new HashMap<String, String[]>();
}


/**
 * Runs the QueueSpider Model.
 *
 * @param spider - A chosen ISpider model used for crawling.
 */
@Override
public void run (ISpider spider) throws IOException {
   this.resourcesFolderPath = spider.getResourcesFolderPath();
   super.run(spider);
   QueueSpider queueSpider = (QueueSpider) spider;
   this.queueSpiderModel = queueSpider.getQueueSpiderModel();

   for (Map.Entry mapElement : queueSpiderModel.entrySet()) {
      initPagesCheckedCSV();
      String url = (String) mapElement.getKey();
      String parent = ((String[]) mapElement.getValue())[0];
      String statusCode = ((String[]) mapElement.getValue())[1];
      String responseTimeInMiliSec = ((String[]) mapElement.getValue())[2];

      String[] csvEntry = new String[]{
         statusCode, responseTimeInMiliSec, url, parent
      };


      pagesCheckedFile.appendRow(csvEntry);
      evalResponseAndWriteToMap(Integer.valueOf(statusCode));
      if (!statusCode.trim().equals("200")) {
         initErrorsCSV();
         errors.appendRow(csvEntry);
      }
   }
}


private String[] buildTitle () {
   return new String[]{
      "response status",
      "response time (ms)",
      "url",
      "parent"
   };
}


private void initPagesCheckedCSV () throws IOException {
   if (pagesCheckedFile == null) {
      pagesCheckedFile = new CSV(""
         + this.resourcesFolderPath
         + "pagesChecked/pages_checked_"
         + this.dateStamp
         + ".csv");
      pagesCheckedFile.appendRow(buildTitle());
   }
}


private void initErrorsCSV () throws IOException {
   if (errors == null) {
      errors = new CSV(""
         + this.resourcesFolderPath
         + "errors/errors_"
         + this.dateStamp
         + ".csv");
      errors.appendRow(buildTitle());
   }
}


}
