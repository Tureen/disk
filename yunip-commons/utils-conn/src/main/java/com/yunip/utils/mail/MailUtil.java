/*
 * 描述：Email发送工具类，以UTF-8编码格式发送邮件，支持发送附件
 * 创建人：junbin.zhou
 * 创建时间：2012-8-13
 */
package com.yunip.utils.mail;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * Email发送工具类，以UTF-8编码格式发送邮件，支持发送附件
 */
public class MailUtil {
    /** 邮件服务器地址 **/
    private String smtpHost = "";
    /** 邮件服务器端口，默认端口25 **/
    @SuppressWarnings("unused")
    private int smtpPort = 25;
    /** 邮件接收人Email地址 **/
    private String mailTo = "";
    /** 邮件发送人Email地址 **/
    private String mailFrom = "";
    /** 邮件发送人名称 **/
    private String mailFromName = "";
    /** 邮件发送账号 **/
    private String userName = "";
    /** 邮件发送账号密码 **/
    private String passWord = "";
    /** 邮件主题 **/
    private String subject = "";

    /**
     * 构造函数
     */
    public MailUtil() {
    }

    /**
     * 构造函数
     */
    public MailUtil(String host, String to, String from, String mailFromName, String subject,
            String userName, String passWord) {
        this.iniMail(host, to, from, mailFromName, subject, userName, passWord);
    }

    /**
     * 设置eMail发送信息.
     * 
     * @param host
     *            邮件服务器IP
     * @param to
     *            收件人eMail
     * @param from
     *            发件人eMail
     * @param userName
     *            发信帐号
     * @param passWord
     *            发信密码
     */
    public void iniMail(String host, String to, String from, String subject,
            String userName, String passWord) {
        this.smtpHost = host;
        this.mailTo = to;
        this.mailFrom = from;
        this.mailFromName = userName;
        this.userName = userName;
        this.passWord = passWord;
        this.subject = subject;
    }

    public void iniMail(String host, String to, String from,
            String mailFromName, String subject, String userName,
            String passWord) {
        this.smtpHost = host;
        this.mailTo = to;
        this.mailFrom = from;
        this.mailFromName = mailFromName;
        this.userName = userName;
        this.passWord = passWord;
        this.subject = subject;
    }

    /**
     * 以 UTF-8 编码格式发送邮件
     * @param mailBody
     * @param attachments
     * @param hasAuth
     * @param hasContent
     * @throws Exception  
     * void 
     * @exception
     */
    public void sendHtmlMail(String mailBody, Collection<String> attachments,
            boolean hasAuth, boolean hasContent) throws Exception {
        Properties prop = System.getProperties();
        Session session = null;
        prop.put("mail.smtp.host", smtpHost);
        // 如果需要认证
        if (hasAuth) {
            prop.put("mail.smtp.auth", "true");
            SmtpAuthenticator authenticator = new SmtpAuthenticator(userName,
                    passWord);
            session = Session.getInstance(prop, authenticator);
        } else {
            session = Session.getDefaultInstance(prop, null);
        }
        session.setDebug(false);
        session.setPasswordAuthentication(new URLName(smtpHost),
                new PasswordAuthentication(userName, passWord));

        // 准备工作已完成，下面加入邮件内容
        MimeMessage message = null;
        if (hasContent) {
            message = new MimeMessage(session, new ByteArrayInputStream(mailBody.getBytes()));
        } else {
            message = new MimeMessage(session);
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart mbp = new MimeBodyPart();

            mbp.setContent(mailBody, "text/html;charset=UTF-8");
            multipart.addBodyPart(mbp);

            // 添加附件图片pic1
            /*
            Iterator itFile = attachments.iterator();
            int index = 0;
            while (itFile.hasNext()) {
                index++;
                String filename = (String) itFile.next();
                try {
                    BodyPart messagePicBodyPart = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(filename);
                    messagePicBodyPart.setDataHandler(new DataHandler(fds));
                    String picName = "pic" + index;
                    messagePicBodyPart.setHeader("Content-ID", picName);
                    multipart.addBodyPart(messagePicBodyPart);
                } catch (Exception e1) {
                }
            }
            */

            // 发送
            message.setContent(multipart);

            if (mailFromName == null || "".equals(mailFromName)) {
                message.setFrom(new InternetAddress(mailFrom));
            } else {
                message.setFrom(new InternetAddress(mailFrom, mailFromName));
            }
            message.addRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mailTo, false));
            message.addHeader("X-Priority", "1");
            message.setSubject(subject, "UTF-8");
        }
        Transport.send(message);
    }
    
    /**
     * 以 UTF-8 编码格式发送邮件(QQ)
     * @param mailBody
     * @param attachments
     * @param hasAuth
     * @param hasContent
     * @throws Exception  
     * void 
     * @exception
     */
    public void sendHtmlMailByQQ(String mailBody, Collection<String> attachments,
            boolean hasAuth, boolean hasContent) throws Exception {
        Properties prop = System.getProperties();
        Session session = null;
        prop.put("mail.smtp.host", smtpHost);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.port", "465");  
        prop.put("mail.smtp.socketFactory.port", "465");
        // 如果需要认证
        if (hasAuth) {
            prop.put("mail.smtp.auth", "true");
            SmtpAuthenticator authenticator = new SmtpAuthenticator(userName,
                    passWord);
            session = Session.getInstance(prop, authenticator);
        } else {
            session = Session.getDefaultInstance(prop, null);
        }
        session.setDebug(false);
        session.setPasswordAuthentication(new URLName(smtpHost),
                new PasswordAuthentication(userName, passWord));
        
        // 准备工作已完成，下面加入邮件内容
        MimeMessage message = null;
        if (hasContent) {
            message = new MimeMessage(session, new ByteArrayInputStream(mailBody.getBytes()));
        } else {
            message = new MimeMessage(session);
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart mbp = new MimeBodyPart();
            
            mbp.setContent(mailBody, "text/html;charset=UTF-8");
            multipart.addBodyPart(mbp);
            
            // 添加附件图片pic1
            /*
            Iterator itFile = attachments.iterator();
            int index = 0;
            while (itFile.hasNext()) {
                index++;
                String filename = (String) itFile.next();
                try {
                    BodyPart messagePicBodyPart = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(filename);
                    messagePicBodyPart.setDataHandler(new DataHandler(fds));
                    String picName = "pic" + index;
                    messagePicBodyPart.setHeader("Content-ID", picName);
                    multipart.addBodyPart(messagePicBodyPart);
                } catch (Exception e1) {
                }
            }
             */
            
            // 发送
            message.setContent(multipart);
            
            if (mailFromName == null || "".equals(mailFromName)) {
                message.setFrom(new InternetAddress(mailFrom));
            } else {
                message.setFrom(new InternetAddress(mailFrom, mailFromName));
            }
            message.addRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mailTo, false));
            message.addHeader("X-Priority", "1");
            message.setSubject(subject, "UTF-8");
        }
        Transport.send(message);
    }
}
