package model;

import java.io.IOException;

/**
 * Interface used for implementing spider classes.
 */
public interface ISpider {
/**
 * Spiders can crawl a url and its children and verify their contents, using variety of methods,
 * recursive, bfs, and etc. The SoupSpider implements a recursive depth first search method while the
 * QueueSpider implements a breadth first search method with a queue.
 */
void crawl (String url, String parent) throws IOException;

/**
 * This method returns the path to the project's resource folder
 * @return String that represents the path to the project's resource folder;
 */
String getResourcesFolderPath ();
}
