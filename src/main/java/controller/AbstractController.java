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

import model.soup.ISpider;
import view.IEmail;


abstract class AbstractController implements IController {
protected HashMap<Integer, Integer> statusCodeMap;
protected String resourcesFolderPath;
protected HashSet<String> baseUrls;
protected String dateStamp;
protected Integer totalChecked;


protected AbstractController (HashSet<String> baseUrls) {
   this.statusCodeMap = new HashMap<>();
   this.baseUrls = baseUrls;
   this.dateStamp = newDateStamp();
   this.totalChecked = 0;
}


protected String newDateStamp () {
   Date date = new Date();
   Calendar c = Calendar.getInstance();
   date = c.getTime();
   DateFormat scriptDateTime = new SimpleDateFormat("yyyy.MM.dd");
   return scriptDateTime.format(date);
}


@Override
public void run (ISpider spider) {
   this.resourcesFolderPath = spider.getResourcesFolderPath();
   try {
      for (String baseUrl : baseUrls) {
//         spider.crawl(baseUrl, "Main URL");
      }
   } catch (Exception e) {
      e.printStackTrace();
   }
}

@Override
public String appendReportHeader (String emailStr) {
   String [] str = {emailStr};
   str[0] = str[0] + "Good Morning,   \n\n";
   str[0] = str[0] + "Here is a Summary of Today's Results:\n\n";
   str[0] = str[0] + "List of Base URLs Checked:\n";
   return str [0];
}

@Override
public String appendReportListURLs (String emailStr) {
   Set set = statusCodeMap.entrySet();
   Iterator iter = set.iterator();
   String [] str = {emailStr};
   for (String temp : this.baseUrls) {
      str[0] = str[0] + temp + "\n";
   }
   return str [0];
}


@Override
public String appendReportPassFails (String emailStr) {
   Set set = statusCodeMap.entrySet();
   Iterator iter = set.iterator();
   String [] str = {emailStr};
   str[0] = str[0] + "\n\n   Total Number of Sites Checked: " + this.totalChecked + "\n";
   while (iter.hasNext()) {
      Map.Entry mentry = (Map.Entry) iter.next();
      str[0] = str[0]
         + "   Number of Sites returning a \"" + mentry.getKey() + ""
         + "\" Response Code:   " + mentry.getValue().toString() + "\n";
   }
   return str [0];
}

@Override
public String appendReportTail (String emailStr) {
   String [] str = {emailStr};
   str[0] = str[0] + "\n\nSites that return a non-\"200\" are documented in errors.txt. " +
      "\n\nThank you! \nYu Lin";
   return str [0];
}

@Override
public String createReport () {
   String str ="";
   str = appendReportHeader( str);
   str = appendReportListURLs(str);
   str = appendReportPassFails(str);
   str = appendReportTail(str);
   return str;
}

@Override
public void sendReportInEmail (IEmail email) throws IOException {
   try {
      email.send();
   } catch (Exception e) {
      e.printStackTrace();
   }
}
}
