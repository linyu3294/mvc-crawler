package model.soup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 *
 * This is a class for crawling and checking reponse status code of a url and its children.
 * It uses a recursive method to travers a url tree.
 */
public class SoupSpider extends AbstractSpider {

  /**
   * This is the constructor of SoupSpider Class.
   */
  public SoupSpider(String resourcesFolder) {
    super(resourcesFolder);
  }


  /**
   * Does - Recursively crawls and checks the status of every embedded child url.
   *
   * /** Spiders can recursively crawl a url and its children and verify their contents. This Spider
   * leverages most of all protected functions in the AbstractSpider Class.
   *
   * @param url    - The url of the current iteration in recursive crawl.
   * @param parent - The parent of the current iteration in recursive crawl, initially the same as
   *               the starting address. but gets updated with each successive recursive iteration.
   *               used to keep track of the parent at each iteration, so that if a child is found
   *               broken, the parent is logged as well as the child.
   * @throws IOException - When Base Host URL is down, the one that's used to enter a recursive *
   *                     crawl.
   */


  public void crawl (String url, String parent) throws IOException {
    if (pagesVisited.isEmpty()) {
      setTestCoverage(url);
      setPagesIgnored();
    }
    if ( ! pagesVisited.contains(url) ) {
      if ( statusCodeIsOK(url, parent) ) {
        System.out.println(url);
        Document document = Jsoup.connect(url).followRedirects(true).timeout(60000).get();
        Elements children = document.select("a[href]");
        for (Element child : children) {
          String childUrl = getUrl(child);
          if (isValidURL(childUrl)) {
            crawl(childUrl, url);
          }
        }
      }
    }
  }

}
