import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import controller.IController;
import controller.QueueController;
import controller.SoupController;
import model.soup.ISpider;
import model.soup.QueueSpider;
import model.soup.SoupSpider;
import view.Email;
import view.IEmail;

public class Main {
  public final static String resourcesFolderPath = "src/resources/";


  public static void main(String [] args) throws IOException {

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

    ISpider soupSpider = new QueueSpider(resourcesFolderPath);

    IController controller = new QueueController(baseUrls);
    controller.run(soupSpider);

    List<String> listOfCCs = new ArrayList<String>();
    listOfCCs.add("ylin@plymouthrock.com");
    listOfCCs.add("ylin@plymouthrock.com");

    String report = controller.createReport();
    String dateStamp = controller.getDateStamp();
    IEmail email = new Email("PRCRELAY.PRCINS.NET");
    try {
      email = email
         .setSender("Z@plymouthrock.com")
         .setReceiver("ylin@plymouthrock.com")
         .setListOfCCs(listOfCCs)
         .setSubject("Public_Website_Regression_Results_" + dateStamp)
         .setAttachment(
            resourcesFolderPath +"/errors/errors_" + dateStamp + ".txt",
            "errors_" + dateStamp + ".txt")
         .setAttachment(
            resourcesFolderPath + "/pagesChecked/pages_checked_" + dateStamp +".txt",
            "pages_checked_" + dateStamp + ".txt")
         .setAttachment(
            resourcesFolderPath + "/pages_ignored.txt",
            "pages_ignored.txt")
         .setEmailBody(report);
    }catch (Exception ignored){}


    controller.sendReportInEmail(email);

  }
}
