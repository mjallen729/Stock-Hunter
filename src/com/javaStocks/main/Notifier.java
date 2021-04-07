package com.javaStocks.main;
import com.sun.mail.smtp.SMTPTransport;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class Notifier { //@DEFINE: this class creates Notifier which can be used via sendMessage to send an email with specified content

    //@NOTE: Depreciated since Java 11+
  
	// for example, smtp.mailgun.org
    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String USERNAME = //intentionally hidden
    		"linesofsymmetrycontact@gmail.com";
    private static final String PASSWORD = //intentionally hidden
    		"mattman12";

    private static final String EMAIL_FROM = "StockEZ";
	
	public void sendMessage(String recipient, String subj,String message) {
		
		String EMAIL_TO = recipient;
	    String EMAIL_SUBJECT = subj;
	    String EMAIL_TEXT = message;
		
        Properties prop = System.getProperties();
        prop.put("mail.smtp.user", USERNAME);
        prop.put("mail.smtp.host", SMTP_SERVER);
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.starttls.enable","true");
        prop.put("mail.smtp.debug", "true");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        try {
		
			// from
            msg.setFrom(new InternetAddress(EMAIL_FROM));

			// to 
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO, false));

			// subject
            msg.setSubject(EMAIL_SUBJECT);
			
			// content 
            msg.setText(EMAIL_TEXT);
			
            msg.setSentDate(new Date());

			// Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
			
			// connect
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);
			
			// send
            t.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Response: " + t.getLastServerResponse());

            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        
	}
	
}
