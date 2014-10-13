
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

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.swing.AbstractAction;
import org.multibit.controller.Controller;
import org.multibit.controller.bitcoin.BitcoinController;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.multibit.file.WalletTableDataEntryConverter;
import org.multibit.file.WalletTableDataHeaderEntryConverter;
import org.multibit.message.MessageManager;
import org.multibit.model.bitcoin.BitcoinModel;
import org.multibit.model.bitcoin.WalletData;
import org.multibit.model.bitcoin.WalletTableData;
import org.multibit.viewsystem.swing.view.dialogs.SendEmailDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DoSendEmailAction extends AbstractAction {

    private static final long serialVersionUID = 200111999465875405L;
    private final Controller controller;
    private final BitcoinController bitcoinController;
    static Properties mailServerProperties;
    static MimeMessage generateMailMessage;
    static Transport transport;
    SendEmailDialog sendEmailD;
    JTextField mailTextF;
    File exportTransactionsFile;
    String defaultFileName;
    
    
    private static final Logger log = LoggerFactory.getLogger(DeleteSendingAddressSubmitAction.class);

    public DoSendEmailAction(BitcoinController bitcoinController, SendEmailDialog sendEmailDialog, JTextField mailTextField) {
       this.bitcoinController = bitcoinController;
        this.controller = this.bitcoinController;
        sendEmailD = sendEmailDialog;
        mailTextF = mailTextField;
    }

    @Override
    
    public void actionPerformed(ActionEvent e) {
        boolean isValid = false;
        defaultFileName =  "AttachmentTransactions" + "." + BitcoinModel.CSV_FILE_EXTENSION;
        try {
            InternetAddress emailAddr = new InternetAddress(mailTextF.getText());
            emailAddr.validate();
            isValid = true;
        } catch (AddressException ex) {
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Please enter a valid Email Address", "Warning",
            JOptionPane.WARNING_MESSAGE);   
        }
        if(isValid){
            sendEmailD.setVisible(false);
            exportTransactions(defaultFileName);
            final String username = "magma.multibit@gmail.com";
            final String password = "psdjsrvxkvlvvzvk";
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
        
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTextF.getText()));
            generateMailMessage.setSubject("Multibit Wallet: Your transactions.");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Dear MultiBit User,\n \n Attached the CSV file which contains all your transactions.\n \n Regards,\n MAGMA-MultiBit");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String filename = "AttachmentTransactions.csv";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            generateMailMessage.setContent(multipart );
            transport = getMailSession.getTransport("smtp");
            transport.connect("smtp.gmail.com", username, password);
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
            } catch (MessagingException ex) {
                java.util.logging.Logger.getLogger(DoSendEmailAction.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (sendEmailD != null) {
            
                boolean deleteWasSuccessful = exportTransactionsFile.delete();
                if (!deleteWasSuccessful) {
                    String message2 = controller.getLocaliser().getString("exportTransactionsSubmitAction.genericCouldNotDelete",
                        new Object[] { defaultFileName});
                    log.error(message2);
                MessageManager.INSTANCE.addMessage(new org.multibit.message.Message(message2));
                }
            }
        }
    }

    void exportTransactions(String exportTransactionsFilename) {
        String message;
        if (new File(exportTransactionsFilename).isDirectory()) {
            message = controller.getLocaliser().getString("exportTransactionsSubmitAction.fileIsADirectory",
                    new Object[] { exportTransactionsFilename });
            log.debug(message);
            MessageManager.INSTANCE.addMessage(new org.multibit.message.Message(message));
            return;
        }

        // If the filename has no extension, put on the wallet extension.
        if (!exportTransactionsFilename.contains(".")) {
             exportTransactionsFilename = exportTransactionsFilename + "." + BitcoinModel.CSV_FILE_EXTENSION;
        }

        exportTransactionsFile = new File(exportTransactionsFilename);
        exportTransactionsDoIt(bitcoinController.getModel().getActivePerWalletModelData(), exportTransactionsFilename);
    }
    public void exportTransactionsDoIt(WalletData walletData, String exportTransactionsFilename) {        
        List<WalletTableData> walletTableDataList = bitcoinController.getModel().createWalletTableData(bitcoinController, walletData);
        
        // Sort by date descending.
        Comparator<WalletTableData> comparator = new Comparator<WalletTableData>() {
            @Override
            public int compare(WalletTableData o1, WalletTableData o2) {
                if (o1 == null) {
                    if (o2 == null) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    if (o2 == null) {
                        return -1;
                    }
                }
                Date d1 = o1.getDate();
                Date d2 = o2.getDate();
                if (d1 == null) {
                    // Object 1 has missing date.
                    return 1;
                }
                if (d2 == null) {
                    // Object 2 has missing date.
                    return -1;
                }
                long n1 = d1.getTime();
                long n2 = d2.getTime();
                if (n1 == 0) {
                    // Object 1 has missing date.
                    return 1;
                }
                if (n2 == 0) {
                    // Object 2 has missing date.
                    return -1;
                }
                if (n1 < n2) {
                    return -1;
                } else if (n1 > n2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        Collections.sort(walletTableDataList, Collections.reverseOrder(comparator));
        
        //Writer out = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            //out = new FileWriter(exportTransactionsFilename);
            outputStreamWriter = new OutputStreamWriter ( new FileOutputStream(exportTransactionsFilename, true ), "UTF-8" ); 
            
            // Write the header row.
            WalletTableDataHeaderEntryConverter headerConverter = new WalletTableDataHeaderEntryConverter();
            headerConverter.setBitcoinController(bitcoinController);
            CSVWriter<WalletTableData> csvHeaderWriter = new CSVWriterBuilder<WalletTableData>(outputStreamWriter).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(headerConverter).build();
            
            csvHeaderWriter.write(new WalletTableData(null));
            
            // Write the body of the CSV file.
            WalletTableDataEntryConverter converter = new WalletTableDataEntryConverter();
            converter.setBitcoinController(bitcoinController);
            CSVWriter<WalletTableData> csvWriter = new CSVWriterBuilder<WalletTableData>(outputStreamWriter).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            
            csvWriter.writeAll(walletTableDataList);
            String message = controller.getLocaliser().getString("exportTransactionsSubmitAction.success",
                    new Object[] { exportTransactionsFilename });
            MessageManager.INSTANCE.addMessage(new org.multibit.message.Message(message));
        } catch (NullPointerException e) {
            String message = controller.getLocaliser().getString("exportTransactionsSubmitAction.failure",
                    new Object[] { exportTransactionsFilename, e.getClass().getName() });
            log.error(message);
            MessageManager.INSTANCE.addMessage(new org.multibit.message.Message(message));
        } catch (IOException e) {
            String message = controller.getLocaliser().getString("exportTransactionsSubmitAction.failure",
                    new Object[] { exportTransactionsFilename, e.getMessage() });
            log.error(message);
            MessageManager.INSTANCE.addMessage(new org.multibit.message.Message(message));
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                } catch (IOException ioe) {
                    String message = controller.getLocaliser().getString("exportTransactionsSubmitAction.failure",
                            new Object[] { exportTransactionsFilename, ioe.getMessage() });
                    log.error(message);
                    MessageManager.INSTANCE.addMessage(new org.multibit.message.Message(message));
                }
            }
        }
    }
    
}

