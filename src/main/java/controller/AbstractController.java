package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import model.ISpider;
import view.IEmail;

/**
 * An abstract class that implements IController. All methods in IController are first implemented
 * here and can be overiden later in concrete Controllers.
 */
abstract class AbstractController implements IController {
protected HashMap<Integer, Integer> statusCodeMap;
protected String resourcesFolderPath;
protected HashSet<String> baseUrls;
protected String dateStamp;
protected Integer totalChecked;


/**
 * A constructor for AbstractController. Need to be instantiated in concrete classes.
 *
 * @param baseUrls - A set of base urls that will passed to and used in model for crawling.
 */
protected AbstractController (HashSet<String> baseUrls) {
   this.statusCodeMap = new HashMap<>();
   this.baseUrls = baseUrls;
   this.dateStamp = newDateStamp();
   this.totalChecked = 0;
}


/**
 * A getter to get today's date.
 *
 * @return - Today's date in the form of a string.
 */
@Override
public String getDateStamp () {
   return this.dateStamp;
}


/**
 * This method is responsible for using one of the various ISpider models to traverse through a list
 * urls and their children to get status of the pages.
 *
 * @param spider - A chosen ISpider model used for crawling.
 */
@Override
public void run (ISpider spider) throws IOException {
   this.resourcesFolderPath = spider.getResourcesFolderPath();
   try {
      for (String baseUrl : baseUrls) {
         spider.crawl(baseUrl, "Main URL");
      }
   } catch (Exception e) {
      e.printStackTrace();
   }
}


/**
 * Appends a header to a body of email.
 *
 * @param emailStr - body of an email represented by as string.
 *
 * @return - returns the appended result of a email body.
 */
@Override
public String appendReportHeader (String emailStr) {
   return emailStr
      + "Good Morning,\n\n"
      + "Here is a Summary of Today's Results:\n\n"
      + "List of Base URLs Checked:\n";
}


/**
 * Appends a list of crawled urls to a body of email.
 *
 * @param emailStr - body of an email represented as a String.
 *
 * @return - returns the appended result of a email body.
 */
@Override
public String appendReportListURLs (String emailStr) {
   Set set = statusCodeMap.entrySet();
   Iterator iter = set.iterator();
   for (String temp : this.baseUrls) {
      emailStr = emailStr + temp + "\n";
   }
   return emailStr;
}


/**
 * Appends the number of passes and fails to a body of email.
 *
 * @param emailStr - body of an email represented as a String.
 *
 * @return - returns the appended result of a email body.
 */
@Override
public String appendReportPassFails (String emailStr) {
   Set set = statusCodeMap.entrySet();
   Iterator iter = set.iterator();
   emailStr = emailStr + "\n\n   Total Number of Sites Checked: " + this.totalChecked + "\n";
   while (iter.hasNext()) {
      Map.Entry mentry = (Map.Entry) iter.next();
      emailStr = emailStr
         + "   Number of Sites returning a \"" + mentry.getKey()
         + "\" Response Code:   " + mentry.getValue().toString() + "\n";
   }
   return emailStr;
}


@Override
public String appendReportTail (String emailStr) {
   return emailStr
      + "\n\nSites that return a non-\"200\" are documented in errors.txt. " +
      "\n\nThank you! \nYu Lin";
}


/**
 * Assembles a report using methods in this interface. Assembly will call the concrete
 * implementations of methods this Interface in the order as follows: 1) appendReportHeader, 2)
 * appendReportListURLs, 3) appendReportPassFails, 4) appendReportTail(str). All parts of the
 * assembly can be overriden to create flexible email generation, specifically tailoring each
 * Ispider Model.
 */
@Override
public String createReport () {
   String str = "";
   str = appendReportHeader(str);
   str = appendReportListURLs(str);
   str = appendReportPassFails(str);
   str = appendReportTail(str);
   return str;
}


/**
 * @param email - An Email object.
 *
 * @exception IOException - when an email fails to send.
 */
@Override
public void sendReportInEmail (IEmail email) throws Exception {
      email.send();
}


protected void evalResponseAndWriteToMap (Integer statusCode) {
   this.totalChecked++;
   Integer attempt = this.statusCodeMap.get(statusCode);
   if (attempt != null) {
      statusCodeMap.put(statusCode, attempt + 1);
   } else {
      statusCodeMap.put(statusCode, 1);
   }
}


protected String newDateStamp () {
   Date date = new Date();
   Calendar c = Calendar.getInstance();
   date = c.getTime();
   DateFormat scriptDateTime = new SimpleDateFormat("yyyy.MM.dd");
   return scriptDateTime.format(date);
}
}
