package model.soup;

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
