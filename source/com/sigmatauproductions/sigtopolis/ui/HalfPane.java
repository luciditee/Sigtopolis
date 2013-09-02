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
import org.newdawn.slick.*;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

/**
 * Primarily used on the main menu screen, a {@code HalfPane} occupies the right
 * half of the screen and is used for adjusting pregame settings, in-game
 * options, and loading savegames.
 * @author Will
 */
public final class HalfPane extends Pane {
    
    /**
     * The title to be shown on the HalfPane.
     */
    private String title;
    
    /**
     * The {@link IsomatrixState} this pane will be assigned to.
     */
    private IsomatrixState state;
    
    /**
     * The {@link FontHandler} used for displaying text.
     */
    private FontHandler text;
    
    /**
     * Tracks whether or not this pane is hidden.
     */
    private boolean hidden = false;
    
    /**
     * The background rectangle of this pane.
     */
    private Rectangle backgroundRect =  new Rectangle(0f, 0f, 0f, 0f);
    
    /**
     * The object used for handling user input.
     */
    private Input input;
    
    /**
     * The text/component padding used in this pane.
     */
    private int padding = 10;

    /**
     * Initializes a new pane, given the title, assigned state, font handler,
     * and input object.
     * @param title
     * @param state
     * @param text
     * @param input 
     */
    public HalfPane(String title, IsomatrixState state, FontHandler text,
            Input input) {
        this.title = title;
        this.state = state;
        this.text = text;
        this.input = input;
    }

    private HalfPane() {
    }
    
    /**
     * Called once per frame, this updates all components within the pane.
     * @param gc
     * @param g 
     */
    public void update(GameContainer gc, Graphics g) {
        for (PaneComponent component : components) {
            component.update(gc, g, input);
        }
    }
    
    /**
     * Called once per render cycle, this draws the pane and its components.
     * @param gc
     * @param g
     * @param input 
     */
    public void draw(GameContainer gc, Graphics g, Input input) {
        int extra = (gc.getWidth() > 800) ? gc.getWidth() / 8 : 0;
        extra -= padding;
        if (!hidden) {
            // Draw the base rectangle
            backgroundRect = new Rectangle(gc.getWidth() / 2f
                    + (padding / 2) - extra, padding, (float) (gc.getWidth() / 2f
                    - (padding) + extra),
                    (float) (gc.getHeight() - (padding * 2f)));
            ShapeFill backgroundFill = new GradientFill(0, 0,
                    new Color(0.0f, 0.0f, 0.0f, 0.4f), backgroundRect.getX()
                    + backgroundRect.getWidth() / 2, gc.getHeight(),
                    new Color(0.0f, 0.0f, 0.0f, 1.0f), true);
            g.fill(backgroundRect, backgroundFill);

            // Draw the frame title
            text.drawMediumText(title, (int) (backgroundRect.getX() + padding),
                    (int) (backgroundRect.getY() + padding), Color.white);
            for (PaneComponent component : components) {
                component.draw(gc, g);
            }
        }
    }
    
    /**
     * Sets the title of the pane to the specified value.
     * @param t 
     */
    public void setTitle(String t) {
        title = t;
    }
    
    /**
     * Sets whether or not the pane is hidden from view.
     * @param h 
     */
    public void setHidden(boolean h) {
        hidden = h;
    }

    /**
     * Returns whether or not the pane is hidden from view.
     * @return 
     */
    public boolean isHidden() {
        return hidden;
    }
    
    /**
     * Returns the position of the pane in screen-space.
     * @return 
     */
    @Override
    public Transform2D getPosition() {
        return new Transform2D((int) backgroundRect.getX(),
                (int) backgroundRect.getY());
    }
    
    /**
     * Returns the width of the pane.
     * @return 
     */
    @Override
    public final int getWidth() {
        return (int)backgroundRect.getWidth();
    }
    
    /**
     * Returns the text/component padding in the x and y directions for this
     * pane.
     * @return 
     */
    @Override
    public final Transform2D getPadding() {
        return new Transform2D(padding, padding);
    }
    
}
