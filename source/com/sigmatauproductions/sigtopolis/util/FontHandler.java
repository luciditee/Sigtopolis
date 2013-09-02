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
package com.sigmatauproductions.sigtopolis.util;

import com.sigmatauproductions.isomatrix.Globals;
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.*;

/**
 * A class used to display text at fixed sizes of 16px, 32px, or 64px.  Used
 * in conjunction with Slick2d's {@code UnicodeFont} class to make a fully-
 * translatable game possible.
 * 
 * @author Will
 */
public class FontHandler {
    
    /**
     * The 64px font.
     */
    private UnicodeFont largeFont;
    
    /**
     * The 32px font.
     */
    private UnicodeFont mediumFont;
    
    /**
     * The 16px font.
     */
    private UnicodeFont smallFont;
    
    /**
     * Initializes the font found in the specified directory, usually specified
     * by the game's current language (if applicable).
     * 
     * @param fontDir 
     */
    public FontHandler(String fontDir) {
        try {
            largeFont = new UnicodeFont(fontDir + "font.ttf", 64, false, false);
            largeFont.addAsciiGlyphs();
            largeFont.getEffects().add(new ColorEffect());
            largeFont.loadGlyphs();
            mediumFont = new UnicodeFont(fontDir + "font.ttf", 32, false, false);
            mediumFont.addAsciiGlyphs();
            mediumFont.getEffects().add(new ColorEffect());
            mediumFont.loadGlyphs();
            smallFont = new UnicodeFont(fontDir + "font.ttf", 16, false, false);
            smallFont.addAsciiGlyphs();
            smallFont.getEffects().add(new ColorEffect());
            smallFont.loadGlyphs();
        } catch (SlickException e) {
            Globals.logError("SlickException in FontHandler constructor: "
                    + e.getMessage(), true);
        }
    }
    
    /**
     * Draws a 64px-sized string of text to the screen at the specified
     * coordinates using the specified color.
     * @param text
     * @param x
     * @param y
     * @param color 
     */
    public void drawLargeText(String text, int x, int y, Color color) {
        if (text == null) { return; }
        largeFont.drawString((float)x,(float)y,text,color);
    }
    
    /**
     * Draws a 32px-sized string of text to the screen at the specified
     * coordinates using the specified color.
     * @param text
     * @param x
     * @param y
     * @param color 
     */
    public void drawMediumText(String text, int x, int y, Color color) {
        if (text == null) { return; }
        mediumFont.drawString((float)x,(float)y,text,color);
    }
    
    /**
     * Draws a 16px-sized string of text to the screen at the specified
     * coordinates using the specified color.
     * @param text
     * @param x
     * @param y
     * @param color 
     */
    public void drawSmallText(String text, int x, int y, Color color) {
        if (text == null) { return; }
        smallFont.drawString(x,y,text,color);
    }
    
    /**
     * Gets the 16px-font width of the specified string.
     * @param text
     * @return 
     */
    public int getSmallWidth(String text) {
        return smallFont.getWidth(text);
    }
    
    /**
     * Gets the 32px-font width of the specified string.
     * @param text
     * @return 
     */
    public int getMediumWidth(String text) {
        return mediumFont.getWidth(text);
    }
    
    /**
     * Gets the 64px-font width of the specified string.
     * @param text
     * @return 
     */
    public int getLargeWidth(String text) {
        return largeFont.getWidth(text);
    }
    
    /**
     * Returns a reference to the 16px font.
     * @return 
     */
    public Font getSmallFont() {
        return smallFont;
    }
    
    /**
     * Returns a reference to the 16px font.
     * @return 
     */
    public Font getMediumFont() {
        return mediumFont;
    }
    
    /**
     * Returns a reference to the 16px font.
     * @return 
     */
    public Font getLargeFont() {
        return largeFont;
    }
    
    /**
     * Necessary to be done at startup, this method loads the glyphs found
     * in a language file to be displayed on the screen (in order to ensure
     * the correct Unicode characters are readily available).
     * @return 
     */
    public void processLanguage(String[] lines) throws SlickException {
        for (String line : lines) {
            largeFont.addGlyphs(line);
            mediumFont.addGlyphs(line);
            smallFont.addGlyphs(line);
        }
        
        largeFont.loadGlyphs();
        mediumFont.loadGlyphs();
        smallFont.loadGlyphs();
    }
    
}
