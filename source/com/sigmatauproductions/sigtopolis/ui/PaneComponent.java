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

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

/**
 * A basic abstract class for all components to be added to a {@link Pane},
 * including buttons, checkboxes, text fields, and so on.
 * @author Will
 */
abstract public class PaneComponent {
    
    /**
     * Tracks whether or not this component is enabled.
     */
    private boolean enabled = true;
    
    /**
     * The y-position (or size, depending on the component) of this component.
     */
    private int yv = 1;
    
    /**
     * The x-position (or size, depending on the component) of this component.
     */
    private int xv = 1;
    
    /**
     * The object of this component that takes input from the user.
     */
    Input input;
    
    /**
     * Creates a basic component.
     * @param height
     * @param width
     * @param input 
     */
    public PaneComponent(int height, int width, Input input) {
        this.yv = height;
        this.xv = width;
        this.input = input;
    }
    
    /**
     * Sets whether or not this component is enabled.
     * @param e 
     */
    public final void setEnabled(boolean e) {
        enabled = e;
    }
    
    /**
     * Returns whether or not this component is enabled.
     * @return 
     */
    public final boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Returns the x-value of this component.
     * @return 
     */
    public final int getX() {
        return xv;
    }
    
    /**
     * Returns the y-value of this component.
     * @return 
     */
    public final int getY() {
        return yv;
    }
    
    /**
     * Sets the x-value of this component.
     * @param x 
     */
    public final void setX(int x) {
        xv = x;
    }
    
    /**
     * Sets the y-value of this component.
     * @param y 
     */
    public final void setY(int y) {
        yv = y;
    }
    
    /**
     * Checks whether or not the mouse is hovering over this component's
     * rectangle.
     * @param rect
     * @param input
     * @return 
     */
    protected final boolean mouseOver(Rectangle rect, Input input) {
        int x = input.getAbsoluteMouseX();
        int y = input.getAbsoluteMouseY();
        
        boolean result = false;
        
        
        if ((x > rect.getX() && x < rect.getX()+rect.getWidth())
                && (y > rect.getY() && y < rect.getY()+rect.getHeight())) {
            result = true;
        }
        
        return result;
    }
    
    /**
     * Checks whether or not the mouse is hovering over this component's circle.
     * @param circ
     * @param input
     * @return 
     */
    protected final boolean mouseOver(Circle circ, Input input) {
        int x = input.getAbsoluteMouseX();
        int y = input.getAbsoluteMouseY();
        
        boolean result = false;
        
        
        if ((x > circ.getX() && x < circ.getX()+circ.getWidth())
                && (y > circ.getY() && y < circ.getY()+circ.getHeight())) {
            result = true;
        }
        
        return result;
    }
    
    /**
     * Called once per frame, this updates the state of the component.
     * @param gc
     * @param g
     * @param i 
     */
    abstract public void update(GameContainer gc, Graphics g, Input i);
    
    /**
     * Called once per render cycle, this draws the component to the screen.
     * @param gc
     * @param g 
     */
    abstract public void draw(GameContainer gc, Graphics g);
}
