package com.williamngo.interfaces;

import com.williamngo.JagEmail.JagEmail;
import java.util.Optional;

/**
 * Interface with send and receive methods
 * 
 * @author William Ngo
 */
public interface Mailer {
    JagEmail sendEmail(Optional<String> listOfTo,Optional<String> listOfCc, Optional<String> listOfBcc, String subject, String message,
            String html, String embedded, String attachment);
    JagEmail[] receiveEmail();
}
