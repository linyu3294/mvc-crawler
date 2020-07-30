package model.screen;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.github.bonigarcia.wdm.WebDriverManager;
import model.AbstractSpider;
import model.screen.IScnShotCompare;
import model.screen.ScnShotCompare;
import model.ISpider;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
//import ru.yandex.qatools.ashot.AShot;
//import ru.yandex.qatools.ashot.Screenshot;
//import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

/**
 * This is an class that represents a Screen Shot ISpider. This Class extends AbstractSpider and
 * implements ISpider.ScShotSpider Uses Selenium and Ashot to take screenshots. Compares screenshots
 * located in testDateFolder to those located in baseDateFolder. Depending the truthiness / or
 * falseness of argument - boolean overwriteTDF, the testDateFolder can be either newlyCreated/
 * overwritten as part the recursive crawl if overwriteTDF is set as true, or can be kept to have
 * the same existing content if folder already contains content from previous crawl and if boolean
 * overwriteTDF is set as false.
 */
public class ScShotSpider extends AbstractSpider implements ISpider {
  private String baseDateFolder;
  private String testDateFolder;
  private boolean overwriteTDF;
  private boolean makeComparison;
  private WebDriver driver;
  private JavascriptExecutor js;
  private int numPixelsPerScroll;


  /**
   * This is the Base Constructor for ScShotSpider Class.
   *
   * @param resourcesFolder - The folder that keeps resources other than code.
   * @param baseDateFolder  - The folder that contains screenshots for base.
   * @param testDateFolder  - The folder that contains screenshots for testing assertion of
   *                        equalness against screenshots kept in baseDateFolder.
   * @param overwriteTDF    - if true, testDateFolder will be overwritten or newly created during
   *                        the test run.
   * @param makeComparison  - if true, comparison will be made between testDateFolder and
   *                        baseDateFolder.
   */
  public ScShotSpider(
          String resourcesFolder,
          String baseDateFolder,
          String testDateFolder,
          boolean overwriteTDF,
          boolean makeComparison
  ) {
    super(resourcesFolder);
    this.baseDateFolder = baseDateFolder;
    this.testDateFolder = testDateFolder;
    this.overwriteTDF = overwriteTDF;
    this.makeComparison = makeComparison;
    this.driver = create("src/resources/selenium_drivers/chromedriver.exe");
    js = (JavascriptExecutor) driver;
  }


  /**
   * This is the Second Constructor for ScShotSpider Class. In contrast to Base Constructor, this
   * particular Constructor does not require boolean arguments (overwriteTDF, makeComparison), and
   * sets them both as true by default.
   *
   * @param resourcesFolder - The folder that keeps resources other than code.
   * @param baseDateFolder  - The folder that contains screenshots for base.
   * @param testDateFolder  - The folder that contains screenshots for testing assertion of
   *                        equalness against screenshots kept in baseDateFolder.
   */
  public ScShotSpider(
          String resourcesFolder,
          String baseDateFolder,
          String testDateFolder
  ) {
    this(
            resourcesFolder,
            baseDateFolder,
            testDateFolder,
            true,
            true
    );
  }

  /**
   * Spiders can recursively crawl a url and its children and verify their contents. This Spider
   * leverages all protected functions in the AbstractSpider Class.
   *
   * @param url    - The url of the current iteration in recursive crawl.
   * @param parent - The parent of the current iteration in recursive crawl, initially the same as
   *               the starting address. but gets updated with each successive recursive iteration.
   *               used to keep track of the parent at each iteration, so that if a child is found
   *               broken, the parent is logged as well as the child.
   * @throws IOException - When Base Host URL is down, the one that's used to enter a recursive
   *                     crawl.
   */
  @Override
  public void crawl (String url, String parent) throws IOException {
    if (pagesVisited.isEmpty()) {
      setTestCoverage(url);
      setPagesIgnored();
    }
    if (pagesVisited.contains(url) == false) {
      if (statusCodeIsOK(url, parent) == true) {
        System.out.println(url);
        Document document = Jsoup.connect(url).followRedirects(true).timeout(60000).get();
        Elements children = document.select("a[href]");

        if (this.overwriteTDF) {
          captureThisScroll(url);
        }
        if (this.makeComparison) {
          compareScroll(url);
        }

        for (Element child : children) {
          String childUrl = getUrl(child);
          if (isValidURL(childUrl)) {
            crawl(childUrl, url);
          }
        }
      }
    }
  }


  /**
   * This is a private method that creates and returns a new default WebDriver object.
   *
   * @param driverPath - the path that points the chromedriver.exe used by Selenium.
   * @return - a newly created WebDriver object.
   */
  private WebDriver create(String driverPath) {
    WebDriver driver = null;
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    driver = new ChromeDriver(options);
    return driver;
  }

  /**
   * Takes a screenshot of the driver's current page.
   *
   * @param url - a url string.
   * @throws IOException - when unable to take a screenshot of a page.
   */
  private void captureThisScroll(String url) throws IOException {
    this.driver.get(url);
    try{this.driver.switchTo().alert().dismiss();} catch(Exception e) {}
    Screenshot myScreenshot =
            new AShot()
                    .shootingStrategy(
                            ShootingStrategies.viewportPasting(100)
                    )
                    .takeScreenshot(this.driver);
    ImageIO.write(
            myScreenshot.getImage(),
            "PNG",
            new File(this.testDateFolder + "/" + abbreviateName(url) + ".png"));
  }


  /**
   * This private method compares two screenshots of the same url using ScnShotCompare class.
   *
   * @param url - the string url.
   */
  private void compareScroll(String url) {
    System.out.println(this.baseDateFolder + "/" + abbreviateName(url));
    System.out.println(this.testDateFolder + "/" + abbreviateName(url));
    IScnShotCompare shotCompare = new ScnShotCompare(
            this.baseDateFolder + "/" + abbreviateName(url) + ".png",
            this.testDateFolder + "/" + abbreviateName(url) + ".png"
    );
    shotCompare.compare();
  }

  /**
   * Changes the url String to a new string with acceptable filename format.
   *
   * @param url - a url string.
   * @return - a new string with acceptable filename format.
   */
  private String abbreviateName(String url) {
    return url.replaceAll("https://", "")
            .replaceAll("http://", "")
            .replaceAll("/", "_");
  }
}
