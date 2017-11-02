package com.yunip.utils.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

class SmtpAuthenticator extends Authenticator {

    private String username   = null;

    private String userpasswd = null;

    public SmtpAuthenticator() {}

    public SmtpAuthenticator(String username, String userpasswd) {
        this.username = username;
        this.userpasswd = userpasswd;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.userpasswd = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, userpasswd);
    }
}
