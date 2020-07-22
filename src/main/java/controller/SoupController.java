package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.soup.ISpider;
import model.soup.SoupSpider;
import view.report.Email;

/**
 * This is a class that implements interface IController. This controller class runs with a
 * SoupSpider model and uses the Email View. The controller keeps model and view seperate and
 * manages data flow between them. To give context, SoupSpider model employs JSoup's Html parsing
 * technology and checks the response code of a url at each iteration of the recursive crawl.
 */
public class SoupController implements IController {
private HashMap<Integer, Integer> statusCodeMap;
private Integer totalChecked;
private String resourcesFolder;
private HashSet<String> baseUrls;

public SoupController (HashSet<String> baseUrls, String resourcesFolder) {
   this.statusCodeMap = new HashMap<>();
   this.totalChecked = 0;
   this.baseUrls = baseUrls;
   this.resourcesFolder = resourcesFolder;
}


public void run () {
   try {
      for (String baseUrl : baseUrls) {
         ISpider spider = new SoupSpider(this.resourcesFolder);
//        spider.crawl(baseUrl, "Main URL");
      }
   } catch (Exception e) {
      e.printStackTrace();
   }
}


private String newDateStamp () {
   Date date = new Date();
   Calendar c = Calendar.getInstance();
   date = c.getTime();
   DateFormat scriptDateTime = new SimpleDateFormat("yyyy.MM.dd");
   return scriptDateTime.format(date);
}

private

private BufferedReader openCrawlerResultLog (String pathToCrawlerResultLog) throws IOException {
   return new BufferedReader(
      new FileReader(pathToCrawlerResultLog));
}


private void mapAllStatusCodes (BufferedReader logOfStatusCodes) throws IOException {
   String line;
   while ((line = logOfStatusCodes.readLine()) != null) {
      if (line.contains("Status Code:    ")) {
         try {
            Integer statusCode = Integer.valueOf(
               line.split("Status Code:    ")[1].trim());
            evalResponseAndWriteToMap(statusCode);
         } catch (Exception e) {
         }
      }
   }
}


private void evalResponseAndWriteToMap (Integer statusCode) {
   this.totalChecked++;
   Integer attempt = statusCodeMap.get(statusCode);
   if (attempt != null) {
      statusCodeMap.put(statusCode, attempt + 1);
   } else {
      statusCodeMap.put(statusCode, 1);
   }
}

private String[] composeEmailBodyHeader (String[] str) {
   str[0] = str[0] + "Good Morning,   \n\n";
   str[0] = str[0] + "Here is a Summary of Today's Results:\n\n";
   str[0] = str[0] + "List of Base URLs Checked:\n";
   return str;
}

private String[] composeEmailBodyListURLs (String[] str, Iterator iter) {
   for (String temp : this.baseUrls) {
      str[0] = str[0] + temp + "\n";
   }
   return str;
}

private String[] composeEmailBodyPassAndFails (String[] str, Iterator iter) {
   str[0] = str[0] + "\n\n   Total Number of Sites Checked: " + this.totalChecked + "\n";
   while (iter.hasNext()) {
      Map.Entry mentry = (Map.Entry) iter.next();
      str[0] = str[0]
         + "   Number of Sites returning a \"" + mentry.getKey() + ""
         + "\" Response Code:   " + mentry.getValue().toString() + "\n";
   }
   return str;
}

private String[] composeEmailBodyTail (String[] str) {
   str[0] = str[0] + "\n\nSites that return a non-\"200\" are documented in errors.txt. " +
      "\n\nThank you! \nYu Lin";
   return str;
}

private String composeEmailBody () {
   Set set = statusCodeMap.entrySet();
   Iterator iter = set.iterator();
   String[] str = {""};
   str = composeEmailBodyHeader(str);
   str = composeEmailBodyListURLs(str, iter);
   str = composeEmailBodyPassAndFails(str, iter);
   str = composeEmailBodyTail(str);
   return str[0];
}




public void report () throws IOException {
   BufferedReader crawlerResultLog = openCrawlerResultLog(
      ""
         + resourcesFolder
         + "PagesChecked/pages_checked_"
         + newDateStamp()
         + ".txt");
   mapAllStatusCodes (crawlerResultLog);

   

   Email email = new Email("PRCRELAY.PRCINS.NET");
   List<String> listOfCCs = new ArrayList<String>();
   listOfCCs.add("ylin@plymouthrock.com");
   listOfCCs.add("ylin@plymouthrock.com");
   try {
      email
         .setSender("Z@plymouthrock.com")
         .setReceiver("ylin@plymouthrock.com")
         .setListOfCCs(listOfCCs)
         .setSubject("Test", newDateStamp())
         .setEmailBody(composeEmailBody())
         .setAttachment("src/resources/Errors/errors_2019.07.08.txt", "Test 3.txt")
         .send();

   } catch (Exception e) {
      e.printStackTrace();
   }
}

}
