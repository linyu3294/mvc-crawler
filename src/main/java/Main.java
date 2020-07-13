import org.testng.annotations.Test;

import java.util.HashSet;

import controller.IController;
import controller.SoupController;

public class Main {
  public final static String resourcesPath = "src/resources/";

  /**
   * This is the Main Test. It's included in the testng.xml.
   */
  @Test
  public static void main() {
    HashSet<String> baseUrls = new HashSet<>();
//    baseUrls.add("http://newpv02-plymouthrock/_homenew");
//    baseUrls.add("https://pracblog.com/");
//    baseUrls.add("http://newpv02-plymouthrock/");
    baseUrls.add("https://www.plymouthrock.com");
//    baseUrls.add("http://www.pilgrimins.com");
//    baseUrls.add("http://www.bunkerhillins.com");
//    baseUrls.add("http://www.twinlightsins.com");
//    baseUrls.add("http://www.stsgi.com");
//    baseUrls.add("https://aarp.plymouthrock.com");
//    baseUrls.add("http://www.pracathome.com");
//    baseUrls.add("http://www.prac.com");
//    baseUrls.add("http://www.prac.com/utility/terms-popup");
//    baseUrls.add("http://www.prac.com/utility/privacy-policy-popup");
//    baseUrls.add("http://www.prac.com/aarp");
//    baseUrls.add("http://NJARQUOTE.com");
//    baseUrls.add("http://www.pateachersquote.com");
//    baseUrls.add("http://njrealtorsrock.com");

    IController controller = new SoupController(baseUrls,resourcesPath);
    controller.run();
    controller.report();

  }
}
