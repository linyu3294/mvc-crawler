package model.screen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SoupReport {
  private HashMap<Integer, Integer> results;
  private int totalChecked;
  private String d;
  private String resourcesFolder;

  public SoupReport(String resourcesFolder) {
    this.resourcesFolder = resourcesFolder;
  }

  public String formatDate() {
    // formate Date
    Date date = new Date();
    Calendar c = Calendar.getInstance();
    date = c.getTime();
    DateFormat scriptDateTime = new SimpleDateFormat("yyyy.MM.dd");
    return scriptDateTime.format(date);
  }

  private void count(Integer responseCode) {
    totalChecked++;

    Integer attempt = results.get(responseCode);
    if (attempt != null) {
      results.put(responseCode, attempt + 1);
    } else {
      results.put(responseCode, 1);
    }
  }


  private void compileResult() throws IOException {
    results = new HashMap<>();
    d = formatDate();
    totalChecked = 0;
    BufferedReader br = new BufferedReader(
            new FileReader(
                    resourcesFolder + "PagesChecked/pages_checked_" + d + ".txt")
    );
    String line;
    while ((line = br.readLine()) != null) {
      if (line.contains("Status Code:    ")) {
        try {
          Integer responseCode;
          responseCode = Integer.valueOf(line.split("Status Code:    ")[1].trim());
          count(responseCode);
        } catch (Exception e) {
        }
      }
    }

  }

  private String resultsToStr(HashSet<String> baseUrls) throws IOException {
    compileResult();
    final String[] str = {"Good Morning,   \n\n"};
    str[0] = str[0] + "Here is a Summary of Today's Results:\n\n";
    str[0] = str[0] + "List of Base URLs Checked:\n";
    Set set = results.entrySet();
    Iterator iter = set.iterator();


    for (String temp : baseUrls) {
      str[0] = str[0] + temp + "\n";
    }
    str[0] = str[0] + "\n\n   Total Number of Sites Checked: " + totalChecked + "\n";
    while (iter.hasNext()) {
      Map.Entry mentry = (Map.Entry) iter.next();
      str[0] = str[0]
              +
              "   Number of Sites returning a \"" + mentry.getKey() + ""
              + "\" Response Code:   " + mentry.getValue().toString() + "\n";
    }
    str[0] = str[0] + "\n\nSites that return a non-\"200\" are documented in errors.txt. " +
            "\n\nThank you! \nYu Lin";
    return str[0];
  }

  public void sendEmail(HashSet<String> baseUrls) {
    d = formatDate();

    // Recipient's email ID needs to be mentioned.
    String to = "ylin@Plymouthrock.com";
    // CC Recipient's email ID needs to be mentioned.
    String cc1 = "cuhlar@Plymouthrock.com";
    String cc2 = "rstrohmenger@plymouthrock.com";
    // Sender's email ID needs to be mentioned
    String from = "Z@Plymouthrock.com";
    // Assuming you are sending email from localhost
    String host = "PRCRELAY.PRCINS.NET";
    // Get system properties
    Properties properties = System.getProperties();
    // Setup mail server
    properties.setProperty("mail.smtp.host", host);
    // Get the default Session object.
    Session session = Session.getDefaultInstance(properties);

    try {
      // Create a default MimeMessage object.
      MimeMessage message = new MimeMessage(session);
      // Set From: header field of the header.
      message.setFrom(new InternetAddress(from));
      // Set To: header field of the header.
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc1));
      message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc2));
      // Set Subject: header field
      message.setSubject("Public Websites Regression Results" + " (" + d + ")");
      // Create the message part
      BodyPart messageBodyPart0 = new MimeBodyPart();
      // Now set the actual message
      messageBodyPart0.setText(resultsToStr(baseUrls));
      // Create a multipart message
      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart0);

      boolean fileExist = new File(resourcesFolder + "Errors/errors_" + d + ".txt").exists();
      if (fileExist) {
        // Attach Errors text Report if there is any
        BodyPart messageBodyPart1 = new MimeBodyPart();
        messageBodyPart1 = new MimeBodyPart();
        String filename = resourcesFolder + "Errors/errors_" + d + ".txt";
        DataSource source = new FileDataSource(filename);
        messageBodyPart1.setDataHandler(new DataHandler(source));
        messageBodyPart1.setFileName("Errors" + "_" + d + ".txt");
        multipart.addBodyPart(messageBodyPart1);
      }


      // Attach all checked urls text report.
      BodyPart messageBodyPart2 = new MimeBodyPart();
      String filename2 = resourcesFolder + "PagesChecked/pages_checked_" + d + ".txt";
      DataSource source2 = new FileDataSource(filename2);
      messageBodyPart2.setDataHandler(new DataHandler(source2));
      messageBodyPart2.setFileName("Public Websites Regression Results" + "_" + d + ".txt");
      multipart.addBodyPart(messageBodyPart2);


      // Attach all checked urls text report.
      BodyPart messageBodyPart3 = new MimeBodyPart();
      String filename3 = resourcesFolder + "pages_ignored.txt";
      DataSource source3 = new FileDataSource(filename3);
      messageBodyPart3.setDataHandler(new DataHandler(source3));
      messageBodyPart3.setFileName("Pages Ignored" + "_" + d + ".txt");
      multipart.addBodyPart(messageBodyPart3);

      // Send the complete message parts
      message.setContent(multipart);


      // Send message
      Transport.send(message);
    } catch (Exception mex) {
      mex.printStackTrace();
    }
  }


}



