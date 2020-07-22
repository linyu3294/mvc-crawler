package view.report;

import java.util.List;

public interface IEmail {
IEmail setSubject (String subject, String dateStamp) throws Exception;
IEmail setSender (String sender) throws Exception;
IEmail setReceiver (String receiver) throws Exception;
IEmail setListOfCCs (List<String> listOfCCs) throws Exception;
IEmail setEmailBody (String msg) throws Exception;
IEmail setAttachment (String attachmentPath, String newName) throws Exception;
void send() throws Exception;

}
