package view;

import java.util.List;

import controller.IController;
import controller.QueueController;
import controller.SoupController;

/**
 * Creates and configures a email.
 * Used in Main.
 */
public class EmailFactory {

public static IEmail make (IController controller,
                           List<String> listOfCCs,
                           String dateStamp,
                           String resourcesFolderPath,
                           String report) throws Exception {

   String attachementFileType = "";
   if (controller instanceof QueueController) {
      attachementFileType = ".csv";
   } else if (controller instanceof SoupController) {
      attachementFileType = ".txt";
   }

   IEmail email = new Email("PRCRELAY.PRCINS.NET");


      email = email
         .setSender("Z@plymouthrock.com")
         .setReceiver("ylin@plymouthrock.com")
         .setListOfCCs(listOfCCs)
         .setSubject("Public Website Regression Results " + dateStamp)
         .setAttachment(
            resourcesFolderPath
               + "/errors/errors_"
               + dateStamp
               + attachementFileType,
            "errors_" + dateStamp + attachementFileType)
         .setAttachment(
            resourcesFolderPath
               + "/pagesChecked/pages_checked_"
               + dateStamp
               + attachementFileType,
            "Public Websites Regression Results_" + dateStamp + attachementFileType)
         .setAttachment(
            resourcesFolderPath
               + "/pages_ignored.txt",
            "pages_ignored.txt")
         .setEmailBody(report);

   return email;
}

}
