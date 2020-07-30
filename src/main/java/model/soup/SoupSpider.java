package model.soup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import model.AbstractSpider;


/**
 * This is a class for crawling and checking response status code of a website and all of its
 * children. It uses a recursive depth-first-search to traverse the website.
 */
public class SoupSpider extends AbstractSpider {


/**
 * This is the constructor of SoupSpider Class.
 */
public SoupSpider (String resourcesFolderPath) {
   super(resourcesFolderPath);
}


/**
 * Recursively crawls and checks the status of every embedded child url.
 *
 * @param url    - The url of the current iteration in recursive crawl.
 * @param parent - The parent of the current iteration in recursive crawl, initially the same as the
 *               starting address. but gets updated with each successive recursive iteration. used
 *               to keep track of the parent at each iteration, so that if a child is found broken,
 *               the parent is logged as well as the child.
 *
 * @exception IOException - When Base Host URL is down, the one that's used to enter a recursive *
 *                        crawl.
 */
public void crawl (String url, String parent) throws IOException {
   if (pagesVisited.isEmpty()) {
      setTestCoverage(url);
      setPagesIgnored();
   }
   if (!pagesVisited.contains(url)) {
      if (statusCodeIsOK(url, parent)) {
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
