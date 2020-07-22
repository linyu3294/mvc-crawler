import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import controller.IController;
import controller.SoupController;
import model.soup.ISpider;
import model.soup.SoupSpider;
import view.Email;
import view.IEmail;

public class Main {
  public final static String resourcesFolderPath = "src/resources/";


  public static void main(String [] args) throws IOException {

    HashSet<String> baseUrls = new HashSet<>();
    baseUrls.add("http://newpv02-plymouthrock/_homenew");
    baseUrls.add("https://pracblog.com/");
    baseUrls.add("http://newpv02-plymouthrock/");
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

    ISpider soupSpider = new SoupSpider(resourcesFolderPath);

    IController controller = new SoupController(baseUrls);

    controller.run(soupSpider);

    List<String> listOfCCs = new ArrayList<String>();
    listOfCCs.add("ylin@plymouthrock.com");
    listOfCCs.add("ylin@plymouthrock.com");

    String report = controller.createReport();

    IEmail email = new Email("PRCRELAY.PRCINS.NET");
    try {
      email = email
         .setSender("Z@plymouthrock.com")
         .setReceiver("ylin@plymouthrock.com")
         .setListOfCCs(listOfCCs)
         .setSubject("Test", "")
         .setAttachment("src/resources/Errors/errors_2019.07.08.txt", "Test 3.txt")
         .setAttachment("src/resources/Errors/errors_2019.07.08.txt", "Test 4.txt")
         .setAttachment("src/resources/Errors/errors_2019.07.08.txt", "Test 5.txt")
         .setEmailBody(report);
    }catch (Exception e){System.out.println("hello");}

    controller.sendReportInEmail(email);

  }
}
