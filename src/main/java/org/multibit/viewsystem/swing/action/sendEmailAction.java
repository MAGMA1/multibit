
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

import org.multibit.controller.Controller;
import org.multibit.controller.bitcoin.BitcoinController;
import org.multibit.viewsystem.swing.MultiBitFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import org.multibit.utils.ImageLoader;
import org.multibit.viewsystem.swing.view.components.MultiBitDialog;
import org.multibit.viewsystem.swing.view.components.MultiBitTextField;
import org.multibit.viewsystem.swing.view.dialogs.SendEmailDialog;
import org.multibit.viewsystem.swing.view.panels.ShowTransactionsPanel;

/**
 * This {@link Action} exports transactions from a wallet.
 */
public class sendEmailAction extends AbstractAction {

    private static final Logger log = LoggerFactory.getLogger(CreateWalletSubmitAction.class);

    private static final long serialVersionUID = 1923492460523457765L;

    private final Controller controller;
    private final BitcoinController bitcoinController;
    
    private MultiBitFrame mainFrame;

    private Font adjustedFont;
    
    private SendEmailDialog sendEmailDialog;
    
    private ShowTransactionsPanel showTransactionsPanel;
    
    /**
     * Creates a new {@link ExportTransactionsSubmitAction}.
     */
    public sendEmailAction(BitcoinController bitcoinController, MultiBitFrame mainFrame) {
        super(bitcoinController.getLocaliser().getString("exportTransactionsSubmitAction.text"), ImageLoader.createImageIcon(ImageLoader.TRANSACTIONS_EXPORT_ICON_FILE));
        this.bitcoinController = bitcoinController;
        this.controller = this.bitcoinController;
        this.mainFrame = mainFrame;

        
    }

    /**
     * Ask the user for a filename and then export transactions to there.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        
        sendEmailDialog = new SendEmailDialog(bitcoinController, mainFrame, showTransactionsPanel);
        sendEmailDialog.setVisible(true);
        
        
        
        
    }
}

