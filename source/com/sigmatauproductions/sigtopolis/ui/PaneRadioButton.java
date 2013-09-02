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
import org.newdawn.slick.geom.Circle;

/**
 * A basic radio button component, used for making selections between a finite
 * selection of values.  Usually best when four or less options are present, in
 * which case a {@link PaneDropdown} is a more ideal choice.  These buttons
 * serve no purpose without being placed into a {@link RadioGroup}.
 * 
 * @author Will
 */
public class PaneRadioButton extends PaneComponent implements Clickable {
    
    /**
     * A small pixel-size radio button.
     */
    public static int SMALL = 16;
    
    /**
     * A medium pixel-size radio button.
     */
    public static int MEDIUM = 32;
    
    /**
     * A large pixel-size radio button.
     */
    public static int LARGE = 64;
    
    /**
     * The amount of pixel-padding between the radio button and its label.
     */
    public static final int labelPadding = 5;
    
    /**
     * The x value of the radio button.
     */
    private int x = 0;
    
    /**
     * The y-value of the radio button.
     */
    private int y = 0;
    
    /**
     * The pane this button belongs to.
     */
    private Pane pane;
    
    /**
     * Tracks whether or not this button is selected.
     */
    protected boolean checked = false;
    
    /**
     * The circle of the radio button itself.
     */
    private Circle circ = new Circle(0f, 0f, 0f);
    
    /**
     * Tracks whether or not the radio button has been clicked.
     */
    private boolean clicked = false;
    
    /**
     * The label of the button.
     */
    private String label = "";
    
    /**
     * The {@link FontHandler} of the button, used for drawing the text of the
     * component.
     */
    private FontHandler text;
    
    /**
     * The action performed by the radio button once selected.
     */
    private Action action = null;
    
    /**
     * The ID of the radio button relative to its group.
     */
    private int id = 0;
    
    /**
     * The {@link RadioGroup} this button belongs to.
     */
    private RadioGroup group = null;
    
    /**
     * The color of this button when the mouse hovers over it.
     */
    public static final Color ENABLED_COLOR = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    
    /**
     * The color of this button when disabled.
     */
    public static final Color DISABLED_COLOR = new Color(1.0f, 0f, 0f, 1.0f);
    
    /**
     * The color of this button when inactive/the mouse is not hovering over it.
     */
    public static final Color INACTIVE_COLOR = new Color(1.0f, 1.0f, 1.0f,0.5f);
    
    /**
     * Creates a basic radio button with no label.
     */
    public PaneRadioButton(int radius, int x, int y, Pane pane, Input input,
            FontHandler text, int id) {
        super(radius/2,radius/2,input);
        this.x = x;
        this.y = y;
        this.pane = pane;
        this.text = text;
        this.id = id;
    }
    
    /**
     * Creates a basic radio button with a label.
     */
    public PaneRadioButton(int radius, int x, int y, HalfPane pane,
            String label, Input input, FontHandler text, int id) {
        this(radius, x, y, pane, input, text, id);
        this.label = label;
    }
    
    /**
     * Called once per frame, this tracks whether or not the button was clicked
     * and has the button react accordingly if so.
     * @param gc
     * @param g
     * @param i 
     */
    @Override
    public final void update(GameContainer gc, Graphics g, Input i) {
        if (i.isMouseButtonDown(0) && (mouseOver(circ, i) || mouseOverLabel(i))
                && !clicked) {
            clicked = true;
        }
        
        if (!i.isMouseButtonDown(0) && clicked) {
            onClick(0);
            clicked = false;
        }
    }
    
    /**
     * Called once per render cycle, this draws the button to the screen.
     * @param gc
     * @param g 
     */
    @Override
    public final void draw(GameContainer gc, Graphics g) {
        int lx = (pane.getPosition().x + x)+getX();
        int ly = (pane.getPosition().y + y)+getY();
        circ = new Circle((float)lx, (float)ly, (float)getX(), 10);
        ShapeFill fill;
        Color color = (isEnabled()) ? ENABLED_COLOR : DISABLED_COLOR;
        if ((!mouseOver(circ, input) && !mouseOverLabel(input))
                && !checked) { color = INACTIVE_COLOR; }
        fill = new GradientFill(0, 0, color, lx+(getX()/2),
                    ly+(getY()), color, true);
        if (checked) {
            int shrinkFactor = 4;
            Circle checkedCirc = new Circle(lx+shrinkFactor,
                    ly+shrinkFactor, circ.radius);
            checkedCirc.setRadius(circ.radius-shrinkFactor);
            g.fill(checkedCirc, fill);
        }
        
        g.draw(circ, fill);
        
        if (!label.equals("")) {
            switch (getX()*2) {
                case 16:
                    text.drawSmallText(label,pane.getPosition().x+x+(getX()+
                            (int)circ.radius)
                            +labelPadding,(y+6),color);
                break;
                case 32:
                    text.drawMediumText(label,pane.getPosition().x+x+(getX()+
                            (int)circ.radius)
                            +labelPadding,(y+12),color);
                break;
                case 64:
                    text.drawLargeText(label,pane.getPosition().x+x+(getX()+
                            (int)circ.radius)
                            +labelPadding,(y+24),color);
                break;
            }
        }
    }
    
    /**
     * Sets the action (if applicable) of the button.
     * @param a 
     */
    public final void setAction(Action a) {
        action = a;
    }
    
    /**
     * Sets this button to be selected, and executes the action of this button
     * (if applicable).
     * @param button 
     */
    @Override
    public final void onClick(int button) {
        if (!checked) { checked = true; }
        if (group != null) {
            group.setSelected(id);
        }
        
        if (action != null) {
            action.operation();
        }
    }
    
    /**
     * Returns the {@link RadioGroup} ID of this button.
     * @return 
     */
    public final int getID() {
        return id;
    }
    
    /**
     * Sets the {@link RadioGroup} of this button.
     * @param g 
     */
    protected void setGroup(RadioGroup g) {
        if (g != null) { group = g; }
    }
    
    /**
     * Used internally to determine if the mouse is over the label of this
     * component.
     * @param input
     * @return 
     */
    private boolean mouseOverLabel(Input input) {
        int mouseX = input.getAbsoluteMouseX();
        int mouseY = input.getAbsoluteMouseY();
        int labelWidth = label.length()*getX();
        int labelHeight = getY()*2;
        int labelX = pane.getPosition().x+x+(getX()/3)+labelPadding;
        int labelY = (pane.getPosition().y+y);
        
        if ((mouseX > labelX && mouseX < (labelX+labelWidth))
                && (mouseY > labelY && mouseY < (labelY+labelHeight))) {
            return true;
        }
        
        return false;
    }
}
