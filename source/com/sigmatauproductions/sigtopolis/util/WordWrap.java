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

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Font;

/**
 * A static class for word-wrapping a string into an array of lines that can
 * later be drawn onto the screen using a {@link FontHandler} or similar.
 * @author Will
 */
public final class WordWrap {
    private WordWrap() {}
    
    /**
     * Returns an array of lines containing the specified string, however broken
     * up into the necessary number of lines at the necessary lengths for word
     * wrap to occur.
     * @param o The string to wrap.
     * @param font The font to reference when calculating the final result.
     * @param widthLimit The width limit to wrap within.
     * @return 
     */
    public static String[] getLines(String o, Font font, int widthLimit) {
        String original = o;
        String[] words = original.split(" ");
        List<String> lines = new ArrayList<>();
        int spaceWidth = font.getWidth(" ");
        
        int currentLineWidth = 0;
        String currentLine = "";
        for (int i = 0; i < words.length; i++) {
            int currentWordWidth = font.getWidth(words[i]);
            currentLineWidth += currentWordWidth;
            currentLineWidth += spaceWidth;
            
            if (currentLineWidth >= widthLimit) {
                lines.add(currentLine);
                currentLineWidth = 0;
                currentLine = "";
            }
        
            currentLine += words[i];
            currentLine += " ";
        }
        
        if (!currentLine.trim().equals("")) {
            lines.add(currentLine);
        }
        
        return lines.toArray(new String[lines.size()]);
    }
}
