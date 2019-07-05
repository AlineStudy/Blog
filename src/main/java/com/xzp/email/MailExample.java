package com.xzp.email;

/**
 * Created by wly on 2018/3/7.
 */

public class MailExample {

    public static void main (String args[]) throws Exception {
        String email = "13347242652@163.com";
        String validateCode = "xzp185646";
        SendEmail.sendEmailMessage(email,validateCode);

    }
}
