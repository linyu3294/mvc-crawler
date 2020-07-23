package controller;

import java.io.IOException;
import java.util.Iterator;

import model.soup.ISpider;
import view.IEmail;

public interface IController {
void run (ISpider spider) throws IOException;
String getDateStamp ();
String appendReportHeader (String emailStr);
String appendReportListURLs (String emailStr);
String appendReportPassFails (String emailStr);
String appendReportTail (String emailStr);
String createReport ();
void sendReportInEmail (IEmail email) throws IOException;
}
