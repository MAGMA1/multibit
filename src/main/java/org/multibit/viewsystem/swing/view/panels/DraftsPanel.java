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
package org.multibit.viewsystem.swing.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.multibit.controller.Controller;
import org.multibit.controller.bitcoin.BitcoinController;
import org.multibit.model.bitcoin.BitcoinModel;
import org.multibit.model.bitcoin.WalletAddressBookData;
import org.multibit.model.bitcoin.WalletInfoData;
import org.multibit.model.core.CoreModel;
import org.multibit.viewsystem.DisplayHint;
import org.multibit.viewsystem.View;
import org.multibit.viewsystem.Viewable;
import org.multibit.viewsystem.swing.ColorAndFontConstants;
import org.multibit.viewsystem.swing.MultiBitFrame;
import org.multibit.viewsystem.swing.action.RetrieveDraftsAction;
import org.multibit.viewsystem.swing.view.components.MultiBitButton;
import org.multibit.viewsystem.swing.view.models.DraftsTableModel;

/**
 * The messages view.
 */
public class DraftsPanel extends JPanel implements Viewable {
    private static final long serialVersionUID = 191662512399957705L;

    private Controller controller;    
    private MultiBitButton retrieveDraftButton;
    private Action retrieveDraftsAction;
    private final BitcoinController bitcoinController;
    
    private MultiBitFrame mainFrame;
    private JTable table;
    private TableRowSorter rowSorter;
    private JScrollPane scrollPane;
    /**
     * Creates a new {@link MessagesPanel}.
     */
    public DraftsPanel(BitcoinController bitcoinController, MultiBitFrame mainFrame) throws IOException {        
         setBackground(ColorAndFontConstants.VERY_LIGHT_BACKGROUND_COLOR);
        this.bitcoinController = bitcoinController;
        
        setBackground(ColorAndFontConstants.VERY_LIGHT_BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        
        initUI();

               
    }
    private void initUI() throws IOException {
        setLayout(new BorderLayout());
        JPanel buttonPanel = createButtonPanel();

        JPanel draftsPanel = createDraftsPanel();
        add(draftsPanel, BorderLayout.CENTER);
        
        buttonPanel.setMinimumSize(new Dimension(60, 60));
        add(buttonPanel, BorderLayout.SOUTH);
    }
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, SystemColor.windowBorder));
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(ColorAndFontConstants.MID_BACKGROUND_COLOR);
        
        retrieveDraftsAction = new RetrieveDraftsAction(bitcoinController, mainFrame);
        retrieveDraftButton = new MultiBitButton(retrieveDraftsAction, controller);
        retrieveDraftButton.setText("Retrieve");
        retrieveDraftButton.setEnabled(false);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 0.1;
        constraints.weighty = 1.0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        buttonPanel.add(retrieveDraftButton, constraints);

        JPanel fill1 = new JPanel();
        fill1.setOpaque(false);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.weightx = 200;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        buttonPanel.add(fill1, constraints);

        return buttonPanel;
    }
    public JPanel createDraftsPanel() throws IOException {
        JPanel draftsPanel = new JPanel();
        draftsPanel.setMinimumSize(new Dimension(550, 160));
        draftsPanel.setBackground(ColorAndFontConstants.VERY_LIGHT_BACKGROUND_COLOR);
        draftsPanel.setLayout(new GridBagLayout());
        draftsPanel.setOpaque(true);
        GridBagConstraints constraints = new GridBagConstraints();
        DraftsTableModel model = new DraftsTableModel(bitcoinController); 
        /*String[] tableHeaderKeys = new String[] { "Address",
            "Label","AmountBTC", "Amount$" };
        model.addColumn(tableHeaderKeys[0]);
        model.addColumn(tableHeaderKeys[1]);
        model.addColumn(tableHeaderKeys[2]);
        model.addColumn(tableHeaderKeys[3]);
        ArrayList<String> dataArray = draftData();
        int i =0;
        while(i<dataArray.size()){
            String row = dataArray.get(i);
            String[] parts = row.split("////");
            model.addRow(parts);
            i++;
        }*/
        table = new JTable(model);
        table.setOpaque(false);
        table.setBorder(BorderFactory.createEmptyBorder());
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setBackground(ColorAndFontConstants.VERY_LIGHT_BACKGROUND_COLOR);
        scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPaneSetup();   
        
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;

        draftsPanel.add(scrollPane, constraints);
        
        return draftsPanel;
    }
    private void scrollPaneSetup() {
        scrollPane.setBackground(ColorAndFontConstants.VERY_LIGHT_BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(ColorAndFontConstants.VERY_LIGHT_BACKGROUND_COLOR);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(CoreModel.SCROLL_INCREMENT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(CoreModel.SCROLL_INCREMENT);
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, SystemColor.windowBorder));
    }
    /*private ArrayList draftData(){
        ArrayList<String> array = new ArrayList();
        String address = "";
        WalletInfoData addressBook = bitcoinController.getModel().getActiveWalletWalletInfo();
            if (addressBook != null) {
                ArrayList<WalletAddressBookData> receivingAddresses = addressBook.getReceivingAddresses();
                if (receivingAddresses != null) {
                    if (receivingAddresses.iterator().hasNext()) {
                        WalletAddressBookData addressBookData = receivingAddresses.iterator().next();
                        if (addressBookData != null) {
                            address = addressBookData.getAddress();
                            //bitcoinController.getModel().setActiveWalletPreference(BitcoinModel.RECEIVE_ADDRESS, address);
                        }
                    }
                }
            }
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(address + "_Drafts.txt"));
            String line;
                try {
                    line = br.readLine();
            while (line != null) {
                array.add(line);
                line = br.readLine();
            }
        } finally {
            br.close();
        }
        } catch (IOException ex) {
            Logger.getLogger(DraftsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return array;
    }*/

    @Override
    public void navigateAwayFromView() {
    }

    @Override
    public void displayView(DisplayHint displayHint) {
    }
       
    @Override
    public Icon getViewIcon() {
        return new ImageIcon("draft.png");
    }

    @Override
    public String getViewTitle() {
        return "Drafts";
    }
    
    @Override
    public String getViewTooltip() {
        return "View Drafts";
    }

    @Override
    public View getViewId() {
        return View.DRAFT_VIEW;
    }
}