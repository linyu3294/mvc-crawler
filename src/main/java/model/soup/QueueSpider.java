package model.soup;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A QueueSpider class extends from AbstracSpider and is a sibling to SoupSpider. Some differences
 * are it uses a breadth-first-search strategy as opposed to depth-first-search. It also has an
 * additional feature that allows recording of each url loading time that it visits. This feature
 * was not available in SoupSpider.
 */
public class QueueSpider extends AbstractSpider implements ISpider {
private String mainUrl;
private LinkedList<String> queue;

/**
 * Constructor for QueueSoup.
 */
public QueueSpider (String resourcesFolder) {
   super(resourcesFolder);
   queue = new LinkedList<String>();
}

private boolean isValidInScopeURL (String url, String domain) {
   return
      url.contains(this.domain) && isValidURL(url);
}

/**
 * QueueSpider can crawl a url and its children and verify their contents. QueueSoup uses a Queue
 * with a breadth-first-search non-recursive strategy as opposed to depth-first-search recursive
 * strategy implemented in SoupSpider.
 */
@Override
public void crawl (String url, String parent) throws IOException {
   this.setPagesIgnored();
   this.setTestCoverage(url);
   queue.add(url);


   while (!queue.isEmpty()) {
      String pointedUrl = (queue.get(0));
      Document document =null;

      try{
      document = Jsoup.connect(pointedUrl)
         .followRedirects(true)
         .timeout(60000)
         .get();}
      catch (HttpStatusException e) {
         System.out.println(""
            + "\n\nUnable to fetch url. \n"
            + pointedUrl + "\n"
            + "Status = " + e.getStatusCode() + ".\n\n");
         String exitChild = queue.get(0);
         queue.remove(0);
         pagesVisited.add(exitChild);
         continue;
      }

      Elements elements = document.select("a[href]");
      Set dedupedElements =
         Arrays.asList(elements.toArray())
            .stream()
            .map(element -> getUrl((Element) element))
            .collect(Collectors.toSet());

      Iterator<String> iter = dedupedElements.iterator();
      while (iter.hasNext()) {
         String childUrl = iter.next();
         if (isValidInScopeURL(childUrl, this.domain)
            && !pagesVisited.contains(childUrl)
            && !queue.contains(childUrl)
         ) {
            queue.add(childUrl);
         }
      }

      String exitChild = queue.get(0);
      queue.remove(0);
      pagesVisited.add(exitChild);
      System.out.println(exitChild);
   }

   System.out.println("Finished this round");
}



}


