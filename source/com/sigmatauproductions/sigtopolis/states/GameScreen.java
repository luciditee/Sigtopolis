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
package com.sigmatauproductions.sigtopolis.states;

import com.sigmatauproductions.isomatrix.Globals;
import com.sigmatauproductions.isomatrix.game.*;
import com.sigmatauproductions.isomatrix.props.Prop;
import com.sigmatauproductions.isomatrix.tiles.*;
import com.sigmatauproductions.isomatrix.util.*;
import com.sigmatauproductions.sigtopolis.util.FontHandler;
import java.io.*;
import java.lang.management.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.newdawn.slick.*;

/**
 * The code for the game itself, handling gameplay, navigation, placement of
 * objects on the map, and interaction between those objects.
 * @author Will
 */
public final class GameScreen extends IsomatrixState {
    
    /**
     * The minimum scale used by the game's {@link Graphics} object.
     */
    public static final float minScale = .2f;
    
    /**
     * The maximum scale used by the game's {@link Graphics} object.
     */
    public static final float maxScale = 1f;
    
    /**
     * The amount added or subtracted to the zoom level when zooming in or out.
     */
    public static final float scaleStepSize = .2f;
    
    /**
     * An internal handler for the game's isomatrix.cfg file.
     */
    private ConfigFile strings;
    
    /**
     * A reference to the {@link FontHandler} used to draw text on-screen.
     */
    private FontHandler text;
    
    /**
     * A reference to the {@code Input} object used for accepting input.
     */
    private Input input;
    
    /**
     * A reference to the {@code Graphics} object used by the game.
     */
    private Graphics g = null;
    
    /**
     * Tracks whether or not the game is ready to start using its
     * {@code update} and {@code render} methods.
     */
    private boolean ready = false;
    
    /**
     * The current map to be displayed by the game.
     */
    private TileMap map;
    
    /**
     * The last-known x coordinate of the mouse.
     */
    private int lastMouseX = 0;
    
    /**
     * The last-known Y coordinate of the mouse.
     */
    private int lastMouseY = 0;
    
    /**
     * Tracks whether or not we invert the right-click scroll feature.
     */
    private boolean invertRightClickScroll = true;
    
    /**
     * A reference to the {@code GameContainer} object used by the game.
     */
    private GameContainer gc;
    
    /**
     * The current scale of the {@link TileMap}.
     */
    private float scale = 1f;
    
    /**
     * The number of milliseconds elapsed since the last frame began.
     */
    private int delta = 0;
    
    /**
     * Tracks which tile we last moved the mouse over.
     */
    private int lastTileToColor = -1;
    
    /**
     * Tracks whether or not we display the mouseover effect for moving the
     * mouse over a tile.
     */
    private boolean doMouseover = true;
    
    /**
     * Initializes a new session, taking the current config file, input
     * variable, and {@link IsomatrixGame} used to switch between game states.
     * 
     * @param game
     * @param strings
     * @param input
     * @throws SlickException 
     */
    public GameScreen(IsomatrixGame game, ConfigFile strings, Input input)
            throws SlickException {
        super("Sigtopolis Session", game);
        Globals.logMessage("Creating game screen instance.");
        this.input = input;
        this.strings = strings;
        String fontDir = strings.getValueByProperty("font");
        if (fontDir == null) {
            Globals.logError("The font is not specified in the current "
                    + "language file " + strings.getFilename(), true);
        }

        text = new FontHandler(Globals.GUI_DIR + "fonts/" + fontDir + "/");
        text.processLanguage(strings.getLines());

        // TODO: Remove this and generate a new map only when told to.
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        long uptime = rb.getUptime();
        generateMap(128, 128, 0, 9, "temperate", 60, 10, 50, 20);
        long newUptime = rb.getUptime() - uptime;
        System.out.println("Process completed, took " + newUptime + " millisecs");

    }
    
    /**
     * Executed after the constructor but before any other method in this class.
     * @param gc 
     */
    @Override
    public final void init(GameContainer gc) {
        this.gc = gc;
    }
    
    /**
     * Update the game session once per frame.
     * @param gc
     * @param delta
     * @throws SlickException 
     */
    @Override
    public final void update(GameContainer gc, int delta)
            throws SlickException {
        this.delta = delta;
        if (ready) {
            map.update(input, scale);
            doScroll(input.getAbsoluteMouseX(), input.getAbsoluteMouseY(),
                    gc.getWidth(), gc.getHeight());
            doTileMouseOver();
        }
    }
    
    /**
     * Render everything within game session once per frame.
     * @param gc
     * @param g
     * @throws SlickException 
     */
    @Override
    public final void render(GameContainer gc, Graphics g)
            throws SlickException {
        if (ready) {
            g.scale(scale, scale);
            map.draw(g, gc.getWidth(), gc.getHeight(), scale, scale);
            //g.resetTransform();
        }
    }
    
    /**
     * Called by a parent class when the mouse wheel spins upward or downward.
     * @param change 
     */
    @Override
    public final void mouseWheelMoved(int change) {
        if (change < 0) {
            scaleOut();
        } else if (change > 0) {
            scaleIn();
        }
    }

    /**
     * Zooms the game camera out by one step.
     */
    public void scaleOut() {
        scale -= scaleStepSize;
        if (scale < minScale) {
            scale = minScale;
        }
    }
    
    /**
     * Zooms the game camera in by one step.
     */
    public void scaleIn() {
        scale += scaleStepSize;
        if (scale > maxScale) {
            scale = maxScale;
        }
    }
    
    /**
     * Called in {@code update()}, this method adjusts the color value of
     * tiles when the mouse moves over them, provided {@code doMouseover} is
     * set to true.
     */
    private final void doTileMouseOver() {
        if (map.getMouseOverIndex() >= 0) {
            map.setTileColor(map.getMouseOverIndex(),
                    Globals.TILE_MOUSEOVER_COLOR);
            if (lastTileToColor >= 0 && lastTileToColor
                    != map.getMouseOverIndex()) {
                map.setTileColor(lastTileToColor, Tile.NORMAL_COLOR);
            }
            lastTileToColor = map.getMouseOverIndex();
        }
    }
    
    /**
     * Generates a new TileMap for gameplay use.  Warning: This method wipes the
     * current map from memory.
     * @param width The width of the TileMap.
     * @param height The height of the TileMap.
     * @param min The lowest possible height value on the map (black on the
     *            heightmap).
     * @param max The highest possible height value on the map (white on the
     *            heightmap)
     * @param tileset The internal name of the tileset to be used.
     * @param treeFreq The number of times trees will occur in a forest.
     * @param treeMin The minimum size of a forest.
     * @param treeMax The maximum size of a forest.
     * @param numForests The number of forests to be considered for generation.
     * @throws SlickException 
     */
    protected final void generateMap(int width, int height, int min, int max,
            String tileset, int treeFreq, int treeMin,
            int treeMax, int numForests) throws SlickException {
        float randomFactor = (new Random().nextFloat() * 100f);

        System.out.println("Generating heightmap...");
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        long uptime = rb.getUptime();
        Image heightmap = FastNoise.getNoiseImage(width, height, randomFactor, 8,
                true);
        long newUptime = rb.getUptime() - uptime;

        Tileset set = new Tileset(tileset);

        map = new TileMap(set, width, height);
        map.loadHeightmap(heightmap, min, max);
        map.randomizeFlats();
        System.out.println("Generation complete, took " + newUptime + "ms");

        for (int i = 0; i < numForests; i++) {
            uptime = rb.getUptime();
            System.out.println("Generating forest #" + (i + 1));
            placeForest(set, treeFreq, treeMin, treeMax);
            newUptime = rb.getUptime() - uptime;
            System.out.println("Generated forest, took " + newUptime + "ms");
        }

        ready = true;
    }
    
    /**
     * Used internally to place a forest cluster on the map.
     * @param set
     * @param frequency
     * @param min
     * @param max
     * @throws SlickException 
     */
    private void placeForest(Tileset set, int frequency, int min,
            int max) throws SlickException {
        // Firstly, list all of the available tree directories available to this
        // tileset.
        String treeDir = Globals.PROP_DIR + "trees/" + set.getDirectoryName()
                + "/";
        File file = new File(treeDir);
        String[] directories;
        directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        });

        // Now, iterate through all of the valid directories.
        if (directories != null) {
            for (int i = 0; i < directories.length; i++) {
                String currentDirectory = treeDir + directories[i] + "/";
                // Get the file listing of the current directory.
                File d = new File(currentDirectory);
                ArrayList<String> names =
                        new ArrayList<>(Arrays.asList(d.list()));

                // ArrayList that will hold the prop listing later.
                List<Prop> props = new ArrayList<>();

                // Iterate through this listing and create props for each one.
                for (int j = 0; j < names.size(); j++) {
                    if (names.get(j).toLowerCase().endsWith(".png")) {
                        props.add(new Prop(new Image(currentDirectory + names.get(j)),
                                1, 1, 0, false));
                    }
                }

                Prop[] propArray = new Prop[props.size()];
                for (int j = 0; j < props.size(); j++) {
                    propArray[j] = props.get(j);
                }

                // Add the forest cluster to the map.
                int location = new Random().nextInt(map.getTileCount());
                int radius = new Random().nextInt((max - min) + 1) + min;
                Prop.createCluster(map, propArray, frequency,
                        location, radius);
            }
        }
    }
    
    /**
     * Called in {@code update()}, this method allows the map to be scrolled
     * by either moving the mouse to the edge of the screen or by right clicking
     * and dragging.
     * @param mouseX
     * @param mouseY
     * @param width
     * @param height 
     */
    private void doScroll(int mouseX, int mouseY, int width, int height) {
        int safeZone = 30;
        int scrollFactor = 5 * ((int) (1f / scale));
        boolean scrollEdge = (input.isMouseButtonDown(1)) ? false : true;

        if (scrollEdge) {
            doMouseover = true;
            gc.setMouseGrabbed(false);
            if (mouseX <= safeZone) {
                // left side of the screen
                map.offset = new Transform(map.offset.x + scrollFactor,
                        map.offset.y, map.offset.z);
            }

            if (mouseX >= (width - safeZone)) {
                // right side of the screen
                map.offset = new Transform(map.offset.x - scrollFactor,
                        map.offset.y, map.offset.z);
            }

            if (mouseY <= safeZone) {
                // top side of the screen
                map.offset = new Transform(map.offset.x,
                        map.offset.y + scrollFactor, map.offset.z);
            }

            if (mouseY >= (height - safeZone)) {
                // bottom side of the screen
                map.offset = new Transform(map.offset.x,
                        map.offset.y - scrollFactor, map.offset.z);
            }
        } else {
            doMouseover = false;
            gc.setMouseGrabbed(true);
            int vectorX = lastMouseX - mouseX;
            int vectorY = lastMouseY - mouseY;

            if (!invertRightClickScroll) {
                map.offset = new Transform(map.offset.x + vectorX,
                        map.offset.y + vectorY, map.offset.z);
            } else {
                map.offset = new Transform(map.offset.x - vectorX,
                        map.offset.y - vectorY, map.offset.z);
            }
        }

        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }
}
