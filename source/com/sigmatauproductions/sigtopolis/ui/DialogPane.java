/*
 * Copyright (C) 2013, Sigma-Tau Productions.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies, 
 * either expressed or implied, of the FreeBSD Project.
 */
package com.sigmatauproductions.sigtopolis.ui;

import com.sigmatauproductions.isomatrix.game.IsomatrixState;
import com.sigmatauproductions.isomatrix.util.Transform2D;
import com.sigmatauproductions.sigtopolis.util.FontHandler;
import com.sigmatauproductions.sigtopolis.util.WordWrap;
import org.newdawn.slick.*;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.*;

/**
 * A basic class for the creation of dialog boxes for error messages, alerts,
 * and other instant notifications.
 * @author Will
 */
public final class DialogPane extends Pane {
    
    /**
     * The string in the dialog box's title bar.
     */
    private String title;
    
    /**
     * The text of the dialog box.
     */
    private String mainText;
    
    /**
     * The {@link IsomatrixState} this dialog will be used in.
     */
    private IsomatrixState state;
    
    /**
     * The {@link FontHandler} used for drawing text in this dialog box.
     */
    private FontHandler text;
    
    /**
     * Tracks whether or not this dialog is hidden or visible.
     */
    private boolean hidden = true;
    
    /**
     * The bounding rectangle of the dialog box.
     */
    private Rectangle backgroundRect =  new Rectangle(0f, 0f, 0f, 0f);
    
    /**
     * Used for receiving input from the user.
     */
    private Input input;
    
    /**
     * Tracks whether or not we have completed the first frame of this dialog's
     * lifetime.
     */
    private boolean firstFrame = false;
    
    /**
     * The x-offset of the upper left corner of the box, relative to the center
     * of the screen.
     */
    private int offsetX = 0;
    
    /**
     * The y-offset of the upper left corner of the box, relative to the center
     * of the screen.
     */
    private int offsetY = 0;
    
    /**
     * The initial x-offset of the upper left corner of the box, relative to the
     * center of the screen.
     */
    private int initialOffsetX = 0;
    
    /**
     * The initial y-offset of the upper left corner of the box, relative to the
     * center of the screen.
     */
    private int initialOffsetY = 0;
    
    /**
     * The button used to close the dialog box.
     */
    private PaneButton closeButton;
    
    /**
     * Tracks whether or not this dialog box will use the close button.
     */
    private boolean useCloseButton = true;
    
    /**
     * The initial Y-offset of the close button, as found in the first frame
     * of this dialog's lifetime.
     */
    private int closeInitialY = 0;
    
    /**
     * An anonymous inner class containing the action for this dialog.
     */
    private Action closeButtonAction = new Action(){
        @Override
        public void operation() {
            hidden = true;
        }
    };
    
    /**
     * The width of the dialog.
     */
    public final int width = 300;
    
    /**
     * The height of the dialog.
     */
    public final int height = 200;
    
    /**
     * The padding on the text within the dialog.
     */
    public final int padding = 10;
    
    /**
     * Creates a new dialog box.
     * @param title
     * @param mainText
     * @param state
     * @param text
     * @param input 
     */
    public DialogPane(String title, String mainText, IsomatrixState state,
            FontHandler text, Input input) {
        this.title = title;
        this.state = state;
        this.text = text;
        this.input = input;
        this.mainText = mainText;
    }
    
    /**
     * Creates a new dialog box and allows the specification of whether or not
     * the close button will be used.
     * @param title
     * @param mainText
     * @param state
     * @param text
     * @param input 
     */
    public DialogPane(String title, String mainText, IsomatrixState state,
            FontHandler text, Input input, boolean useCloseButton) {
        this(title, mainText, state, text, input);
        this.useCloseButton = useCloseButton;
    }
    
    /**
     * Private constructor used to prevent programmers from creating a dialog
     * without any default values.
     */
    private DialogPane() {}

    /**
     * Called once per frame, this method updates the components found within
     * the dialog.
     * @param gc
     * @param g 
     */
    public void update(GameContainer gc, Graphics g) {
        if (!firstFrame) {
            offsetX = (gc.getWidth()/2)-(width/2);
            offsetY = (gc.getHeight()/2)-(height/2);
            initialOffsetX = offsetX;
            initialOffsetY = offsetY;
            
            closeButton = new PaneButton(PaneButton.SMALL_SIZE,offsetX,offsetY-
                    height,
                    "Close", this, 10, closeButtonAction, text, input);
            closeInitialY = closeButton.getY();
            closeButton.setX(width-closeButton.getWidth()-padding-closeButton
                    .getPadding());
            closeButton.setY(closeInitialY+padding);
            closeButton.setDrawBorder(false);
            addComponent(closeButton);
            
            firstFrame = true;
        }
        
        for (PaneComponent component : components) {
            if (!hidden) {
                component.update(gc, g, input);
            }
        }
    }

    /**
     * Called once per render cycle, this method draws the dialog to the screen
     * as well as its components.
     * @param gc
     * @param g
     * @param input 
     */
    public void draw(GameContainer gc, Graphics g, Input input) {
        if (!hidden && firstFrame) {
            closeButton.setX(width-closeButton.getWidth()-padding-
                    closeButton.getPadding());
            closeButton.setY(closeInitialY+padding);
            
            Color color = new Color(0.0f, 0.0f, 0.0f, 0.7f);
            backgroundRect = new Rectangle(offsetX, offsetY, width, height);
            GradientFill fill = new GradientFill(offsetX, offsetY, color,
                    offsetX+width, offsetY+height, color);
            g.fill(backgroundRect, fill);
            text.drawSmallText(title, offsetX+padding, offsetY+padding,
                    Color.white);
            Line separator = new Line(offsetX+padding, (offsetY+
                    text.getSmallFont().getLineHeight()*2)-padding, offsetX+width-padding,
                    (offsetY+text.getSmallFont().getLineHeight()*2)-padding);
            g.draw(separator, new GradientFill(separator.getX1(),separator
                    .getY1(), Color.white, separator.getX2(), separator
                    .getY2(), Color.white));
            
            String[] lines = WordWrap.getLines(mainText, text.getSmallFont(),
                    width-(padding*4));
            
            for (int i = 0; i < lines.length; i++) {
                text.drawSmallText(lines[i], offsetX+padding,
                        (int)separator.getY1()+padding
                        +(text.getSmallFont().getLineHeight()*i), Color.white);
                
            }
            
            closeButton.setHidden(!useCloseButton);
            
            // Draw components--always do last!
            for (PaneComponent component : components) {
                component.draw(gc, g);
            }
        }
    }
    
    /**
     * Sets the title of the dialog box.
     * @param t 
     */
    public void setTitle(String t) {
        title = t;
    }
    
    /**
     * Sets whether the dialog is visible or not.
     * @param v 
     */
    public void setVisible(boolean v) {
        hidden = !v;
    }
    
    /**
     * Returns whether the dialog is visible or not.
     * @return 
     */
    public boolean isVisible() {
        return !hidden;
    }
    
    /**
     * Returns the position of the dialog box in screen-space.
     * @return 
     */
    @Override
    public Transform2D getPosition() {
        return new Transform2D((int) backgroundRect.getX(),
                (int) backgroundRect.getY());
    }
    
    /**
     * Returns the width of the dialog box.
     * @return 
     */
    @Override
    public int getWidth() {
        return width;
    }
    
    /**
     * Returns the height of the dialog box.
     * @return 
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Returns the text padding of the dialog box.
     * @return 
     */
    @Override
    public final Transform2D getPadding() {
        return new Transform2D(padding, padding);
    }
    
}
