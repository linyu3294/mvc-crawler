package model.soup;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;

/**
 * This is an abstract Class that implements ISpider.
 */
public abstract class AbstractSpider implements ISpider{
  protected HashSet<String> pagesVisited;
  protected String domain;
  protected String resourcesFolder;

  /**
   * constructor of AbstractSpider.
   *
   * @param resourcesFolder
   */
  protected AbstractSpider (String resourcesFolder) {
    this.resourcesFolder = resourcesFolder;
    this.pagesVisited = new HashSet<String>();
    this.domain = "";
  }


  /**
   * Defines the range of crawl and saves to this.domain. Once the test starts,
   * setTestCoverage(url) is called only during, the first instance crawl(url) is called.
   * SetTestCoverage(url) will define this.domain. This.domain is used to limit the
   * range of the crawl in each recursive instance. If any children url does not contain
   * domain, then that child will not be checked.
   *
   * @param mainURL - the url that confines the scope of a crawl check. The crawler will not reach
   *                beyond the domain.
   */
  protected void setTestCoverage(String mainURL) throws IOException {
    try {
      Document document =
         Jsoup.connect(mainURL)
         .followRedirects(true)
            .timeout(60000).get();
      String parsedURL[] = document.location().split("/");
      if (parsedURL.length > 2) {
        this.domain = document.location();
      } else {
        this.domain = parsedURL[2];
      }
      log("\n\nurlHost ", domain, domain, "PagesChecked/pages_checked");
//      System.out.println("______________________________________");
    } catch (IOException e) {
      log(" The Main URL is down", domain, domain, "Errors/errors");
//      throw new IOException("The main URL is Down! " +
//              "Please Report a Defect."
//      );
    }
  }



  /**
   * At Initial Set up. Appends any url that is in the pages_ignored.txt to pagesVisited Set.
   */
  protected void setPagesIgnored() {
    String[] pagesIgnoredStr;
    BufferedReader reader;
    try {
      Scanner scanner = new Scanner(new File(resourcesFolder + "pages_ignored.txt"));
      while (scanner.hasNextLine()) {
        pagesVisited.add(scanner.nextLine());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * This private helper method checks an url response status code. if the response code is invalid,
   * meaning connection was not established (0), or if the repsonse code is other than 200 the
   * method will return false. This method will return true only when the code is 200;
   *
   * @param url - the url string;
   * @return - true if response code is ok, false otherwise.
   */
  protected boolean statusCodeIsOK(String url, String parent) {
    int statusCode = 0;

    boolean statusConfirmed = false;
    if (pagesVisited.add(url)) {
      statusCode = getResponseCode(url, parent);
      if (statusCode == 0) {
        log(statusCode + "", url, parent, "PagesChecked/pages_checked");
        statusConfirmed = false;
      } else if (statusCode != 200) {
        log(statusCode + "", url, parent, "PagesChecked/pages_checked");
        statusConfirmed = false;
      } else {
        log(statusCode + "", url, parent, "PagesChecked/pages_checked");
        statusConfirmed = true;
      }
    }
    return statusConfirmed;
  }


  /**
   * This private helper method gets the status code of an url.
   *
   * @param url - a string that represents the address of an url.
   * @return - an int that represents the status code of a connection made to url.
   */
  protected int getResponseCode(String url, String parent) {
    Connection.Response response = null;
    int statusCode = 0;
    try {
      response = Jsoup.connect(url)
              .followRedirects(true)
              .timeout(60000)
              .execute();
      statusCode = response.statusCode();
      if (statusCode != 200) {
        log(statusCode + "", url, parent, "Errors/errors");
      }
    } catch (HttpStatusException e) {
      statusCode = e.getStatusCode();
      log(e.getStatusCode() + "  " + e.getMessage(), url, parent, "Errors/errors");
    } catch (UnsupportedMimeTypeException e) {
      log("Failed to get Response. Due to a runtime UnsupportedMimeTypeException.\n"
              + "                " + e.getMessage(), url, parent, "Errors/errors");
    } catch (IOException e) {
      log("Failed to get Response. Due to a runtime IOException.\n"
              + "                " + e.getMessage(), url, parent, "Errors/errors");
    } catch (IllegalArgumentException e) {
      log("Failed to get Response. Due to a runtime IlegalArgumentException.\n"
              + "                " + e.getMessage() + "\n", url, parent, "Errors/errors");
    }
    return statusCode;
  }


  /**
   * This private helper method determines if an url is a valid link.
   *
   * @param url - url string under scrutiny.
   * @return - true if url string passes a set of conditions, else returns false.
   */
  protected boolean isValidURL(String url) {
    return (url.contains(domain)
            && (url.contains("http://") || url.contains("https://"))
            && !url.contains(".pdf")
            && !url.contains(".jpg"))
            && !url.endsWith("#respond")
            && !url.contains("PlymouthRockAssuranceOpensNewCorporateHeadquarters");
  }


  /**
   * This private helper method logs any  Response status code of a url.
   *
   * @param errorCode - the HTTP Status code OTHER THAN 200.
   * @param url       - a string that represents the address of an url.
   * @param parent    - parent url.
   * @param doc       - the path of the text document recieving the log.
   * @param misc      - any trailing material that needs to be logged for each entry.
   */
  protected void log(String errorCode, String url, String parent, String doc, String... misc) {
    Date date = new Date();
    Calendar c = Calendar.getInstance();
    date = c.getTime();
    DateFormat scriptDateTime = new SimpleDateFormat("_yyyy.MM.dd");
    String d = scriptDateTime.format(date);

    boolean fileExist = new File(resourcesFolder + doc + d + ".txt").exists();
    if (!fileExist) {
      String path = resourcesFolder+ doc + d + ".txt";
      File f = new File(path);
      f.getParentFile().mkdirs();
      try {
        f.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      Files.write(Paths.get(resourcesFolder + doc + d + ".txt"),
              ("Status Code:    " + errorCode
                      + System.lineSeparator()
                      + "URL:            " + url
                      + System.lineSeparator()
                      + "Parent:         " + parent
                      + System.lineSeparator()
                      + System.lineSeparator()
                      + System.lineSeparator()

              ).getBytes()
              , StandardOpenOption.APPEND);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * This private helper method gets the address of an link element.
   *
   * @param page - a link element.
   * @return - a formatted String that represents the url of a link element.
   */
  protected static String getUrl(Element page) {
    return page.attr("abs:href").replace("%20&", "&");
  }

}
