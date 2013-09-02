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
 * A spinner for inputting a numeric value within a specified range.
 * @author Will
 */
public class PaneSpinner extends PaneComponent implements Clickable {
    
    /**
     * A small pixel-sized spinner.
     */
    public static final int SMALL = 16;
    
    /**
     * A medium pixel-sized spinner.
     */
    public static final int MEDIUM = 32;
    
    /**
     * A large pixel-sized spinner.
     */
    public static final int LARGE = 64;
    
    /**
     * The normal color of this spinner.
     */
    public static final Color NORMAL_COLOR = new Color(1f, 1f, 1f, 0.5f);
    
    /**
     * The color of this spinner when the mouse moves over it.
     */
    public static final Color MOUSEOVER_COLOR = Color.white;
    
    /**
     * The amount of padding (in pixels) between the spinner and its label.
     */
    public int labelPadding = 3;
    
    /**
     * The font of this spinner, as derived from the FontHandler.
     */
    private Font font;
    
    /**
     * The {@link FontHandler} of the spinner, used for drawing text onto
     * the screen.
     */
    private FontHandler text;
    
    /**
     * The pane this spinner is assigned to.
     */
    private Pane pane;
    
    /**
     * The x-position of the spinner.
     */
    private int drawX = 0;
    
    /**
     * The y-position of the spinner.
     */
    private int drawY = 0;
    
    /**
     * The main (text-area) portion of the spinner.
     */
    private Rectangle mainRect = new Rectangle(0f, 0f, 0f, 0f);
    
    /**
     * The up-arrow portion of the spinner.
     */
    private Rectangle upRect = new Rectangle(0f, 0f, 0f, 0f);
    
    /**
     * The down-arrow portion of the spinner.
     */
    private Rectangle downRect = new Rectangle(0f, 0f, 0f, 0f);
    
    /**
     * The pixel-width (length) of the spinner.
     */
    private int length = 0;
    
    /**
     * The minimum value of the spinner.
     */
    private int minimum = 0;
    
    /**
     * The maximum value of the spinner.
     */
    private int maximum = 255;
    
    /**
     * The current value of the spinner.
     */
    private int value = 0;
    
    /**
     * The padding of the text-area within the spinner.
     */
    private int textPadding = 4;
    
    /**
     * The current color of the spinner.
     */
    private Color color = NORMAL_COLOR;
    
    /**
     * The font-size of the spinner.
     */
    private int fontSize = 16;
    
    /**
     * Determines whether or not the up arrow was clicked.
     */
    private boolean clickedUp = false;
    
    /**
     * Determines whether or not the down arrow was clicked.
     */
    private boolean clickedDown = false;
    
    /**
     * The amount of time elapsed since the last frame.
     */
    private int delta = 1;
    
    /**
     * The amount of time elapsed since the initial click on either the up or 
     * down arrow buttons.
     */
    private int repeatTimer = 0;
    
    /**
     * Tracks whether or not we need to check which button was clicked.
     */
    private boolean doClickCheck = true;
    
    /**
     * The text field within the spinner.
     */
    private TextField textField;
    
    /**
     * Tracks whether or not the first frame has occurred in this spinner's
     * lifetime.
     */
    private boolean firstFrame = false;
    
    /**
     * The last value of the spinner before the user began typing in the
     * text area.
     */
    private int lastValue = 0;
    
    /**
     * The label of the spinner.
     */
    private String label = "";
    
    /**
     * Creates a basic spinner with the default range of (0, 255) and no label.
     */
    public PaneSpinner(int fontSize, int length, int x, int y, int startingVal,
            Input input, Pane pane, FontHandler text) {
        super(x, y, input);
        this.pane = pane;
        this.text = text;
        this.length = Math.abs(length);
        this.fontSize = fontSize;
        
        this.value = startingVal;
        
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
     * Creates a spinner with a custom range and no label.
     */
    public PaneSpinner(int fontSize, int length, int x, int y, int startingVal,
            int min, int max, Input input, HalfPane pane, FontHandler text) {
        this(fontSize, length, x, y, startingVal, input, pane, text);
        this.minimum = min;
        this.maximum = max;
    }
    
    /**
     * Creates a spinner with the default range (0-255) and a label.
     */
    public PaneSpinner(int fontSize, int length, int x, int y, int startingVal,
            String label, Input input, HalfPane pane, FontHandler text) {
        this(fontSize, length, x, y, startingVal, input, pane, text);
        this.label = label;
    }
    
    /**
     * Creates a spinner with a custom range and a label.
     */
    public PaneSpinner(int fontSize, int length, int x, int y, int startingVal,
            int min, int max, String label, Input input, HalfPane pane,
            FontHandler text) {
        this(fontSize, length, x, y, startingVal, label, input, pane, text);
        this.minimum = min;
        this.maximum = max;
    }
    
    /**
     * Updates the delta-time value of this spinner.  Call this once per frame
     * in the {@code update()} function of your pane.
     * @param delta 
     */
    public final void updateDelta(int delta) {
        this.delta = delta;
    }
    
    /**
     * Called once per frame, this updates the state of the spinner.
     * @param gc
     * @param g
     * @param input 
     */
    @Override
    public final void update(GameContainer gc, Graphics g, Input input) {
        if (!firstFrame) {
            textField = new TextField(gc, font, 0, 0, length,
                    font.getLineHeight(), new ComponentListener() {
                @Override
                public void componentActivated(AbstractComponent source) {
                    try {
                        int newValue = Integer.parseInt(textField.getText());
                        value = validateValue(newValue);
                    } catch (NumberFormatException e) {
                        value = lastValue;
                    }
                    textField.setFocus(false);
                }
            });
            textField.setBackgroundColor(null);
            textField.setBorderColor(null);
            textField.setText(Integer.toString(value));
            firstFrame = true;
        }
        
        textField.setTextColor(color);
        
        if (!textField.hasFocus()) {
            try {
                lastValue = Integer.parseInt(textField.getText());
            } catch (NumberFormatException e) {}
            textField.setText(Integer.toString(value));
        }
        
        
        
        if (mouseOver(upRect, input) || mouseOver(downRect, input)
                || mouseOver(mainRect, input)) {
            color = MOUSEOVER_COLOR;
        } else {
            color = NORMAL_COLOR;
        }
        
        if (mouseOver(upRect, input) && input.isMouseButtonDown(0)) {
            doClickCheck = false;
            if (repeatTimer >= 500) {
                increment();
            } else {
                repeatTimer += delta;
                doClickCheck = true;
            }
        } else if (mouseOver(downRect, input) && input.isMouseButtonDown(0)) {
            doClickCheck = false;
            if (repeatTimer >= 500) {
                decrement();
            } else {
                repeatTimer += delta;
                doClickCheck = true;
            }
        } else {
            repeatTimer = 0;
        }
        
        if (doClickCheck) {
            checkDownButton(input);
            checkUpButton(input);
        }
        
    }
    
    /**
     * Called once per render cycle, this draws the spinner to the screen.
     * @param gc
     * @param g 
     */
    @Override
    public final void draw(GameContainer gc, Graphics g) {
        if (label.equals("")) {
            drawX = getY()+pane.getPosition().x;
        } else {
            drawX = getY()+pane.getPosition().x+font.getWidth(label)
                    +labelPadding;
        }
        
        drawY = getX()+pane.getPosition().y;
        
        GradientFill fill = new GradientFill(drawX, drawY, color,
                drawX+length+textPadding,drawY+font.getLineHeight()+textPadding,
                color);
        mainRect = new Rectangle(drawX, drawY, length+textPadding,
                font.getLineHeight()+textPadding);
        upRect = new Rectangle(drawX+length+textPadding+2, drawY-(textPadding/2)+1,
                font.getWidth("##"),
                font.getLineHeight()/2);
        downRect = new Rectangle(drawX+length+textPadding+2, drawY+(upRect.getHeight())+2,
                font.getWidth("##"),
                font.getLineHeight()/2);
        
        font.drawString(upRect.getX()+font.getWidth("^"), upRect.getY()-fontSize/4, "^", color);
        font.drawString(downRect.getX()+((font.getWidth("v")/3)*3), downRect.getY()-fontSize/2, "v", color);
        
        if (firstFrame) {
            textField.setLocation(drawX, drawY);
            textField.render(gc, g);
        }
        
        if (!label.equals("")) {
            font.drawString(drawX-font.getWidth(label)-labelPadding, drawY,
                    label, color);
        }
            
        g.draw(mainRect, fill);
        g.draw(upRect, fill);
        g.draw(downRect, fill);
        
    }
    
    /**
     * Checks whether or not the up button has been clicked.  Used internally.
     * @param i 
     */
    private void checkUpButton(Input i) {
        if (i.isMouseButtonDown(0) && (mouseOver(upRect, i))
                && !clickedUp) {
            clickedUp = true;
        }
        
        if (!i.isMouseButtonDown(0) && clickedUp) {
            onClick(0);
            clickedUp = false;
        }
    }
    
    /**
     * Checks whether or not the down button has been clicked.  Used internally.
     * @param i 
     */
    private void checkDownButton(Input i) {
        if (i.isMouseButtonDown(0) && (mouseOver(downRect, i))
                && !clickedDown) {
            clickedDown = true;
        }
        
        if (!i.isMouseButtonDown(0) && clickedDown) {
            onClick(1);
            clickedDown = false;
        }
    }
    
    /**
     * Called when a button has been clicked on with the parameter set to 0 for
     * up, and 1 for down.
     * @param value 
     */
    @Override
    public final void onClick(int value) {
        switch (value) {
            case 0: increment();
            break;
            case 1: decrement();
            break;
        }
    }
    
    /**
     * Safely increments the spinner.
     */
    private void increment() {
        value = ((value+1) <= maximum) ? value+1 : value;
    }
    
    /**
     * Safely decrements the spinner.
     */
    private void decrement() {
        value = ((value-1) >= minimum) ? value-1 : value;
    }
    
    /**
     * Validates that the current value is within the range constraints.
     * @param v
     * @return 
     */
    private int validateValue(int v) {
        if (v > maximum) { return maximum; }
        if (v < minimum) { return minimum; }
        return v;
    }
    
    /**
     * Returns the current numeric value of the spinner.
     * @return 
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Sets the spinner value to be equal to the specified value (after
     * validation).
     * @param v 
     */
    public void setValue(int v) {
        value = validateValue(v);
    }
    
    /**
     * Sets the label of the spinner.
     * @param l 
     */
    public void setLabel(String l) {
        if (l != null) { label = l; }
    }
    
}
