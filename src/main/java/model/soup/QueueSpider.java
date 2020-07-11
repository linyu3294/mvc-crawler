package model.soup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A QueueSpider class extends from AbstracSpider and is a sibling to SoupSpider. Some differences
 * are it uses a breadth-first-search strategy as opposed to depth-first-search. It also has an
 * additional feature that allows recording of each url loading time that it visits. This feature
 * was not available in SoupSpider.
 */
public class QueueSpider extends AbstractSpider {
Queue<String[][]> queue;

/**
 * Constructor for QueueSoup.
 */
protected QueueSpider (String resourcesFolder) {
   super(resourcesFolder);
   //
   queue = new LinkedList<String[][]>();
}

/**
 * QueueSpider can crawl a url and its children and verify their contents. QueueSoup uses a Queue
 * with a breadth-first-search recursive strategy as opposed to depth-first-search strategy
 * implemented in SoupSpider.
 */
@Override
public void crawl (String url, String parent) throws IOException {
   setTestCoverage(url);
   setPagesIgnored();
   String inUrls;
   String outUrl;

   while (!queue.isEmpty()) {
      Document document = Jsoup.connect(url)
         .followRedirects(true)
         .timeout(60000)
         .get();
      Elements children = document.select("a[href]");
      String[][] childrenUrls = new String[children.size()][2];
      for (int i = 0; i < children.size(); i++) {
         childrenUrls[i][0] = children.get(i).baseUri();
      }
      queue.add(childrenUrls);

      String popedUrl[] = queue.poll()[0];
      System.out.println(popedUrl[0]);
   }
}


}


