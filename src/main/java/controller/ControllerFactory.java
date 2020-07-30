package controller;

import java.io.IOException;
import java.util.HashSet;

import model.soup.ISpider;
import model.soup.QueueSpider;
import model.soup.SoupSpider;

/**
 * A Factory used in main to instantiate a IController object.
 */
public class ControllerFactory {
   public static IController make (String crawlerType, HashSet <String>baseUrls)
      throws IOException {

      IController controller =null;
      if (crawlerType.toUpperCase().equals("QUEUE")){
         controller = new QueueController(baseUrls);
      }
      else if (crawlerType.toUpperCase().equals("SOUP")){
         controller = new SoupController(baseUrls);
      }
      return controller;
   }
}