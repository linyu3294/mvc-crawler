package controller;

/**
 * this is an interface for implement various types of url_crawler models.
 */
public interface IController {
  /**
   * This method runs the model, which will invoke a list of urls to be checked and run a ISpider
   * object of a specified sort to recursively check the contents of the url and its children.
   */
  void run();

  /**
   * reports the result.
   */
  void report();
}
