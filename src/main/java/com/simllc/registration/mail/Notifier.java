package com.simllc.registration.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.MethodInvocationException;

import com.simllc.registration.model.User;
import com.sun.mail.smtp.SMTPTransport;

public class Notifier {

	public Notifier() {

	}

	public void sendMessage() {

		try {
			Properties props = System.getProperties();
			props.put("mail.smtps.host", "smtp.gmail.com");
			props.put("mail.smtps.auth", "true");
			Session session = Session.getInstance(props, null);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("change-this-name@example.com"));
			;
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
					"sample-recipient@example.com", false));
			msg.setSubject("Tested notifier at: " + System.currentTimeMillis());
			msg.setText("This is a cool test...");
			msg.setHeader("X-Mailer", "my registration program");
			msg.setSentDate(new Date());
			SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
			t.connect("smtp.gmail.com", "change-this-name@example.com", "password-goes-here");
			t.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Response: " + t.getLastServerResponse());
			t.close();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (SendFailedException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	public void sendMessage(String recipient, String activationUrl) {

		try {
			Properties props = System.getProperties();
			props.put("mail.smtps.host", "smtp.gmail.com");
			props.put("mail.smtps.auth", "true");
			Session session = Session.getInstance(props, null);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("change-this-name@example.com"));
			;
			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(recipient, false));
			msg.setSubject("Tested notifier at: " + System.currentTimeMillis());
			msg.setText("This is a cool test...\n" + "Activate: "
					+ activationUrl);
			msg.setHeader("X-Mailer", "my registration program");
			msg.setSentDate(new Date());
			SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
			t.connect("smtp.gmail.com", "change-this-name@example.com", "password-goes-here");
			t.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Response: " + t.getLastServerResponse());
			t.close();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SendFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String sendActivationMessage(User user, String activationUrl) {
		String responseMsg = "";
		try {
			Properties props = System.getProperties();
			props.put("mail.smtps.host", "smtp.gmail.com");
			props.put("mail.smtps.auth", "true");
			Session session = Session.getInstance(props, null);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("change-this@yahoo.com"));
			;
			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail(), false));
			msg.setSubject("User account activation message: "
					+ System.currentTimeMillis());
			msg.setText("This is a cool test...\n" + "Activate: "
					+ activationUrl);
			msg.setHeader("X-Mailer", "registration-jdbcrealm's program");
			msg.setSentDate(new Date());
			SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
			t.connect("smtp.gmail.com", "change-this@example.com", "password-goes-here");
			t.sendMessage(msg, msg.getAllRecipients());
			responseMsg = t.getLastServerResponse();
			System.out.println("Response: " + t.getLastServerResponse());
			t.close();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			responseMsg = e.getMessage();
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			responseMsg = e.getMessage();
			e.printStackTrace();
		} catch (SendFailedException e) {
			responseMsg = e.getMessage();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			responseMsg = e.getMessage();
			e.printStackTrace();
		}
		return responseMsg;
	}

	/**
	 * Sends an Email message to the user to enable them to reset their
	 * password.
	 * 
	 * @param User
	 *            user
	 * @param String
	 *            resetUrl
	 * @return
	 */
	public String sendResetMessage(User user, String resetUrl) {
		String responseMsg = "";
		try {
			Properties props = System.getProperties();
			props.put("mail.smtps.host", "smtp.gmail.com");
			props.put("mail.smtps.auth", "true");
			Session session = Session.getInstance(props, null);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("pauley.chris@gmail.com"));
			;
			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail(), false));
			msg.setSubject("User account password reset message: "
					+ System.currentTimeMillis());
			msg.setText("Your system account password has been reset. \n"
					+ "Please click on the following link or paste it into your browser: "
					+ resetUrl);
			msg.setHeader("X-Mailer", "registration-jdbcrealm's program");
			msg.setSentDate(new Date());
			SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
			t.connect("smtp.gmail.com", "change-this-name@example.com", "password-goes-here");
			t.sendMessage(msg, msg.getAllRecipients());
			responseMsg = t.getLastServerResponse();
			System.out.println("Response: " + t.getLastServerResponse());
			t.close();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			responseMsg = e.getMessage();
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			responseMsg = e.getMessage();
			e.printStackTrace();
		} catch (SendFailedException e) {
			responseMsg = e.getMessage();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			responseMsg = e.getMessage();
			e.printStackTrace();
		}
		return responseMsg;
	}

	public String sendResetHTMLMessage(User user, String resetUrl) {
		String responseMsg = "";
		try {
			Properties props = System.getProperties();
			props.put("mail.smtps.host", "smtp.gmail.com");
			props.put("mail.smtps.auth", "true");
			Session session = Session.getInstance(props, null);

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("pauley.chris@gmail.com"));
			msg.setHeader("X-Mailer", "gpxdb's program");
			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail(), false));
			msg.setSubject("Your gpxdb account password has been reset");

			MimeMultipart multipart = new MimeMultipart("related");
			BodyPart messageBodyPart = new MimeBodyPart();
			String htmlText = "<H1>Hello</H1><img src=\"cid:image\">"
					+ "<p><a href='" + resetUrl + "'>Login Now</a></p>";
			messageBodyPart.setContent(htmlText, "text/html");
			multipart.addBodyPart(messageBodyPart);
     		msg.setContent(multipart);

			SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
			t.connect("smtp.gmail.com", "change-this@example.com", "password-goes-here");
			t.sendMessage(msg, msg.getAllRecipients());
			responseMsg = t.getLastServerResponse();
			System.out.println("Response: " + t.getLastServerResponse());
			t.close();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			responseMsg = e.getMessage();
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			responseMsg = e.getMessage();
			e.printStackTrace();
		} catch (SendFailedException e) {
			responseMsg = e.getMessage();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			responseMsg = e.getMessage();
			e.printStackTrace();
		}
		return responseMsg;
	}
	
	public String sendEmailVelocity(User user, String resetUrl) {
		Properties props = new Properties();
		props.setProperty("resource.loader", "class");
		props.setProperty("resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		props.setProperty("class.resource.loader.description","Velocity Classpath Resource Loader");
		props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init( props );

		VelocityContext context = new VelocityContext();

		context.put("name", new String(user.getUsername()));
		context.put("title", new String("Please activate your new account"));
		context.put("activation_url", new String(resetUrl));
		Template template = null;

		try {
			template = Velocity.getTemplate("thank_you_for_registering.html");
		} catch (ResourceNotFoundException rnfe) {
			System.out.println(" couldn't find the template");
		} catch (ParseErrorException pee) {
			// syntax error: problem parsing the template
		} catch (MethodInvocationException mie) {
			// something invoked in the template
			// threw an exception
		} catch (Exception e) {
		}

		StringWriter sw = new StringWriter();

		template.merge(context, sw);
		//System.out.println(sw.toString());
		
		
		String responseMsg = "";
		try {
			Properties mprops = System.getProperties();
			mprops.put("mail.smtps.host", "smtp.gmail.com");
			mprops.put("mail.smtps.auth", "true");
			Session session = Session.getInstance(props, null);

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("change-this@example.com"));
			msg.setHeader("X-Mailer", "gpxdb's program");
			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail(), false));
			msg.setSubject("Your activation link to example.com ");

			MimeMultipart multipart = new MimeMultipart("related");
			// text
//			BodyPart textBodyPart = new MimeBodyPart();
//			textBodyPart.setContent("Text message sTrIng...", "text/plain");
//			multipart.addBodyPart(textBodyPart);
			
			// html
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(sw.toString(), "text/html");
			multipart.addBodyPart(messageBodyPart);
	
            // image
			String fileName = "Lifebuoy.png";
	        ClassLoader classLoader = Thread.currentThread()
	                .getContextClassLoader();

	        DataSource ds = new URLDataSource(classLoader.getResource(fileName));
			BodyPart imagePart = new MimeBodyPart();
	        imagePart.setDataHandler(new DataHandler(ds));
	        imagePart.setHeader("Content-ID", "<life-buoy-img>");
			multipart.addBodyPart(imagePart);			

			// 
			msg.setContent(multipart);

			SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
			t.connect("smtp.gmail.com", "change-this@example.com", "password-goes-here");
			t.sendMessage(msg, msg.getAllRecipients());
			responseMsg = t.getLastServerResponse();
			System.out.println("Response: " + t.getLastServerResponse());
			t.close();
			
		} catch (AddressException e) {
			responseMsg = e.getMessage();
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			responseMsg = e.getMessage();
			e.printStackTrace();
		} catch (SendFailedException e) {
			responseMsg = e.getMessage();
			e.printStackTrace();
		} catch (MessagingException e) {
			responseMsg = e.getMessage();
			e.printStackTrace();
		}
		return responseMsg;
	}	

}
