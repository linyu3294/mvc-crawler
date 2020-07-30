package controller;

import java.io.IOException;

import model.soup.ISpider;
import view.IEmail;

public interface IController {
/**
 * This method is responsible for using one of the various ISpider models to traverse through a list
 * urls and their children to get status of the pages.
 *
 * @param spider - A chosen ISpider model used for crawling.
 */
void run (ISpider spider) throws IOException;

/**
 * A getter to get today's date.
 *
 * @return - Today's date in the form of a string.
 */
String getDateStamp ();

/**
 * Appends a header to a body of email.
 *
 * @param emailStr - body of an email represented by as string.
 *
 * @return - returns the appended result of a email body.
 */
String appendReportHeader (String emailStr);

/**
 * Appends a list of crawled urls to a body of email.
 *
 * @param emailStr - body of an email represented as a String.
 *
 * @return - returns the appended result of a email body.
 */
String appendReportListURLs (String emailStr);

/**
 * Appends the number of passes and fails to a body of email.
 *
 * @param emailStr - body of an email represented as a String.
 *
 * @return - returns the appended result of a email body.
 */
String appendReportPassFails (String emailStr);

/**
 * Appends the tail signature to a body of email.
 *
 * @param emailStr - body of an email represented as a String.
 *
 * @return - returns the appended result of a email body.
 */
String appendReportTail (String emailStr);

/**
 * Assembles a report using methods in this interface. Assembly will call the concrete
 * implementations of methods this Interface in the order as follows: 1) appendReportHeader, 2)
 * appendReportListURLs, 3) appendReportPassFails, 4) appendReportTail(str). All parts of the
 * assembly can be overriden to create flexible email generation, specifically tailoring each
 * Ispider Model.
 */
String createReport ();

/**
 * @param email - An Email object.
 *
 * @exception Exception - when an email fails to send.
 */
void sendReportInEmail (IEmail email) throws Exception;
}
