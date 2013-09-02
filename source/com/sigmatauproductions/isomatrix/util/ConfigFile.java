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

package com.sigmatauproductions.isomatrix.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A utility class used for reading configuration files.  Configuration files
 * (also known as cfgs or configs) are used for modding, gameplay tweaking, and
 * setting properties of ingame activity so that gameplay can be modified,
 * changed, etc. without the need for modifying the Isomatrix or game code.
 * <p>
 * Syntax is similar to that of .ini files.  Comments are denoted by a ; or a #
 * at the beginning of a line, and properties and their values are separated
 * by equal signs.
 * <p>
 * Here's an example config file:
 * <pre>
 *  # This is a comment and will be ignored by the parser.<br />
 *  ; This is also a comment and will be ignored as well.<br />
 * property1=value
 * property2=3848.39
 * Some Random Property=asdf
 * </pre>
 * 
 * All characters can be used in property names and property values <b>except
 * for the pipe (|) character,</b> which is used later for delimiting between
 * entries.
 * 
 * @author Will
 */
public class ConfigFile {
    
    /**
     * A {@code Scanner} used internally for reading the config file.
     */
    private Scanner scanner;
    
    /**
     * A string array used internally for storing all of the lines in the config
     * file.
     */
    private String[] lines;
    
    /**
     * A string used internally for grabbing the filename of the config file.
     */
    private String filename = "";
    
    /**
     * The default constructor, which opens the config file {@code default.cfg}
     * since no other filename is specified.
     * @throws FileNotFoundException 
     */
    public ConfigFile() throws FileNotFoundException {
        this("default.cfg");
    }
    
    /**
     * Opens and parses the specified config file, storing it in the current
     * instance.
     * @param filename
     * @throws FileNotFoundException 
     */
    public ConfigFile(String filename) throws FileNotFoundException {
        this.filename = filename;
        scanner = new Scanner(new File(filename), "UTF-8");
        StringBuilder builder = new StringBuilder();
            
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.startsWith("#") && !line.startsWith(";")) {
                builder.append(line);
                builder.append("|");
            }
        }
            
        scanner.close();
        lines = builder.toString().split("\\|");
    }
    
    /**
     * Returns the specified line of the file (non-zero-indexed), meaning that
     * line 1 returns the first line, line 2 returns the second line, etc.
     * 
     * Returns null in the event of an invalid line.
     */
    public String getLine(int line) {
        try {
            String ret = lines[line-1];
            return ret;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    
    /**
     * Returns the line number of the specified property by searching the
     * file from top to bottom for the specified property.
     */
    public int getLineByProperty(String property) {
        /*
         * Config files are laid out as such:
         * 
         * property=value
         * property2=value2
         * etc.
         * 
         * Thus, since we have entire lines already stored in lines[], we
         * iterate through these lines until the part behind the equals sign
         * is determined to equal what is specified in the property parameter.
         * 
         * We then return the line number (index + 1 to account for array zero-
         * indexing).
         */
        for (int i = 0; i < lines.length; i++) {
            String[] properties = lines[i].split("\\=");
            if (properties[0].equalsIgnoreCase(property)) {
                return i+1;
            }
        }
        
        // Return -1 if we didn't find the specified property.
        return -1;
    }
    
    /**
     * Returns the specified value of the current line (that is, what comes to
     * the right of the equals sign).  Returns null in the event of an invalid
     * line.
     */
    public String getValue(int line) {
        try {
            String[] data = lines[line-1].split("\\=");
            String ret = data[1];
            return ret;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    
    /**
     * Perhaps the most useful function: returns the value of the specified
     * property by searching the file from top to bottom.  Returns null if the
     * property is never found.
     */
    public String getValueByProperty(String property) {
        return getValue(getLineByProperty(property));
    }
    
    /**
     * Refreshes the config file in case of changes.
     * @throws FileNotFoundException 
     */
    public void refresh() throws FileNotFoundException {
        scanner = new Scanner(new File(this.filename));
        StringBuilder builder = new StringBuilder();
            
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.startsWith("#")) {
                builder.append(line);
                builder.append("|");
            }
        }
            
        scanner.close();
        lines = builder.toString().split("\\|");
    }
    
    /**
     * Returns the filename.
     */
    public String getFilename() {
        return filename;
    }
    
    /**
     * Returns the raw line data in the form of a string array.
     */
    public String[] getLines() {
        return lines;
    }
}
