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

import com.sigmatauproductions.isomatrix.util.Transform2D;
import com.sigmatauproductions.sigtopolis.util.FontHandler;
import org.newdawn.slick.*;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.*;

/**
 * A basic clickable, rectangular button for taking input.
 * @author Will
 */
public class PaneButton extends PaneComponent implements Clickable {
    
    /**
     * The smallest-sized button.
     */
    public static final int SMALL_SIZE = 0;
    
    /**
     * A medium sized button.
     */
    public static final int MEDIUM_SIZE = 1;
    
    /**
     * A large sized button.
     */
    public static final int LARGE_SIZE = 2;
    
    /**
     * The color of the button during its idle state.
     */
    public static final Color INACTIVE_COLOR = new Color(1f, 1f, 1f, 0.5f);
    
    /**
     * The color of the button when the mouse hovers over it.
     */
    public static final Color MOUSEOVER_COLOR = new Color(1f, 1f, 1f, .8f);
    
    /**
     * The color of the button when it is set to be disabled.
     */
    public static final Color DISABLED_COLOR = new Color(0.86f, 0f, 0f, 1f);
    
    /**
     * The label on this button.
     */
    private String label = "";
    
    /**
     * The padding surrounding the text on this button.
     */
    private int padding = 0;
    
    /**
     * Tracks whether or not this button is hidden.
     */
    private boolean hidden = false;
    
    /**
     * The rectangular enclosure of this button.
     */
    private Rectangle rect = new Rectangle(0f, 0f, 0f, 0f);
    
    /**
     * The {@link FontHandler} of this button, used for drawing the button
     * text.
     */
    private FontHandler text;
    
    /**
     * The size of the button, used internally.
     */
    private int size;
    
    /**
     * The number of pixels (in both directions) to offset the button's text
     * by when a mouse click occurs.  For internal use only.
     */
    private int clickOffset = 1;
    
    /**
     * The x-position of the button.
     */
    private int x;
    
    /**
     * The y-position of the button.
     */
    private int y;
    
    /**
     * The {@link Pane} this button is assigned to.
     */
    private Pane pane;
    
    /**
     * Tracks whether or not this button has been clicked.
     */
    private boolean clicked = false;
    
    /**
     * The {@link Action} this button performs when clicked.
     */
    private Action action;
    
    /**
     * Tracks whether or not the rectangular border should be drawn around the
     * button.
     */
    private boolean drawBorder = true;
    
    /**
     * Constructor for a basic button.
     * @param size
     * @param x
     * @param y
     * @param label
     * @param pane
     * @param padding
     * @param action
     * @param text
     * @param input 
     */
    public PaneButton(int size, int x, int y, String label, Pane pane,
            int padding, Action action, FontHandler text, Input input) {
        super(x, y, input);
        this.x = x;
        this.y = y;
        this.size = size;
        this.label = label;
        this.padding = padding;
        this.text = text;
        this.pane = pane;
        this.action = action;
    }
    
    /**
     * Called once per frame, this method checks to see if the button has been
     * clicked or not.
     * @param gc
     * @param g
     * @param i 
     */
    @Override
    public final void update(GameContainer gc, Graphics g, Input i) {
        
        if (i.isMouseButtonDown(0) && (mouseOver(rect, i))
                && !clicked) {
            clicked = true;
        }
        
        if (!i.isMouseButtonDown(0) && clicked) {
            onClick(0);
            clicked = false;
        }
    }
    
    /**
     * Called once per render cycle, this method draws the button to the
     * screen at its coordinates, relative to the origin of its parent
     * {@link Pane}.
     * @param gc
     * @param g 
     */
    @Override
    public final void draw(GameContainer gc, Graphics g) {
        int drawX = x + pane.getPosition().x;
        int drawY = y + pane.getPosition().y;
        if (!hidden) {
            int buttonWidth = 0;
            int buttonHeight = 0;
            if (size == SMALL_SIZE) {
                buttonWidth = text.getSmallFont().getWidth(label);
                buttonHeight = text.getSmallFont().getLineHeight();
            } else if (size == MEDIUM_SIZE) {
                buttonWidth = text.getMediumFont().getWidth(label);
                buttonHeight = text.getMediumFont().getHeight(label);
            } else if (size == LARGE_SIZE) {
                buttonWidth = text.getLargeFont().getWidth(label);
                buttonHeight = text.getLargeFont().getHeight(label);
            } else { return; }
            
            buttonWidth += padding;
            buttonHeight += padding;
            
            Color color = (mouseOver(rect, input)) ? MOUSEOVER_COLOR :
                    INACTIVE_COLOR;
            if (!isEnabled()) {
                color = DISABLED_COLOR;
            }
            
            
            rect = new Rectangle(drawX, drawY, buttonWidth, buttonHeight);
            GradientFill fill = new GradientFill((float)drawX, (float)drawY,
                    color, (float)drawX+(float)buttonWidth, 
                    (float)drawY+(float)buttonHeight, color);
            
            if (drawBorder) {
                g.draw(rect, fill);
            }
            
            drawX += padding/2;
            drawY += padding/2;
            if (input.isMouseButtonDown(0) && (mouseOver(rect, input))) {
                drawX += clickOffset;
                drawY += clickOffset;
            }
            
            if (size == SMALL_SIZE) {
                text.drawSmallText(label, drawX, drawY, color);
            } else if (size == MEDIUM_SIZE) {
                text.drawMediumText(label, drawX, drawY, color);
            } else if (size == LARGE_SIZE) {
                text.drawLargeText(label, drawX, drawY, color);
            }
        }
    }
    
    /**
     * Sets the label text of the button.
     * @param l 
     */
    public void setLabel(String l) {
        if (l != null) { label = l; }
    }
    
    /**
     * Called when the button is clicked.
     * @param button 
     */
    @Override
    public final void onClick(int button) {
        // Check to make sure they didn't drag the mouse off the button
        if (!mouseOver(rect, input)) { return; }
        if (action != null) { action.operation(); }
    }
    
    /**
     * Returns the width of the button's bounding rectangle.
     * @return 
     */
    public int getWidth() {
        return (int)rect.getWidth();
    }
    
    /**
     * Returns the height of the button's bounding rectangle.
     * @return 
     */
    public int getHeight() {
        return (int)rect.getHeight();
    }
    
    /**
     * Returns the screen-space position of the button.
     * @return 
     */
    public Transform2D getScreenPosition() {
        return new Transform2D(x + pane.getPosition().x,y+pane.getPosition().y);
    }
    
    /**
     * Returns the position of the button, relative to the position of its
     * parent pane.
     * @return 
     */
    public Transform2D getLocalPosition() {
        return new Transform2D(x, y);
    }
    
    /**
     * Returns true if the mouse is hovering over the button's bounding box.
     * @return 
     */
    public boolean isHovering() {
        return mouseOver(rect, input);
    }
    
    /**
     * Returns the text padding value of the button.
     * @return 
     */
    public int getPadding() {
        return padding;
    }
    
    /**
     * Sets whether or not the button's bounding box is drawn.
     * @param d 
     */
    public void setDrawBorder(boolean d) {
        drawBorder = d;
    }
    
    /**
     * Returns whether or not the button is hidden.
     * @return 
     */
    public boolean isHidden() {
        return hidden;
    }
    
    /**
     * Sets whether or not the button is hidden.
     * @param h 
     */
    public void setHidden(boolean h) {
        hidden = h;
    }
}
