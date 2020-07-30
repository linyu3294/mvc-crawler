package model;


import model.ISpider;
import model.queue.QueueSpider;
import model.soup.SoupSpider;

/**
 * A Factory used in main to instantiate an ISpider object.
 */
public class ModelFactory {
   public static ISpider make (String crawlerType, String resourcesFolderPath){
      ISpider spider =null;
      if (crawlerType.toUpperCase().equals("QUEUE")){
         spider = new QueueSpider(resourcesFolderPath);
      }
      else if (crawlerType.toUpperCase().equals("SOUP")){
         spider = new SoupSpider(resourcesFolderPath);
      }
      return spider;
   }
}
