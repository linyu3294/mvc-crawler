package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import model.soup.ISpider;
import model.soup.QueueSpider;
import view.CSV;

public class QueueController extends AbstractController {
private HashMap<String, String[]> queueSpiderModel;
CSV pagesCheckedFile;
CSV errors;

public QueueController (HashSet<String> baseUrls) throws IOException {
   super(baseUrls);
   queueSpiderModel = new HashMap<String, String[]>();

}

@Override
public void run (ISpider spider) throws IOException {
   this.resourcesFolderPath = spider.getResourcesFolderPath();
   try {
      super.run(spider);
   } catch (Exception ignored) {
   }
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

      evalResponseAndWriteToMap(Integer.valueOf(statusCode));
      if (!statusCode.trim().equals("200")) {
         initErrorsCSV();
         errors.appendRow(csvEntry);
      } else {
         pagesCheckedFile.appendRow(csvEntry);
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
