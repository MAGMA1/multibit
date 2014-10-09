/**
 * Copyright 2011 multibit.org
 *
 * Licensed under the MIT license (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://opensource.org/licenses/mit-license.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.multibit.viewsystem.swing.action;

import java.awt.event.ActionEvent;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.AbstractAction;

import javax.swing.Action;
import org.multibit.controller.Controller;
import org.multibit.controller.bitcoin.BitcoinController;
import javax.mail.*;
import javax.mail.internet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This {@link Action} represents an action to delete a sending address.
 */
public class DoSendEmailAction extends AbstractAction {

    private static final long serialVersionUID = 200111999465875405L;
    private final Controller controller;
    private final BitcoinController bitcoinController;
    static Properties mailServerProperties;
    static MimeMessage generateMailMessage;
    static Transport transport;
    //private String to;

    private static final Logger log = LoggerFactory.getLogger(DeleteSendingAddressSubmitAction.class);

    /**
     * Creates a new {@link DeleteSendingAddressSubmitAction}.
     */
    public DoSendEmailAction(BitcoinController bitcoinController, String  email) {
       this.bitcoinController = bitcoinController;
        this.controller = this.bitcoinController;
        //to = email;
    }

    /**
     * Delete the currently selected sending address.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      final String username = "vip2uae@gmail.com";
        final String password = "vbiefqhvdotwvwyk";
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.host", "smtp.gmail.com");
        mailServerProperties.put("mail.smtp.socketFactory.port", "465");
        mailServerProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.port", "465");
         try {
        Session getMailSession = Session.getInstance(mailServerProperties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
            }
        }
        );
        generateMailMessage = new MimeMessage(getMailSession);
        
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("aziza.alsawafi@gmail.com"));
        generateMailMessage.setSubject("Greetings from Crunchify.com..");
        String emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
        generateMailMessage.setContent(emailBody, "text/html");
        transport = getMailSession.getTransport("smtp");
        transport.connect("smtp.gmail.com", username, password);
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
        } catch (MessagingException ex) {
            java.util.logging.Logger.getLogger(DoSendEmailAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
