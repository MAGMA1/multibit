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

import org.multibit.controller.bitcoin.BitcoinController;
import org.multibit.model.bitcoin.BitcoinModel;
import org.multibit.model.bitcoin.WalletAddressBookData;
import org.multibit.model.bitcoin.WalletData;
import org.multibit.model.bitcoin.WalletInfoData;
import org.multibit.store.MultiBitWalletVersion;
import org.multibit.viewsystem.swing.view.panels.SendBitcoinPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import static java.lang.System.out;
import org.multibit.viewsystem.swing.view.models.AddressBookTableModel;
import org.multibit.viewsystem.swing.view.panels.AbstractTradePanel;

/**
 * This {@link Action} represents an action to create a sending address.
 */
public class SearchAddressAction extends AbstractAction {

    private static final long serialVersionUID = 200111935465875405L;
    private BitcoinController bitcoinController;
    private SendBitcoinPanel sendBitcoinPanel;
    private AbstractTradePanel tradePanel;
    private AddressBookTableModel addressBookModel;
    private JComboBox filterBy;
    private String criStr;
    private JTextField criteria;

    /**
     * Creates a new {@link CreateNewSendingAddressAction}.
     */
    public SearchAddressAction(BitcoinController bitcoinController, SendBitcoinPanel sendBitcoinPanel, JComboBox searchByList, JTextField criteria) {
        this.bitcoinController = bitcoinController;
        this.sendBitcoinPanel = sendBitcoinPanel;
        this.filterBy = searchByList;
        this.criteria = criteria;
        
    }

    /**
     * Create new send address.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        criStr = criteria.getText();
        int rowIndex;
        String strBy = (String) filterBy.getItemAt(filterBy.getSelectedIndex());
        addressBookModel = new AddressBookTableModel(this.bitcoinController, false);
        int rowCount = addressBookModel.getRowCount();
        if(strBy.equals("Label")){
            for(int i=0; i<rowCount; i++){   
                String indexStr = (String)addressBookModel.getValueAt(i, 0);
                if(indexStr.equalsIgnoreCase(criStr)){
                    rowIndex = i;
                    out.println(rowIndex);
                }
            }
        }
        else{
            for(int i=0; i<rowCount; i++){   
                String indexStr = (String)addressBookModel.getValueAt(i, 1);
                if(indexStr.equalsIgnoreCase(criStr)){
                    rowIndex = i;
                    out.println(rowIndex);
                }
            }
        }
    }
}