package model.soup;

import java.io.IOException;

/**
 * This is an interface used for implementing Spiders
 */
public interface ISpider {
/**
 * Spiders can crawl a url and its children and verify their contents, using variety of methods,
 * recursive or bfs. The SoupSpider implements a recursive depth first search method while the
 * QueueSpider implements a breadth first search method with a queue.
 */
void crawl (String url, String parent) throws IOException;

/**
 * This method returns the path to the project's resource folder
 * @return String that represents the path to the project's resource folder;
 */
String getResourcesFolderPath ();
}
