import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import controller.ControllerFactory;
import controller.IController;
import model.ISpider;
import model.ModelFactory;
import view.EmailFactory;
import view.IEmail;

public class Main {


public static void main (String[] args) throws IOException {
   String resourcesFolderPath = "src/resources/";
   String crawlerType = "QUEUE";

   HashSet<String> baseUrls = new HashSet<>();
   baseUrls.add("https://www.google.com");
   baseUrls.add("https://www.instagram.com");


   String param;
   int i = 0;
   if (args.length > 0) {
      while (i < args.length) {
         param = args[i];
         if (param.equals("-type")) {
            crawlerType = args[i + 1];
         }

         if (param.equals("-outputPath")) {
            resourcesFolderPath = args[i + 1];
         }
         i++;
      }
   }

   IController controller = ControllerFactory.make(crawlerType, baseUrls);
   ISpider spider = ModelFactory.make(crawlerType, resourcesFolderPath);
   controller.run(spider);

   List<String> listOfCCs = new ArrayList<String>();
   listOfCCs.add("rstrohmenger@plymouthrock.com");
   listOfCCs.add("cuhlar@plymouthrock.com");

   String report = controller.createReport();
   String dateStamp = controller.getDateStamp();


   try {
      IEmail email = EmailFactory.make(
         controller,
         listOfCCs,
         dateStamp,
         resourcesFolderPath,
         report
      );
      controller.sendReportInEmail(email);
   } catch (Exception e) {
      e.printStackTrace();
   }
}
}
