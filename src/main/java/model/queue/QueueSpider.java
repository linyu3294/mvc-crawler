package model.queue;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

import model.AbstractSpider;
import model.ISpider;

/**
 * A QueueSpider class extends from AbstracSpider and is a sibling to SoupSpider. A key difference
 * is that the QueueSpider uses a breadth-first-search strategy whereas the SoupSpider uses
 * depth-first-search strategy. QueueSpider also has an additional feature that allows it to record
 * the loading time each child url it crawls. This feature is not available in SoupSpider.
 */
public class QueueSpider extends AbstractSpider implements ISpider {
private LinkedList<String> queue;
private HashMap<String, String> parentMap;
private HashMap<String, String[]> queueSpiderModel;


/**
 * Constructor for QueueSoup.
 */
public QueueSpider (String resourcesFolder) {
   super(resourcesFolder);
   queue = new LinkedList<String>();
   parentMap = new HashMap<String, String>();
   queueSpiderModel = new HashMap<String, String[]>();
}


/**
 * QueueSpider can crawl a website and its children urls, verify their http status codes, and record
 * DOM loading time. QueueSoup uses a breadth-first-search algorithm and a queue data structure.
 *
 * @param url    - The main website url.
 * @param parent - Not used. Slightly bad design, but trade benefit is saving a lot of unesscessary
 *               refactoring of code.
 */
@Override
public void crawl (String url, String parent) throws IOException {
   this.setPagesIgnored();
   this.setTestCoverage(url);
   queue.add(url);

   while (!queue.isEmpty()) {
      String firstOut = (queue.get(0));
      Document document = null;

      try {
         document = getDocument(firstOut);
         Elements elements = document.select("a[href]");
         Set set = dedupeDiscoveredChildren(elements);
         addValidChildrenToQueue(set, firstOut);
      } catch (Exception e) {
         continue;
      } finally {
         popVisitedChildFromQueue();
         appendQueueSpiderModel(firstOut, parentMap.get(firstOut));
      }
   }
}


/**
 * @return - The model of the collected result as a hashmap containing,  url as key, and String [3]
 * {parent, statuscode, responseTime} as value.
 */
public HashMap<String, String[]> getQueueSpiderModel () {
   return queueSpiderModel;
}


private void appendQueueSpiderModel (String exitChild, String parent) {
   String[] statusCodeAndResponseTime = new String[2];
   try {
      statusCodeAndResponseTime = getStatusCodeAndResponseTime(exitChild);
   } catch (Exception e) {
      statusCodeAndResponseTime = handleUnsuccessfulResponse(e);
   }
   String statusCode = statusCodeAndResponseTime[0];
   String responseMiliSec = statusCodeAndResponseTime[1];
   queueSpiderModel.put(exitChild,
      new String[]{parent, statusCode, responseMiliSec}
   );
   printInConsole(statusCode, responseMiliSec, exitChild, parent);
}


private String[] handleUnsuccessfulResponse (Exception e) {
   if (e instanceof HttpStatusException) {
      return new String[]{((HttpStatusException) e).getStatusCode() + "", ""};
   } else if (e instanceof UnsupportedMimeTypeException) {
      return new String[]{"UnsupportedMimeTypeException", "Runtime UnsupportedMimeTypeException"};
   } else if (e instanceof IOException) {
      return new String[]{"IOException", "Page Timed Out"};
   } else {
      return new String[]{"General Error", ""};
   }
}


private void printInConsole (String statusCode,
                             String responseMiliSec,
                             String exitChild,
                             String parent) {
   System.out.println("      "
      + statusCode + "       "
      + responseMiliSec + "      "
      + "url: " + exitChild + "     "
      + "parent: " + parent + "     "
   );
}


private String[] getStatusCodeAndResponseTime (String url) throws Exception {
   long startTime = System.nanoTime();
   long status = Jsoup.connect(url)
      .followRedirects(true)
      .timeout(60000)
      .execute()
      .statusCode();
   long endTime = System.nanoTime();
   long responseInMiliSec = (endTime - startTime) / 1000000;
   return new String[]{String.valueOf(status), String.valueOf(responseInMiliSec)};
}


private Document getDocument (String url) throws IOException {
   return Jsoup.connect(url)
      .followRedirects(true)
      .timeout(60000)
      .get();
}


private boolean isValidInScopeURL (String url, String domain) {
   return
      url.contains(this.domain) && isValidURL(url);
}


private String popVisitedChildFromQueue () {
   String exitChild = queue.get(0);
   pagesVisited.add(exitChild);
   queue.remove(0);
   return exitChild;
}


private Set dedupeDiscoveredChildren (Elements elements) {
   Set dedupedElements =
      Arrays.asList(elements.toArray())
         .stream()
         .map(element -> getUrl((Element) element))
         .collect(Collectors.toSet());
   return dedupedElements;
}


private void addValidChildrenToQueue (Set set, String parentUrl) {
   Iterator<String> iter = set.iterator();
   while (iter.hasNext()) {
      String childUrl = iter.next();
      if (isValidInScopeURL(childUrl, this.domain)
         && !pagesVisited.contains(childUrl)
         && !queue.contains(childUrl)
      ) {
         parentMap.put(childUrl, parentUrl);
         queue.add(childUrl);
      }
   }
}


}


