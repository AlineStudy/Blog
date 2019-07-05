package com.xzp.email;


import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail {

    private final static Logger log = Logger.getLogger(SendEmail.class);


    public static void sendEmailMessage(String email,String validateCode){

        try {
            //发件人使用发邮件的电子信箱服务器
            String host = "smtp.163.com";
            //发邮件的出发地（发件人的信箱）
            String from = "13347242652@163.com";
            //发邮件的目的地（收件人信箱）
            String to = email;

            Properties properties  = System.getProperties();
            // Setup mail server
            properties.put("mail.smtp.host", host);

            //Get session   这样才能通过验证
            properties.put("mail.smtp.auth",true);

            MyAuthenticator myAuth = new MyAuthenticator(from,"xzp185646");
            //创建会话
            Session session = Session.getDefaultInstance(properties,myAuth);


            MimeMessage mimeMessage = new MimeMessage(session);
            // Set the from address   ，发送地址
            mimeMessage.setFrom(new InternetAddress(from));
            // Set the to address ，接收地址
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            // Set the subject
            mimeMessage.setSubject("菜鸟博主激活邮件通知:");
            //Set the content
            mimeMessage.setContent( "<a href=\"http://localhost:8080/activecode?email="+email+"&validateCode="+validateCode+"\" target=\"_blank\">请于24小时内点击激活</a>","text/html;charset=gb2312");
            mimeMessage.saveChanges();

            Transport.send(mimeMessage);
            log.info( "send validateCode to " + email );
        } catch (MessagingException e) {
            log.info( "Send Email Exception:"+e.getMessage() );
            e.printStackTrace();
        }
    }
}
