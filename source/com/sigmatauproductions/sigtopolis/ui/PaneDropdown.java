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

/**
 * A basic dropdown menu to select from a finite list of values.
 * @author Will
 */
public class PaneDropdown extends PaneComponent {
    
    /**
     * The small-size of a dropdown.
     */
    public static final int SMALL = 16;
    
    /**
     * The medium-size of a dropdown.
     */
    public static final int MEDIUM = 32;
    
    /**
     * The large-size of a dropdown.
     */
    public static final int LARGE = 64;
    
    /**
     * The idle color of this dropdown.
     */
    public static final Color NORMAL_COLOR = new Color(1f, 1f, 1f, 0.5f);
    
    /**
     * The color of this dropdown when the mouse moves over it.
     */
    public static final Color MOUSEOVER_COLOR = Color.white;
    
    /**
     * The internal padding of this component.
     */
    public int padding = 3;
    
    /**
     * The label padding of this component.
     */
    public int labelPadding = 4;
    
    /**
     * The bounding rectangle of the dropdown.
     */
    private Rectangle rect = new Rectangle(0f, 0f, 0f, 0f);
    
    /**
     * The rectangle containing the actual list of items.
     */
    private Rectangle itemsRect = new Rectangle(0f, 0f, 0f, 0f);
    
    /**
     * The separator between the dropdown button and the text within.
     */
    private Line separator = new Line(0f, 0f, 0f, 0f);
    
    /**
     * The parent {@link Pane} of this component.
     */
    private Pane pane;
    
    /**
     * The {@link FontHandler} of this component used for drawing text.
     */
    private FontHandler text;
    
    /**
     * The font of this component, derived from the {@link FontHandler}.
     */
    private Font font;
    
    /**
     * The current color of the component.
     */
    private Color color;
    
    /**
     * The items in the dropdown.
     */
    private String[] items;
    
    /**
     * The index of the currently selected item in the dropdown, defaulted to 0.
     */
    private int selectedItem = 0;
    
    /**
     * The pixel-width (length) of the component.
     */
    private int length = 0;
    
    /**
     * The x-drawing position.
     */
    private int drawX = 0;
    
    /**
     * The y-drawing position.
     */
    private int drawY = 0;
    
    /**
     * Tracks whether or not the items listing should be shown.
     */
    private boolean showItems = false;
    
    /**
     * The label of the component.
     */
    private String label = "";
    
    /**
     * Creates a basic dropdown menu without a label.
     * @param fontSize
     * @param length
     * @param x
     * @param y
     * @param items
     * @param input
     * @param pane
     * @param text 
     */
    public PaneDropdown(int fontSize, int length, int x, int y, String[] items,
            Input input, Pane pane, FontHandler text) {
        super(x, y, input);
        
        this.pane = pane;
        this.text = text;
        this.length = length;
        
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
        
        color = NORMAL_COLOR;
        this.items = items;
        
    }
    
    /**
     * Creates a basic dropdown menu with a label.
     * @param fontSize
     * @param length
     * @param x
     * @param y
     * @param items
     * @param label
     * @param input
     * @param pane
     * @param text 
     */
    public PaneDropdown(int fontSize, int length, int x, int y, String[] items,
            String label, Input input, HalfPane pane, FontHandler text) {
        this(fontSize, length, x, y, items, input, pane, text);
        if (label != null) { this.label = label; }
    }
    
    /**
     * Called once per frame, this updates the state of the dropdown.
     * @param gc
     * @param g
     * @param input 
     */
    @Override
    public final void update(GameContainer gc, Graphics g, Input input) {
        // Click on the dropdown, show items, click anywhere else, hide items
        if (mouseOver(rect, input) && !showItems) {
            if (input.isMousePressed(0)) {
                showItems = true;
            }
        } else {
            if (input.isMousePressed(0) && !mouseOver(itemsRect, input)) {
                showItems = false;
            }
        }
        
        // Click on an item, select it
        if (mouseOver(itemsRect, input) && showItems) {
            if (input.isMouseButtonDown(0)) {
                // Figure out which item we have the mouse over
                int mouseY = input.getAbsoluteMouseY();
                int lowerBound;
                int upperBound;
                for (int i = 0; i < items.length; i++) {
                    lowerBound = (int)(itemsRect.getY()+padding
                            +(font.getLineHeight()*i)+font.getLineHeight());
                    upperBound = lowerBound-font.getLineHeight();
                    if (mouseY >= upperBound && mouseY <= lowerBound) {
                        selectedItem = i;
                        showItems = false;
                    }
                }
            }
        }
        
        // Mouse over or items showing?  Set to a more noticeable color
        if (mouseOver(rect, input) || showItems) {
            color = MOUSEOVER_COLOR;
        } else {
            color = NORMAL_COLOR;
        }
    }
    
    /**
     * Called once per render cycle, this draws the dropdown to the screen.
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
        
        GradientFill itemsBackground = new GradientFill(drawX, drawY, 
                new Color(0f, 0f, 0f, 0.4f), drawX, drawY,
                new Color(0f, 0f, 0f, 0.4f));
        GradientFill fill = new GradientFill(drawX, drawY, color, drawX+length
                +padding, drawY+font.getLineHeight()+padding, color);
        rect = new Rectangle(drawX, drawY,length+padding, font.getLineHeight()
                +padding);
        itemsRect = new Rectangle(drawX, drawY+rect.getHeight(),length+padding,
                ((font.getLineHeight()+padding)*(items.length)));
        int lineDrawX = (int)rect.getX()+(int)rect.getWidth()-
                (int)(((float)rect.getWidth())*0.2f);
        separator = new Line(lineDrawX, rect.getY(), lineDrawX, rect.getY()+
                rect.getHeight());
        font.drawString(lineDrawX+font.getWidth("#"), rect.getY(), "v", color);
        font.drawString(rect.getX()+(padding*2), rect.getY(), 
                items[selectedItem], color);
        
        g.draw(rect, fill);
        g.draw(separator, fill);
        
        if (showItems) {
            g.fill(itemsRect, itemsBackground);
            
            if (mouseOver(itemsRect, input)) {
                Rectangle selectionRectangle = new Rectangle(0f, 0f, 0f, 0f);
                GradientFill selectionFill = new GradientFill(0f, 0f,
                        new Color(1f, 1f, 1f, 0.3f), 0f, 0f,
                        new Color(1f, 1f, 1f, 0.3f));
                int mouseY = input.getAbsoluteMouseY();
                int lowerBound;
                int upperBound;
                
                for (int i = 0; i < items.length; i++) {
                    lowerBound = (int)(itemsRect.getY()+padding
                            +(font.getLineHeight()*i)+font.getLineHeight());
                    upperBound = lowerBound-font.getLineHeight();
                    if (mouseY >= upperBound && mouseY <= lowerBound) {
                        selectionRectangle.setX(itemsRect.getX());
                        selectionRectangle.setY(upperBound);
                        selectionRectangle.setWidth(itemsRect.getWidth());
                        selectionRectangle.setHeight(lowerBound-upperBound);
                        selectionFill.setStart(selectionRectangle.getX(),
                                selectionRectangle.getY());
                        selectionFill.setEnd(selectionRectangle.getX()+
                                selectionRectangle.getWidth(),
                                selectionRectangle.getY()+
                                selectionRectangle.getHeight());
                    }
                }
                g.fill(selectionRectangle, selectionFill);
            }
            
            
            for (int i = 0; i < items.length; i++) {
                font.drawString(itemsRect.getX()+padding,
                        itemsRect.getY()+padding+(font.getLineHeight()*i),
                        items[i], color);
            }
        }
        
        if (!label.equals("")) {
            font.drawString(drawX-font.getWidth(label)-labelPadding, drawY,
                    label, color);
        }
    }
    
    /**
     * Returns the currently selected item.
     * @return 
     */
    public int getSelection() {
        return selectedItem;
    }
    
    /**
     * Returns the list of items.
     * @return 
     */
    public String[] getItems() {
        return items;
    }
    
    /**
     * Returns the label of the dropdown.
     * @param l 
     */
    public void setLabel(String l) {
        label = l;
    }
    
    
}
