package controller;

import java.util.HashSet;

import model.report.SoupReport;
import model.soup.SoupSpider;

/**
 * This is a class implement interface IController for url_crawler. This class implement the IController and
 * runs with a SoupSpider. SoupSpider employs JSoup's Html parsing technology and checks the
 * response code of a url at each iteration of the recursive crawl.
 */
public class SoupController implements IController {
  private String resourcesFolder;
  private HashSet<String> baseUrls;

  public SoupController (HashSet<String> baseUrls, String resourcesFolder) {
    this.baseUrls = baseUrls;
    this.resourcesFolder = resourcesFolder;
  }

  public void run() {
    try {
      for (String baseUrl : baseUrls) {
        SoupSpider spider = new SoupSpider(this.resourcesFolder);
        spider.crawl(baseUrl, "Main URL");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void report() {
    SoupReport report = new SoupReport(this.resourcesFolder);
    report.sendEmail(this.baseUrls);
  }

}
