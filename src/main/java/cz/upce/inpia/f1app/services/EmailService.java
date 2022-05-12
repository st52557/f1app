package cz.upce.inpia.f1app.services;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {

    public static void sendMail(String emailTo, String code) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("semoradskola@gmail.com", "85321749");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("semoradskola@gmail.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
        msg.setSubject("Recovery code");
        msg.setContent("Use code to change password: " + code, "text/html");

        msg.setSentDate(new Date());

        Transport.send(msg);

    }
}








