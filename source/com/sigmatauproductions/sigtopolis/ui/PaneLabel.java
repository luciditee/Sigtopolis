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

import com.sigmatauproductions.sigtopolis.util.WordWrap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * A basic component for drawing simple text labels (with word wrap) onto a
 * pane.  If you want to label a component, use the component's built-in label
 * constructor instead.
 * @author Will
 */
public class PaneLabel extends PaneComponent {
    
    /**
     * The x-position of the label.
     */
    private int x = 0;
    
    /**
     * The y-position of the label.
     */
    private int y = 0;
    
    /**
     * The pane this label is assigned to.
     */
    private Pane pane;
    
    /**
     * The text of the label.
     */
    private String label = "";
    
    /**
     * The font used by this label.
     */
    private Font text;
    
    /**
     * The color of this label.
     */
    public static final Color COLOR = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    
    /**
     * Creates a new label.
     */
    public PaneLabel(String label, int x, int y, Pane pane, Input input,
            Font font) {
        super(x,y,input);
        this.label = label;
        this.x = x;
        this.y = y;
        this.pane = pane;
        this.text = font;
    }
    
    /**
     * Called once per frame... this method does nothing, but is required to be
     * considered a component.
     */
    @Override
    public final void update(GameContainer gc, Graphics g, Input i) {}
    
    /**
     * Called once per render cycle, this draws the label to the screen (with
     * word wrap taken into consideration).
     * @param gc
     * @param g 
     */
    @Override
    public final void draw(GameContainer gc, Graphics g) {
        int drawX = pane.getPosition().x+x+pane.getPadding().x;
        int drawY = pane.getPosition().y+y+pane.getPadding().y;
        String[] lines = WordWrap.getLines(label, text, pane.getWidth()-(
                pane.getPadding().x*8));
        int currentY = drawY;
        for (int i = 0; i < lines.length; i++) {
            text.drawString(drawX, currentY, lines[i], COLOR);
            currentY += text.getLineHeight();
        }
    }
    
}
