package app.tools;

import java.util.List;

public class EmailRequest {
    public String Title;
    public String Subject;
    public String HTMLText;
    public String RecipientName;
    public String RecipientEmail;
    public List<Recipient> CC;
    public List<Recipient> CCO;
    public List<Attachment> Attached;
    public EmailConfig Config;

    public static class Recipient {
        public String Email;
        public String Name;
    }

    public static class Attachment {
        public String Path;
    }

    public static class EmailConfig {
        public String User;
        public String Password;
        public boolean Autentication;
        public boolean Security;
        public int Port;
        public String Email;
        public String Smtp;
    }
}

