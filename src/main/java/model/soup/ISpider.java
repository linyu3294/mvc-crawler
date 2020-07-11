package model.soup;

import java.io.IOException;

/**
 * This is an interface used for implementing Spiders
 */
public interface ISpider {
  /**
   * Spiders can recursively crawl a url and its children and verify their contents.
   */
  void crawl (String url, String parent) throws IOException;
}
