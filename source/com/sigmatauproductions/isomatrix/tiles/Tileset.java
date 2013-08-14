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

package com.sigmatauproductions.isomatrix.tiles;

import org.newdawn.slick.*;
import com.sigmatauproductions.isomatrix.*;
import com.sigmatauproductions.isomatrix.util.ConfigFile;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Contains an array of images and their various properties, used to specify the
 * appearance of a {@link TileMap}.
 * 
 * There is a specific order in which the images must occur, and correspond to
 * the types established in {@link SlopeType}.
 *
 * @author sigtau
 */
public final class Tileset {
    
    /**
     * The x-size (width) of the tiles in this set.
     */
    private int tileSizeX = Globals.DEFAULT_TILE_X;
    
    /**
     * The y-size (height) of the tiles in this set.
     */
    private int tileSizeY = Globals.DEFAULT_TILE_Y;
    
    /**
     * The default pixel offset for simulating height with this tileset.
     */
    private final int DEFAULT_HEIGHT_OFFSET = 8;
    
    /**
     * The array that stores the actual tile images.
     */
    private Image[] tiles = new Image[20]; 
    
    /**
     * The actual pixel offset for simulating height with this tileset.
     */
    private int heightOffset;
    
    /**
     * Contains the {@link ConfigFile} used at construction to enter default
     * values.
     */
    private ConfigFile config = null;
    
    /**
     * Stores the proper, user-readable name of the tileset.
     */
    private String name = "";
    
    /**
     * The vertical offset (y-offset) in pixels that all Props 
     * must be raised by in order to be placed correctly onto their anchor.
     */
    private int propOffset = 8;
    
    /**
     * Creates a new tileset using the default tile images and default height
     * offset.
     * 
     * @throws SlickException 
     */
    public Tileset() throws SlickException {
        int max = tiles.length;
        for (int i = 0; i < max; i++) {
            tiles[i] = new Image(Globals.TILESET_DIR
                    + Globals.DEFAULT_TILESET + "/" + (""+(i+1)) + ".png");
        }
        
        tileSizeX = tiles[0].getWidth();
        tileSizeY = tiles[0].getHeight();
        this.heightOffset = DEFAULT_HEIGHT_OFFSET;
        
        readConfig(Globals.TILESET_DIR + Globals.DEFAULT_TILESET);
        
        // Read config file (tileset.txt) line by line and get the prop offset,
        // as well as the proper name of the tileset.
    }
    
    /**
     * 
     * Creates a new tileset from the specified tileset image directory and
     * with the specified height offset.
     * 
     * @param directory
     * @param heightOffset
     * @throws SlickException 
     */
    public Tileset(String directory, int heightOffset) throws SlickException {
        int max = tiles.length;
        for (int i = 0; i < max; i++) {
            tiles[i] = new Image(Globals.TILESET_DIR 
                    + directory + "/" + (""+(i+1)) + ".png");
        }
        
        tileSizeX = tiles[0].getWidth();
        tileSizeY = tiles[0].getHeight();
        this.heightOffset = heightOffset;
        readConfig(Globals.TILESET_DIR+directory);
    }
    
    /**
     * Returns the width (x-size) of all tiles in this tileset.
     * 
     * @return An int containing the tile width.
     */
    public int getTileWidth() {
        return tileSizeX;
    }
    
    /**
     * Returns the height (y-size) of all tiles in this tileset.
     * 
     * @return An int containing the tile height.
     */
    public int getTileHeight() {
        return tileSizeY;
    }
    
    /**
     * 
     * Returns pixel height offset used by this tileset for simulating altitude.
     * 
     * @return 
     */
    public int getHeightOffset() {
        return heightOffset;
    }
    
    /**
     * 
     * Returns the tile image from the specified index.
     * 
     * @param index
     * @return Returns a slick2d Image containing the specified tile image.
     */
    public Image getImage(int index) {
        return tiles[index];
    }
    
    /**
     * Returns the proper name of the tileset according to its tileset.cfg.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the prop offset of the tileset according to its tileset.cfg.
     */
    public int getPropOffset() {
        return propOffset;
    }
    
    /**
     * Used internally to read and parse the tileset.cfg of the tileset.
     */
    private void readConfig(String directory) {
        int pO;
        String n = "";
        
        try {
            config = new ConfigFile(directory + "/tileset.cfg");
            n = config.getValueByProperty("name");
            pO = Integer.parseInt(config.getValueByProperty("propoffset"));
        } catch (FileNotFoundException e) {
            Globals.logWarning("tileset.cfg not found in " + directory +  "/ -"
                    + " assuming default config values");
            n = "Invalid";
            pO = 8;
        } catch (NumberFormatException e) {
            Globals.logWarning("tileset.cfg in tileset directory \""
                    + directory + "\" does not contain proper "
                    + "offset values.  Assuming default offsets.");
            pO = 8;
        }
        
        this.name = n;
        this.propOffset = pO;
    }
}
