package model.soup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * A QueueSpider class extends from AbstracSpider and is a sibling to SoupSpider. Some differences
 * are it uses a breadth-first-search strategy as opposed to depth-first-search. It also has an
 * additional feature that allows recording of each url loading time that it visits. This feature
 * was not available in SoupSpider.
 */
public class QueueSpider extends AbstractSpider implements ISpider {
private String mainUrl;
private LinkedList<String[]> queue;

/**
 * Constructor for QueueSoup.
 */
public QueueSpider (String resourcesFolder) {
   super(resourcesFolder);
   queue = new LinkedList<String[]>();
}

private boolean isValidInScopeURL (String url, String domain) {
   boolean isInScope = false;
   if (isValidURL(url)) {
      isInScope = url.contains(this.domain);
   }
   return isInScope;
}

/**
 * QueueSpider can crawl a url and its children and verify their contents. QueueSoup uses a Queue
 * with a breadth-first-search recursive strategy as opposed to depth-first-search strategy
 * implemented in SoupSpider.
 */
@Override
public void crawl (String url, String parent) throws IOException {
   this.setTestCoverage(url);
   this.setPagesIgnored();
   queue.add(new String[]{url});
 

   while (!queue.isEmpty() ) {
      String pointedUrl = (queue.get(0) [0]);
      Document document = Jsoup.connect(pointedUrl)
         .followRedirects(true)
         .timeout(60000)
         .get();
      Elements elements = document.select("a[href]");

      HashSet dedupedElements = new LinkedHashSet(Arrays.asList(elements.toArray()));
      Iterator<Element> it = dedupedElements.iterator();
      while (it.hasNext()) {
         String childUrl = getUrl(it.next());
         if (isValidInScopeURL(childUrl, this.domain)
            && !pagesVisited.contains(childUrl)) {
            queue.add(new String[]{childUrl});
         }
      }

      String exitChild = queue.get(0) [0];
      queue.remove(0);
      pagesVisited.add(exitChild);
      System.out.println(exitChild);
   }
   System.out.println("Finished this round");
}

}


