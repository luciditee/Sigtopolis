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
package com.sigmatauproductions.isomatrix.input;

import com.sigmatauproductions.isomatrix.Globals;
import com.sigmatauproductions.isomatrix.util.Transform2D;
import org.newdawn.slick.*;

/**
 * A class for handling mouse input and displaying a cursor image at the mouse's
 * location.
 * @author Will
 */
public final class MouseHandler {
    
    /**
     * The maximum allowable x-size of a cursor image, in pixels.
     */
    public static final int MAX_CURSOR_X = 128;
    
    /**
     * The maximum allowable y-size of a cursor image, in pixels.
     */
    public static final int MAX_CURSOR_Y = 128;
    
    /**
     * The filename (inclusive of the GUI resource folder) of the default
     * cursor.
     */
    public static final String DEFAULT_CURSOR = Globals.GUI_DIR
            + "cursor.png";
    
    /**
     * The filename (exclusive of the GUI resource folder) of the default
     * cursor.
     */
    public static final String DEFAULT_CURSOR_FILENAME = "cursor.png";
    
    /**
     * The cursor image.
     */
    private Image cursorImage;
    
    /**
     * The draw position of the cursor.
     */
    private Transform2D position = new Transform2D();
    
    /**
     * The position of the "clicking" pixel, an offset to the draw position.
     */
    private Transform2D offset = new Transform2D();
    
    /**
     * Tracks whether mouse input is locked.
     */
    private boolean locked = false;
    
    /**
     * Tracks whether the cursor is visible.
     */
    private boolean showCursor = true;
    
    /**
     * Tracks the Slick2D Input variable.
     */
    private Input input;
    
    /**
     * The primary constructor, allowing the specification of a cursor image
     * filename (relative to the GUI resource folder) and a Slick2D Input
     * tracker.
     * @param cursorImage
     * @param i 
     */
    public MouseHandler(String cursorImage, Input i) {
        this.input = i;
        
        // validate that the image isn't null
        if (cursorImage == null) {
            Globals.logWarning("Attempted to load cursorImage, but"
                    + " a null value was encountered in MouseHandler. "
                    + "Using default cursor at " + DEFAULT_CURSOR);
            useDefault();
        }
        
        // If we're here, it's not null.  Use a try-catch block to determine
        // if the non-null image directory string is valid, and if not, use the
        // default.
        try {
            this.cursorImage = new Image(Globals.GUI_DIR + cursorImage);
        } catch (SlickException e) {
            Globals.logWarning("The specified cursor image could not be found. "
                    + "Using the default cursor at " + DEFAULT_CURSOR);
            useDefault();
        }
        
        if (this.cursorImage.getWidth() > MAX_CURSOR_X) {
            Globals.logWarning("The specified cursor image is too wide (greater"
                    + " than " + MAX_CURSOR_X + ".  Using default.");
            useDefault();
        }
        
        if (this.cursorImage.getWidth() > MAX_CURSOR_Y) {
            Globals.logWarning("The specified cursor image is too tall (greater"
                    + " than " + MAX_CURSOR_Y + ".  Using default.");
            useDefault();
        }
        
        Globals.logMessage("Mouse initialized.");
    }
    
    /**
     * A simplified constructor that uses the default cursor instead of one
     * specified by the user.
     * @param i 
     */
    public MouseHandler(Input i) {
        this(DEFAULT_CURSOR_FILENAME, i);
    }
    
    private MouseHandler() {}
    
    /**
     * Returns the screen position of the mouse's "clicker" pixel, i.e the
     * pixel that must be over a button/field/control in order for a click
     * event to be received.
     * @return 
     */
    public Transform2D getPosition() {
        return new Transform2D(position.x+offset.x, position.y+offset.y);
    }
    
    /**
     * Returns the position at which the cursor image is drawn.
     * @return 
     */
    public Transform2D getDrawPosition() {
        return position;
    }
    
    /**
     * Returns offset of the "clicker" pixel, relative to the draw position.
     * @return 
     */
    public Transform2D getOffset() {
        return offset;
    }
    
    /**
     * Draws the cursor to the screen and updates the cursor's position
     * accordingly.
     * @param i 
     */
    public void draw(Input i) {
        if (!locked) {
            position.x = i.getAbsoluteMouseX();
            position.y = i.getAbsoluteMouseY();
            
            if (showCursor) {
                cursorImage.draw(position.x+offset.x, position.y+offset.y);
            }
        }
    }
    
    /**
     * Returns whether or not mouse input is locked.
     * @return 
     */
    public boolean isLocked() {
        return locked;
    }
    
    /**
     * Returns whether or not the mouse has been clicked as of the last frame.
     * @param button Specifies which mouse button has been clicked, starting
     *               with 0.
     * @return {@code true} if the mouse was clicked last frame, {@code false}
     *         if not.
     */
    public boolean hasClicked(int button) {
        if (button < 0 || locked) { return false; }
        boolean ret = this.input.isMousePressed(button);
        
        return ret;
    }
    
    /**
     * Returns whether the specified mouse button is being held down.
     * @param button
     * @return {@code true} if the button is being held, false if otherwise.
     */
    public boolean isHolding(int button) {
        if (button < 0 || locked) { return false; }
        return this.input.isMousePressed(button);
    }
    
    /**
     * Locks (or unlocks) the mouse cursor.  If locked, the MouseHandler will
     * no longer update the position of the cursor, draw the cursor, or accept
     * mouse button input--likewise, the {@code isHolding()} and {@code
     * hasClicked()} methods will return false if this is set to true.
     * @param value 
     */
    public void setLocked(boolean value) {
        locked = value;
    }
    
    /**
     * A method used internally to set the cursor image to the default image.
     */
    private void useDefault() {
        try {
            this.cursorImage = new Image(DEFAULT_CURSOR);
        } catch (SlickException e) {
            Globals.logError("The default cursor image " + DEFAULT_CURSOR
                    + " could not be loaded.", true);
        }
    }
    
    /**
     * Allows the setting of whether or not the cursor should be visible (but
     * not necessarily input-locked).
     * @param value 
     */
    public void setShowCursor(boolean value) {
        showCursor = value;
    }
    
    /**
     * Returns true or false based on whether or not the mouse cursor is
     * allowed to show.
     */
    public boolean isShowing() {
        return showCursor;
    }
    
}
