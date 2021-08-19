package webshop;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Session;
import javax.mail.Transport;

public class JavaMail {

    private static Message prepareMessage(String email, String name, String topic, String message)
            throws MessagingException, UnsupportedEncodingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("bierbestellen@gmail.com", "2M6FJ0i3a4Am");
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("bierbestellen@gmail.com", "Bielefelder Unikat"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email, name));
        msg.setSubject(topic);
        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(message);
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        return msg;
    }

    // recipient format: "Real Name <email@addre.ss>"
    public static void sendMessage(String email, String name, String topic, String message) {
        try {
            Message msg = prepareMessage(email, name, topic, message);
            Transport.send(msg);
            System.out.println("e-mail sent to " + name + " <" + email + ">");
        } catch (Exception e) {
            System.out.println("ERROR when trying to send e-mail to " + name + " <" + email + ">");
            e.printStackTrace();
        }
    }

    private JavaMail() {
    } // just to make VS Code shut up

}