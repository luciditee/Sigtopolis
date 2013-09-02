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

import com.sigmatauproductions.sigtopolis.util.FontHandler;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

/**
 * A basic checkbox component that has a checked/unchecked state and support for
 * a label.
 * @author Will
 */
public class PaneCheckbox extends PaneComponent implements Clickable {
    
    /**
     * The smallest pixel size of a checkbox.
     */
    public static final int SMALL = 16;
    
    /**
     * A medium-sized checkbox.
     */
    public static final int MEDIUM = 32;
    
    /**
     * A large-sized checkbox.
     */
    public static final int LARGE = 64;
    
    /**
     * The padding between the edge of the box and its label.
     */
    public static final int labelPadding = 5;
    
    /**
     * The x-coordinate of the box.
     */
    private int x = 0;
    
    /**
     * The y-coordinate of the box.
     */
    private int y = 0;
    
    /**
     * The parent {@link Pane} of this component.
     */
    private Pane pane;
    
    /**
     * Tracks whether or not this box is checked.
     * 
     * @see isChecked()
     */
    private boolean checked = false;
    
    /**
     * The bounding rectangle of the checkbox.
     */
    private Rectangle rect = new Rectangle(0f, 0f, 0f, 0f);
    
    /**
     * Tracks whether or not this box has been clicked.
     */
    private boolean clicked = false;
    
    /**
     * The label of the checkbox.
     */
    private String label = "";
    
    /**
     * The {@link FontHandler} of this component, used for displaying the label.
     */
    private FontHandler text;
    
    /**
     * The {@link Action} performed by this box once clicked.
     */
    private Action action = null;
    
    /**
     * The color of this checkbox when the mouse is moved over.
     */
    public static final Color ENABLED_COLOR = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    
    /**
     * The color of this checkbox when it is disabled.
     */
    public static final Color DISABLED_COLOR = new Color(1.0f, 0f, 0f, 1.0f);
    
    /**
     * The color of the checkbox while idling.
     */
    public static final Color INACTIVE_COLOR = new Color(1.0f, 1.0f, 1.0f,0.5f);
    
    /**
     * Creates a basic checkbox without a label.
     * @param size
     * @param x
     * @param y
     * @param pane
     * @param input
     * @param text 
     */
    public PaneCheckbox(int size, int x, int y, Pane pane, Input input,
            FontHandler text) {
        super(size,size,input);
        this.x = x;
        this.y = y;
        this.pane = pane;
        this.text = text;
    }
    
    /**
     * Creates a basic checkbox with a label.
     * @param size
     * @param x
     * @param y
     * @param pane
     * @param label
     * @param input
     * @param text 
     */
    public PaneCheckbox(int size, int x, int y, HalfPane pane, String label,
            Input input, FontHandler text) {
        this(size, x, y, pane, input, text);
        this.label = label;
    }
    
    /**
     * Called once per frame, this tracks when the checkbox has been clicked.
     * @param gc
     * @param g
     * @param i 
     */
    @Override
    public final void update(GameContainer gc, Graphics g, Input i) {
        if (i.isMouseButtonDown(0) && (mouseOver(rect, i) || mouseOverLabel(i))
                && !clicked) {
            clicked = true;
        }
        
        if (!i.isMouseButtonDown(0) && clicked) {
            onClick(0);
            clicked = false;
        }
    }
    
    /**
     * Called once per render cycle, this draws the checkbox and its label to
     * the screen.
     * @param gc
     * @param g 
     */
    @Override
    public final void draw(GameContainer gc, Graphics g) {
        int lx = pane.getPosition().x + x;
        int ly = pane.getPosition().y + y;
        rect = new Rectangle((float)lx, (float)ly,
                getX(), getY());
        ShapeFill fill;
        Color color = (isEnabled()) ? ENABLED_COLOR : DISABLED_COLOR;
        if ((!mouseOver(rect, input) && !mouseOverLabel(input))
                && !checked) { color = INACTIVE_COLOR; }
        fill = new GradientFill(0, 0, color, lx+(getX()/2),
                    ly+(getY()), color, true);
        
        
        if (checked) {
            Rectangle checkedRect = new Rectangle(rect.getX(),
                    rect.getY(), rect.getWidth(), rect.getHeight());
            checkedRect.grow(-6, -6);
            g.fill(checkedRect, fill);
        }
        
        g.draw(rect, fill);
        
        if (!label.equals("")) {
            switch (getX()) {
                case 16:
                    text.drawSmallText(label,pane.getPosition().x+x+getX()
                            +labelPadding,(y+6),color);
                break;
                case 32:
                    text.drawMediumText(label,pane.getPosition().x+x+getX()
                            +labelPadding,(y+12),
                            color);
                break;
                case 64:
                    text.drawLargeText(label,pane.getPosition().x+x+getX()
                            +labelPadding,(y+24),color);
                break;
            }
        }
    }
    
    /**
     * This sets the action of the checkbox (if applicable).
     * @param a 
     */
    public final void setAction(Action a) {
        action = a;
    }
    
    /**
     * Called when the checkbox is clicked, and activates its {@link Action}
     * (if any).
     * @param button 
     */
    @Override
    public final void onClick(int button) {
        checked = (!checked);
        if (action != null) {
            action.operation();
        }
    }
    
    /**
     * Returns whether or not the checkbox is checked.
     * @return 
     */
    public final boolean isChecked() {
        return checked;
    }
    
    /**
     * Sets whether or not the checkbox is checked.
     * @param c 
     */
    public final void setChecked(boolean c) {
        checked = c;
    }

    /**
     * Used internally to determine if the mouse is hovering over the label.
     * @param input
     * @return 
     */
    private boolean mouseOverLabel(Input input) {
        int mouseX = input.getAbsoluteMouseX();
        int mouseY = input.getAbsoluteMouseY();
        int labelWidth = label.length()*getX();
        int labelHeight = getY();
        int labelX = pane.getPosition().x+x+(getX()/3)+labelPadding;
        int labelY = pane.getPosition().y+y;
        
        if ((mouseX > labelX && mouseX < (labelX+labelWidth))
                && (mouseY > labelY && mouseY < (labelY+labelHeight))) {
            return true;
        }
        
        return false;
    }
}
