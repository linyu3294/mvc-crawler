package view;

import java.util.List;

/**
 * A interface for the Email View.
 */
public interface IEmail {

/**
 * Sets the email subject.
 * @param subject - A String.
 * @return - self.
 * @throws Exception
 */
IEmail setSubject (String subject) throws Exception;

/**
 * Sets the sender of the email.
 * @param sender - email of the sender.
 * @return - self.
 * @throws Exception
 */
IEmail setSender (String sender) throws Exception;

/**
 * Sets the reciever of the email.
 * @param receiver - email of the reciever.
 * @return - self.
 * @throws Exception
 */
IEmail setReceiver (String receiver) throws Exception;

/**
 * Sets a list of ccd emails.
 * @param listOfCCs - A List<String> that contains ccd emails.
 * @return - self.
 * @throws Exception
 */
IEmail setListOfCCs (List<String> listOfCCs) throws Exception;

/**
 * Sets the message body of the email.
 * @param msg - message body.
 * @return - self.
 * @throws Exception
 */
IEmail setEmailBody (String msg) throws Exception;

/**
 * Appends an attachment.
 * @param attachmentPath - the path of an attachment.
 * @param newName - a new name given to the attachment.
 * @return - self.
 * @throws Exception
 */
IEmail setAttachment (String attachmentPath, String newName) throws Exception;

/**
 * Sends off the email once it is all set.
 * @throws Exception
 */
void send() throws Exception;

}
