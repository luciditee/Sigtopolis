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

import com.sigmatauproductions.isomatrix.Globals;
import com.sigmatauproductions.isomatrix.game.IsomatrixState;
import com.sigmatauproductions.isomatrix.util.Transform2D;
import com.sigmatauproductions.sigtopolis.util.FontHandler;
import org.newdawn.slick.*;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

/**
 * Normally occupying the left half of the screen, this type of menu is used
 * on the main menu screen.
 * @author Will
 */
public final class TitleMenu {
    
    /**
     * The list of available values in this menu.
     */
    private String[] values;
    
    /**
     * The list of text offsets in this menu.
     */
    private int[] offsets;
    
    /**
     * The list of which values are selectable.
     */
    private boolean[] selectables;
    
    /**
     * The list of action codes for each value.
     */
    private int[] action;
    
    /**
     * The state this menu exists within.
     */
    private IsomatrixState state;
    
    /**
     * The FontHandler used to render the text of this menu.
     */
    private FontHandler text;
    
    /**
     * Tracks whether or not this menu is hidden.
     */
    private boolean hidden = false;
    
    /**
     * Tracks which menu item had the mouse over it last.
     */
    private int selected = 0;
    
    /**
     * Tracks whether or not this menu is "locked" (unusable).
     */
    private boolean locked = false;
    
    /**
     * Tracks whether or not the mouse is usable on this menu.
     */
    private boolean enableMouse = true;
    
    /**
     * Tracks the current action code of this menu.
     */
    private int currentAction = -1;
    
    /**
     * Tracks the last known state of {@code boolean hidden}.
     */
    private boolean lastHiddenState = hidden;
    
    /**
     * Tracks whether or not we are playing the slide animation.
     */
    private boolean doSlide = false;
    
    /**
     * Tracks the amount of time elapsed since the last frame.
     */
    private int delta = 0;
    
    /**
     * Tracks how complete the sliding animation is.
     */
    private float completion = 0f;
    
    /**
     * Tracks whether the mouse was clicked on this menu.
     */
    private boolean clicked = false;
    
    /**
     * Tracks what the mouse is currently hovering over.
     */
    private int currentMouseover = -1;
    
    /**
     * Creates a new title menu.
     */
    // TODO: Throw new IllegalArgumentException when the lengths aren't equal
    public TitleMenu(String[] values, int[] offsets, boolean[] selectables,
            int[] action, IsomatrixState state, FontHandler text) {

        this.values = values;
        this.offsets = offsets;
        this.selectables = selectables;
        this.action = action;
        this.state = state;
        this.text = text;

        setSelected(0);
    }
    
    /**
     * Called once per frame, this updates the title menu's delta value.
     * @param d
     * @param input 
     */
    public void update(int d, Input input) {
        delta = d;
    }
    
    /**
     * Called once per render cycle, this draws the title menu to the screen.
     * @param level
     * @param gc
     * @param g
     * @param input 
     */
    public void draw(int level, GameContainer gc, Graphics g, Input input) {
        currentAction = -1;
        int width = 175;
        int appearPosition = (20*(level+1)) + (level * width);
        int position = -300;
        float speed = 5.0f;
        
        if (!hidden && lastHiddenState) {
            doSlide = true;
        }
        
        if (hidden && !lastHiddenState) {
            doSlide = false;
            completion = 0f;
        }
        
        if (doSlide) {
            completion += (((float)delta)/1000f)*speed;
            position = (int)Globals.lerp((float)position,
                    (float)appearPosition, completion);
            if (completion >= 1.0f) {
                doSlide = false;
                completion = 0f;
            }
        }
        
        if (!doSlide) { position = appearPosition; }
        
        if (!hidden) {
            Transform2D mousePos = new Transform2D(
                    input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
            
            
            int spacing = 47;
            int y = 1;
            Rectangle backgroundRect = new Rectangle(position, 0, width,
                    gc.getHeight());
            ShapeFill backgroundFill = new GradientFill(0, 0,
                    new Color(0.0f, 0.0f, 0.0f, 0.3f), backgroundRect.getX()
                    + backgroundRect.getWidth() / 2, gc.getHeight(),
                    new Color(0.0f, 0.0f, 0.0f, 1.0f), true);
            g.fill(backgroundRect, backgroundFill);
            for (int i = 0; i < values.length; i++) {
                text.drawMediumText(values[i], ((int) backgroundRect.getX())+15,
                        y + offsets[i], new Color(1.0f, 1.0f, 1.0f));
                y += spacing + offsets[i];
                if (i == selected && !locked) {
                    Rectangle selectionRect = new Rectangle(
                            (float) backgroundRect.getX(),
                            (float) (y-spacing), backgroundRect.getWidth(), 48);
                    ShapeFill selectionFill = new GradientFill(selectionRect
                            .getCenterX(), selectionRect.getMaxY(),
                            new Color(1.0f, 1.0f, 1.0f, 0.3f),
                            selectionRect.getCenterX(), selectionRect.getMinY(),
                            new Color(1.0f, 1.0f, 1.0f, 0.3f));
                    g.fill(selectionRect, selectionFill);
                }
                
                if (mousePos.x > backgroundRect.getX() && mousePos.x < (
                        backgroundRect.getX()+backgroundRect.getWidth())) {
                    if (mousePos.y > (y-spacing) && mousePos.y < ((y-spacing)
                            +48)) {
                        if (enableMouse) {
                            setSelected(i);
                            currentMouseover = getSelected();
                            if (isSelectable(i)) {
                                if (input.isMouseButtonDown(0) && !clicked) {
                                    clicked = true;
                                }
                                
                                if (!input.isMouseButtonDown(0) && clicked) {
                                    currentAction = action[i];
                                    clicked = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        lastHiddenState = hidden;
    }
    
    /**
     * Prevents blind initialization (i.e {@code new TitleMenu().whatever()})
     * of a TitleMenu.
     */
    private TitleMenu() {
    }
    
    /**
     * Returns whether or not the specified menu item is selectable.
     * @param index
     * @return 
     */
    public boolean isSelectable(int index) {
        if (index < 0 || index >= selectables.length) {
            return false;
        }
        return selectables[index];
    }

    /**
     * Returns the action code of the specified index.
     * @param index
     * @return 
     */
    public int getAction(int index) {
        if (index < 0 || index > action.length) {
            return -1;
        }
        return action[index];
    }
    
    /**
     * Forces the current action code to be the specified code.
     * @param action 
     */
    public void forceAction(int action) {
        currentAction = action;
    }
    
    /**
     * Sets the menu to be hidden (or not).
     * @param h 
     */
    public void setHidden(boolean h) {
        lastHiddenState = hidden;
        hidden = h;
        locked = h;
    }
    
    /**
     * Returns whether or not the menu is hidden.
     * @return 
     */
    public boolean isHidden() {
        return hidden;
    }
    
    /**
     * Returns whether or not the menu is locked (unusable);
     * @return 
     */
    public boolean isLocked() {
        return locked;
    }
    
    /**
     * Lock the menu (render it unusable, but still able to be made visible).
     * @param l 
     */
    public void setLocked(boolean l) {
        locked = l;
    }
    
    /**
     * Set the currently selected menu item to the specified index.
     * @param index 
     */
    public void setSelected(int index) {
        if (locked) {
            return;
        }
        if (index < 0) {
            //System.out.println("selected="+(selectables.length-1));
            setSelected(selectables.length - 1);
            return;
        }
        if (index >= selectables.length) {
            //System.out.println("selected=0");
            setSelected(0);
            return;
        }

        int i = index;
        if (selectables[i]) {
            selected = i;
            //System.out.println("selected="+i);
        } else {
           setSelected(i+1);
        }
    }
    
    /**
     * Gets the index of the currently selected item.
     * @return 
     */
    public int getSelected() {
        return selected;
    }

    /**
     * Increments the currently selected item.
     */
    public void incrementSelected() {
        setSelected(selected + 1);
    }

    /**
     * Decrements the currently selected item.
     */
    public void decrementSelected() {
        setSelected(selected - 1);
    }
    
    /**
     * Gets the current action code of the menu.
     * @return 
     */
    public int getCurrentAction() {
        return currentAction;
    }
    
    /**
     * Enables the mouse for this menu.
     * @param m 
     */
    public void setMouseEnabled(boolean m) {
        enableMouse = m;
    }
    
    /**
     * Returns whether or not the mouse can be used on this menu.
     * @return 
     */
    public boolean isMouseEnabled() {
        return enableMouse;
    }
    
    /**
     * Returns which menu item the mouse is currently hovering over.
     * @return 
     */
    public int getMouseover() {
        return currentMouseover;
    }
}
