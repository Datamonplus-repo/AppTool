package app.tools;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(EmailRequest request) throws Exception {

        Properties props = new Properties();
        props.put("mail.smtp.auth", String.valueOf(request.Config.Autentication));
        props.put("mail.smtp.starttls.enable", String.valueOf(request.Config.Security));
        props.put("mail.smtp.host", request.Config.Smtp);
        props.put("mail.smtp.port", String.valueOf(request.Config.Port));


        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(request.Config.User, request.Config.Password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(request.Config.Email));

        message.setRecipient(
                Message.RecipientType.TO,
                new InternetAddress(request.RecipientEmail, request.RecipientName)
        );

        if (request.CC != null) {
            for (EmailRequest.Recipient cc : request.CC) {
                if (cc.Email != null && !cc.Email.isEmpty()) {
                    message.addRecipient(
                            Message.RecipientType.CC,
                            new InternetAddress(cc.Email, cc.Name)
                    );
                }
            }
        }

        if (request.CCO != null) {
            for (EmailRequest.Recipient cco : request.CCO) {
                if (cco.Email != null && !cco.Email.isEmpty()) {
                    message.addRecipient(
                            Message.RecipientType.BCC,
                            new InternetAddress(cco.Email, cco.Name)
                    );
                }
            }
        }

        message.setSubject(request.Subject != null && !request.Subject.isEmpty() ? request.Subject : request.Title);

        Multipart multipart = new MimeMultipart();

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(request.HTMLText != null ? request.HTMLText : "", "text/html; charset=utf-8");
        multipart.addBodyPart(htmlPart);

        if (request.Attached != null) {
            for (EmailRequest.Attachment att : request.Attached) {
                if (att.Path != null && !att.Path.isEmpty()) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    attachPart.attachFile(new File(att.Path));
                    multipart.addBodyPart(attachPart);
                }
            }
        }

        message.setContent(multipart);

        Transport.send(message);
        System.out.println("E-mail enviado com sucesso!");
    }
}
