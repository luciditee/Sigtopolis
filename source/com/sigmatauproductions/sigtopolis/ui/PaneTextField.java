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
import org.newdawn.slick.*;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.gui.*;

/**
 * A basic component for single-line text entry.  This class wraps carefully
 * around the functionality of Slick2d's TextField.  At the moment, multi-line
 * entry is not supported.  This is planned for a future release.
 * @author Will
 */
public class PaneTextField extends PaneComponent {
    
    /**
     * The small pixel-height of a text field.
     */
    public static final int SMALL = 16;
    
    /**
     * The medium pixel-height of a text field.
     */
    public static final int MEDIUM = 32;
    
    /**
     * The large pixel-height of a text field.
     */
    public static final int LARGE = 64;
    
    /**
     * The normal color of this component.
     */
    public static final Color NORMAL_COLOR = new Color(1f, 1f, 1f, 0.5f);
    
    /**
     * The color of this component when the mouse moves over it.
     */
    public static final Color MOUSEOVER_COLOR = Color.white;
    
    /**
     * The padding of the text area relative to its bounding rectangle.
     */
    public int padding = 3;
    
    /**
     * The padding between the text field and its label.
     */
    public int labelPadding = 5;
    
    /**
     * The actual Slick2d text field accepting input for this component.
     */
    private TextField field;
    
    /**
     * The {@link FontHandler} of this text field, used to draw the text and
     * label.
     */
    private FontHandler text;
    
    /**
     * The bounding rectangle of the text field.
     */
    private Rectangle rect;
    
    /**
     * The outline fill of the bounding rectangle.
     */
    private GradientFill fill;
    
    /**
     * The color of this component.
     */
    private Color color = NORMAL_COLOR;
    
    /**
     * The pane this component is assigned to.
     */
    private Pane pane;
    
    /**
     * Tracks whether or not this component is hidden.
     */
    private boolean hidden = false;
    
    /**
     * The font being used by this component, derived from the FontHandler.
     */
    private Font font = null;
    
    /**
     * The pixel-width (length) of the component.
     */
    private int length;
    
    /**
     * The initial x position of the text field.
     */
    private int initialX = getX();
    
    /**
     * The initial y position of the text field.
     */
    private int initialY = getY();
    
    /**
     * Tracks whether or not we have completed the first frame of this
     * component's lifetime.
     */
    private boolean doneFirstFrame = false;
    
    /**
     * The label of the text field.
     */
    private String label = "";
    
    /**
     * The font size of this text field (used internally).
     */
    private int fontSize = 0;
    
    /**
     * The size of the text field's label.
     */
    private int labelSize = 0;
    
    /**
     * The action performed by this text field when the Enter key is pressed.
     */
    private Action action;
    
    /**
     * Creates a text field without a label.
     */
    public PaneTextField(int fontSize, int length, int x, int y, Input input,
            Pane pane, FontHandler text) {
        super(y, x, input);
        
        this.length = length;
        this.pane = pane;
        this.text = text;
        this.fontSize = fontSize;
        
        switch (fontSize) {
            case 16:
                font = text.getSmallFont();
            break;
            case 32:
                font = text.getMediumFont();
            break;
            case 64:
                font = text.getLargeFont();
            break;
            default:
                font = text.getSmallFont();
            break;
        }
        
    }
    
    /**
     * Creates a text field with a label.
     */
    public PaneTextField(int fontSize, int length, int x, int y, String label,
            Input input, HalfPane pane, FontHandler text) {
        this(fontSize, length, x, y, input, pane, text);
        if (label != null) { this.label = label; }
    }
    
    /**
     * Sets the action of this text field (if applicable).
     * @param a 
     */
    public void setAction(Action a) {
        action = a;
    }
    
    /**
     * <b>This must be called at initialization of the field</b> in order for
     * the field to work properly!  This could not be called from the
     * constructor, so it is up to the programmer to call it in the init()
     * method of the pane.  If you don't do this, you will get a
     * NullPointerException.
     * @param gc 
     */
    public final void init(GameContainer gc) {
        setPosition();
    }
    
    /**
     * Called once per frame, this updates the position and the active
     * status of the text field.
     * @param gc
     * @param g
     * @param input 
     */
    @Override
    public final void update(GameContainer gc, Graphics g, Input input) {
        setPosition();
        
        if (!doneFirstFrame) {
            setPosition();
            this.field = new TextField(gc, font, this.getX(), this.getY(),
                length, font.getLineHeight(), new ComponentListener() {
                    @Override
                    public void componentActivated(AbstractComponent source) {
                        if (action != null) { action.operation(); }
                    }
                });
            this.field.setBackgroundColor(null);
            this.field.setBorderColor(null);
            doneFirstFrame = true;
        } else {
            if (mouseOver(rect, input)) {
                color = MOUSEOVER_COLOR;
            } else {
                color = NORMAL_COLOR;
            }
        }
    }
    
    /**
     * Used internally, this sets the position of the text field.
     */
    private void setPosition() {
        if (!label.equals("")) {
            labelSize = font.getWidth(label);
            this.setX(pane.getPosition().x+initialX+labelPadding
                    +labelSize);
        } else {
            this.setX(pane.getPosition().x+initialX);
        }
        
        this.setY(pane.getPosition().y+initialY);
    }
    
    /**
     * Called once per render cycle, this draws the text field to the screen.
     * @param gc
     * @param g 
     */
    @Override
    public final void draw(GameContainer gc, Graphics g) {
        if (!hidden && doneFirstFrame) {
            rect = new Rectangle((float)getX(), (float)getY(),
                (float)field.getWidth()+padding, (float)font.getLineHeight()
                +padding);
            fill = new GradientFill((float)getX(), (float)getY(), color,
                (float)getX()+(float)rect.getWidth(),
                (float)getY()+(float)rect.getHeight(), color);
            field.render(gc, g);
            g.draw(rect, fill);
            if (!label.equals("")) {
                font.drawString((float)(getX()-labelSize-labelPadding),
                        (float)getY(), label, color);
            }
        }
    }
    
    /**
     * Returns the current text within the text field.
     * @return 
     */
    public final String getText() {
        if (!doneFirstFrame) { return null; }
        return field.getText();
    }
    
    /**
     * Sets whether or not the text field is currently accepting input.
     * @param v 
     */
    public final void setAcceptingInput(boolean v) {
        if (!doneFirstFrame) { return; }
        field.setAcceptingInput(v);
    }
    
    
    
}
