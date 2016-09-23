/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.firstproject;

import java.util.Optional;

/**
 *
 * @author William Ngo
 */
public interface IMailer {
    JagEmail sendEmail(Optional<String> listOfTo,Optional<String> listOfCc, Optional<String> listOfBcc, String subject, String message,
            String html, String embedded, String attachment);
    //JagEmail sendEmail (JagEmail e);
    JagEmail[] receiveEmail();
    void setConfigBean(ConfigBean cb);
    ConfigBean getConfigBean();
}
