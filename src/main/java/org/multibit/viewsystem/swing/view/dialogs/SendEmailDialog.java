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
package org.multibit.viewsystem.swing.view.dialogs;

import org.multibit.viewsystem.swing.view.panels.SendBitcoinPanel;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import static java.lang.System.out;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.multibit.controller.Controller;
import org.multibit.controller.bitcoin.BitcoinController;
import org.multibit.utils.ImageLoader;
import org.multibit.viewsystem.swing.ColorAndFontConstants;
import org.multibit.viewsystem.swing.MultiBitFrame;
import org.multibit.viewsystem.swing.action.CancelBackToParentAction;
import org.multibit.viewsystem.swing.action.DeleteSendingAddressSubmitAction;
import org.multibit.viewsystem.swing.action.DoSendEmailAction;
import org.multibit.viewsystem.swing.action.sendEmailAction;
import org.multibit.viewsystem.swing.view.components.FontSizer;
import org.multibit.viewsystem.swing.view.components.MultiBitButton;
import org.multibit.viewsystem.swing.view.components.MultiBitDialog;
import org.multibit.viewsystem.swing.view.components.MultiBitLabel;
import org.multibit.viewsystem.swing.view.panels.ShowTransactionsPanel;

/**
 * The delete sending address confirm dialog.
 */
public class SendEmailDialog extends MultiBitDialog {
    private static final long serialVersionUID = 191435699945057705L;

    private static final int HEIGHT_DELTA = 100;
    private static final int WIDTH_DELTA = 200;

    private final Controller controller;
    private final BitcoinController bitcoinController;

    private MultiBitLabel labelText;
    private MultiBitLabel addressLabelText;

    private MultiBitLabel explainLabel;

    private MultiBitButton doSendEmailButton;
    private MultiBitButton cancelButton;

    private ShowTransactionsPanel showTransactionsPanel;

    /**
     * Creates a new {@link DeleteWalletConfirmDialog}.
     */
    public SendEmailDialog(BitcoinController bitcoinController, MultiBitFrame mainFrame,
            ShowTransactionsPanel showTransactionsPanel) {
        super(mainFrame, "Send Email");
        
        this.bitcoinController = bitcoinController;
        this.controller = this.bitcoinController;
        
        this.showTransactionsPanel = showTransactionsPanel;

        ImageIcon imageIcon = ImageLoader.createImageIcon(ImageLoader.MULTIBIT_ICON_FILE);
        if (imageIcon != null) {
            setIconImage(imageIcon.getImage());
        }

        initUI();

        cancelButton.requestFocusInWindow();
        applyComponentOrientation(ComponentOrientation.getOrientation(controller.getLocaliser().getLocale()));
    }

    /**
     * Initialise dialog.
     */
    public void initUI() {
        FontMetrics fontMetrics = getFontMetrics(FontSizer.INSTANCE.getAdjustedDefaultFont());

        int minimumHeight = fontMetrics.getHeight() * 5 + HEIGHT_DELTA;
        int minimumWidth = Math.max(fontMetrics.stringWidth(this.bitcoinController.getModel().getActiveWalletFilename()),
                fontMetrics.stringWidth("Write the Email to which the transactions will be sent."))
                + WIDTH_DELTA;
        setMinimumSize(new Dimension(minimumWidth, minimumHeight));
        positionDialogRelativeToParent(this, 0.5D, 0.47D);

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        explainLabel = new MultiBitLabel("");
        explainLabel.setText("Write the Email to which the transactions will be sent.");
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 0.8;
        constraints.weighty = 0.3;
        constraints.gridwidth = 5;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        mainPanel.add(explainLabel, constraints);

        JPanel detailPanel = new JPanel(new GridBagLayout());
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.weightx = 0.01;
        constraints.weighty = 0.01;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        mainPanel.add(detailPanel, constraints);

        GridBagConstraints constraints2 = new GridBagConstraints();

        JLabel emailLabel = new JLabel();
        constraints2.fill = GridBagConstraints.BOTH;
        constraints2.gridx = 1;
        constraints2.gridy = 0;
        constraints2.weightx = 0.001;
        constraints2.weighty = 0.009;
        constraints2.gridwidth = 1;
        constraints2.gridheight = 1;
        constraints2.anchor = GridBagConstraints.LINE_START;
        emailLabel.setText("Email");
        detailPanel.add(emailLabel, constraints2);
        
        JTextField emailTextField = new JTextField();
        constraints2.fill = GridBagConstraints.BOTH;
        constraints2.gridx = 2;
        constraints2.gridy = 0;
        constraints2.weightx = 0.009;
        constraints2.weighty = 0.001;
        constraints2.gridwidth = 1;
        constraints2.gridheight = 1;
        constraints2.anchor = GridBagConstraints.CENTER;
        detailPanel.add(emailTextField, constraints2);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.weightx = 0.8;
        constraints.weighty = 0.1;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        mainPanel.add(buttonPanel, constraints);
        
        DoSendEmailAction doSendEmailAction = new DoSendEmailAction(this.bitcoinController, this, emailTextField);
        doSendEmailButton = new MultiBitButton(doSendEmailAction, controller);
        doSendEmailButton.setText("Send Email");
        buttonPanel.add(doSendEmailButton);
        

        CancelBackToParentAction cancelAction = new CancelBackToParentAction(controller, null, this);
        cancelButton = new MultiBitButton(cancelAction, controller);
        buttonPanel.add(cancelButton);

    }
}