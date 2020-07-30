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
//    baseUrls.add("http://newpv02-plymouthrock/_homenew");
//    baseUrls.add("http://newpv02-plymouthrock/");
   baseUrls.add("https://pracblog.com/");
   baseUrls.add("https://www.plymouthrock.com");
   baseUrls.add("http://www.pilgrimins.com");
   baseUrls.add("http://www.bunkerhillins.com");
   baseUrls.add("http://www.twinlightsins.com");
   baseUrls.add("http://www.stsgi.com");
   baseUrls.add("https://aarp.plymouthrock.com");
   baseUrls.add("http://www.pracathome.com");
   baseUrls.add("http://www.prac.com");
   baseUrls.add("http://www.prac.com/utility/terms-popup");
   baseUrls.add("http://www.prac.com/utility/privacy-policy-popup");
   baseUrls.add("http://www.prac.com/aarp");
   baseUrls.add("http://NJARQUOTE.com");
   baseUrls.add("http://www.pateachersquote.com");
   baseUrls.add("http://njrealtorsrock.com");


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
