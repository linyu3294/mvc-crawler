//package controller;
//
//import java.io.File;
//import java.util.HashSet;
//
//import controller.IController;
//import model.soup.ISpider;
//import model.soup.ScShotSpider;
//
//
///**
// * This is a class that represents a Screenshot Model. The scShotModel implements IController. In
// * addition to checking response code, The screenshot Model employs a screenshot-based comparison
// * technique to compare url content at each recursive crawl iteration.
// */
//public class ScShotController implements IController {
//  private String resourcesFolder;
//  private HashSet<String> baseUrls;
//  private String baseDateFolder;
//  private String testDateFolder;
//  private boolean overwriteTDF;
//  private boolean makeComparison;
//
//
//  /**
//   * This is the Base constructor for scShotModel.
//   *
//   * @param resourcesFolder - the root folder that contains all non-code based resources.
//   * @param baseUrls        - a list of urls to be compared.
//   * @param baseDate        -  a date referenced in association with a collection of screenshots
//   *                        used as a base for comparison.
//   * @param testDate        -  a date referenced in association with a collection of screenshots
//   *                        used as a subject of comparison.
//   * @param overwriteTDF    - passed as an argument to init a scShotSpider object in model's run
//   *                        method, if true, testDateFolder will be overwritten or newly created
//   *                        during the test run.
//   * @param makeComparison  - passed as an argument to init a scShotSpider object in model's run
//   *                        method, if true, comparison will be made between testDateFolder and
//   *                        baseDateFolder.
//   */
//  public ScShotController (
//          HashSet<String> baseUrls,
//          String resourcesFolder,
//          String baseDate,
//          String testDate,
//          boolean overwriteTDF,
//          boolean makeComparison) {
//    this.baseUrls = baseUrls;
//    this.resourcesFolder = resourcesFolder;
//    this.baseDateFolder = resourcesFolder + "Screenshots/" + baseDate;
//    this.testDateFolder = resourcesFolder + "Screenshots/" + testDate;
//    this.overwriteTDF = overwriteTDF;
//    this.makeComparison = makeComparison;
//  }
//
//
//  /**
//   * This is the SECOND constructor for scShotModel.
//   *
//   * @param resourcesFolder - the root folder that contains all non-code based resources.
//   * @param baseUrls        - a list of urls to be compared.
//   * @param baseDate        -  a date referenced in association with a collection of screenshots
//   *                        used as a base for comparison.
//   * @param testDate        -  a date referenced in association with a collection of screenshots
//   *                        used as a subject of comparison.
//   */
//  public ScShotController (
//          HashSet<String> baseUrls,
//          String resourcesFolder,
//          String baseDate,
//          String testDate) {
//    this(
//            baseUrls,
//            resourcesFolder,
//            baseDate,
//            testDate,
//            true,
//            true);
//  }
//
//
//  /**
//   * This method runs the model, which will invoke a list of urls to be checked and run a ISpider
//   * object of a specified sort to recursively check the contents of the url and its children.
//   * ScShotController will run with a ScShotSpider Class object.
//   */
//  @Override
//  public void run() {
//    boolean baseDateFolderExist = new File(baseDateFolder).exists();
//    boolean testDateFolderExist = new File(testDateFolder).exists();
//    if (!baseDateFolderExist) {
//      throw new IllegalStateException("The folder that contains screenshots "
//              + "associated with a base-date is missing! "
//              + "Please choose a valid date for a base-date.");
//    }
//
//    if (overwriteTDF == false) {
//      if (!testDateFolderExist) {
//        throw new IllegalStateException("The folder that contains screenshots "
//                + "associated with a compare-date is missing! "
//                + "Please choose a valid date for a base-date.");
//      }
//    } else {
//      if (testDateFolderExist) {
//        new File(testDateFolder).delete();
//      }
//      new File(testDateFolder).mkdir();
//    }
//
//
//    try {
//      for (String baseUrl : baseUrls) {
//        ISpider spider = new ScShotSpider(
//                this.resourcesFolder,
//                this.baseDateFolder,
//                this.testDateFolder,
//                this.overwriteTDF,
//                this.makeComparison);
//        spider.crawl(baseUrl, "Main URL");
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//
//  /**
//   * reports the result.
//   */
//  @Override
//  public void report() {
//
//  }
//}
