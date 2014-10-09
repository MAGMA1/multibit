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
    static Session getMailSession;
    static MimeMessage generateMailMessage;
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
      final String username = "aziza.alsawafi@gmail.com";
        final String password = "L3BSJ300";
        //Step1     
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587"); // TLS Port
        mailServerProperties.put("mail.smtp.auth", "true"); // Enable Authentication
        mailServerProperties.put("mail.smtp.starttls.enable", "true"); // Enable StartTLS
        System.out.println("Mail Server Properties have been setup successfully..");
 
//Step2     
        try {
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("aziza.alsawafi@gmail.com"));
        
        generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("aziza.alsawafi@gmail.com"));
        generateMailMessage.setSubject("Greetings from Crunchify.com..");
        String emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
        generateMailMessage.setContent(emailBody, "text/html");
        System.out.println("Mail Session has been created successfully..");
        
 
//Step3     
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");
            // Enter your correct gmail UserID and Password
        transport.connect("smtp.gmail.com", username, password);
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
        } catch (MessagingException ex) {
            java.util.logging.Logger.getLogger(DoSendEmailAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}