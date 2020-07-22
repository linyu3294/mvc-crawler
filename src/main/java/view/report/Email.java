package view.report;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Email implements IEmail{
private Session session;
private Message message;
private Multipart multipart;


public Email (String host){
   Properties properties = System.getProperties();
   properties.setProperty("mail.smtp.host", host);
   this.session = Session.getDefaultInstance(properties);
   this.message = new MimeMessage(this.session);
   this.multipart = new MimeMultipart();
}


public Email setSubject (String subject, String dateStamp)  throws MessagingException{
   message.setSubject(subject + dateStamp);
   return this;
}

public Email setSender (String sender)  throws MessagingException {
   this.message.setFrom(new InternetAddress(sender));
   return this;
}

public Email setReceiver (String receiver) throws MessagingException {
   this.message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
   return this;
}

public Email setListOfCCs (List <String> listOfCCs) throws MessagingException {
   Iterator<String> ccIterator = listOfCCs.iterator();
   while (ccIterator.hasNext()) {
      message.addRecipient(
         Message.RecipientType.CC,
         new InternetAddress(ccIterator.next()
            .trim()));
   }
   return this;
}

public Email setEmailBody (String msg) throws MessagingException {
   BodyPart msgPart = new MimeBodyPart();
   msgPart.setText(msg);
   multipart.addBodyPart(msgPart);
   message.setContent(multipart);
   return this;
}

public Email setAttachment (String attachmentPath, String newName) throws MessagingException {
   //Add Attachment to Multi-Body Part
   BodyPart attachement = new MimeBodyPart();
   String filename1 = attachmentPath;
   DataSource source = new FileDataSource(attachmentPath);
   attachement.setDataHandler(new DataHandler(source));
   attachement.setFileName(newName);
   multipart.addBodyPart(attachement);
   message.setContent(multipart);
   return this;
}


public void send() throws MessagingException {
   Transport.send(message);
}

}
