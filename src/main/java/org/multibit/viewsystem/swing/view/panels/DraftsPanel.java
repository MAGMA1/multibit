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
import java.awt.ComponentOrientation;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.multibit.controller.Controller;
import org.multibit.message.Message;
import org.multibit.message.MessageListener;
import org.multibit.viewsystem.DisplayHint;
import org.multibit.viewsystem.View;
import org.multibit.viewsystem.Viewable;
import org.multibit.viewsystem.swing.ColorAndFontConstants;
import org.multibit.viewsystem.swing.MultiBitFrame;

/**
 * The messages view.
 */
public class DraftsPanel extends JPanel implements Viewable, MessageListener {
    private static final long serialVersionUID = 191662512399957705L;

    private Controller controller;    
       
  /**
     * Creates a new {@link MessagesPanel}.
     */
    public DraftsPanel(Controller controller, MultiBitFrame mainFrame) {        
         setBackground(ColorAndFontConstants.VERY_LIGHT_BACKGROUND_COLOR);
        this.controller = controller;
       
        setLayout(new BorderLayout());
        

        applyComponentOrientation(ComponentOrientation.getOrientation(controller.getLocaliser().getLocale()));
               
    }

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

    @Override
    public void newMessageReceived(Message newMessage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}