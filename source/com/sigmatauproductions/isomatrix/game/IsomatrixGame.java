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

package com.sigmatauproductions.isomatrix.game;

import com.sigmatauproductions.isomatrix.Globals;
import com.sigmatauproductions.isomatrix.event.*;
import com.sigmatauproductions.isomatrix.util.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.*;

/**
 * An abstract class used for creating a game to use the Isomatrix package.
 * @author Will
 */
abstract public class IsomatrixGame extends BasicGame {
    
    /**
     * Holds the isomatrix.cfg file for settings universal to the entire game,
     * such as resolution, framerate caps, and so on.
     */
    public ConfigFile config;
    
    /**
     * The width of the game resolution, in pixels.
     */
    public static int width = 800;
    
    /**
     * The height of the game resolution, in pixels.
     */
    public static int height = 600;
    
    /**
     * Tracks whether or not the game is playing in fullscreen.
     */
    public static boolean fullscreen = false;
    
    /**
     * Tracks whether or not we show the FPS counter.
     */
    public static boolean showFPS = true;
    
    /**
     * The target frame rate.
     */
    public static int targetFrameRate = 60;
    
    /**
     * Tracks whether or not we will be using the target frame rate.
     */
    public static boolean capFrameRate = false;
    
    /**
     * The icon used by the game.
     */
    public static String icon = "resources/icons/IconLowRes.png";
    
    /**
     * The background color of the window, which can be adjusted later.
     */
    public static Color BACKGROUND_COLOR = new Color (.13f, .26f, .3f);
    
    /**
     * Tracks input from the user.
     */
    protected Input input;
    
    /**
     * The index of the currently active state.
     */
    private int activeState = 0;
    
    /**
     * A list of all of the states to be loaded.
     */
    private List<IsomatrixState> states = new ArrayList<>();
    
    /**
     * Initializes the game and places it into the default, idle state.
     * @param name
     * @throws SlickException 
     */
    public IsomatrixGame (String name) throws SlickException {
        super(name);
        DefaultState ds = new DefaultState(this);
        states.add(0, ds);
        ds.index = 0;
        loadConfig();
    }
    
    /**
     * Called before any frames occur in any state, and calls the init()
     * function of all states.
     * @param gc
     * @throws SlickException 
     */
    @Override
    public final void init(GameContainer gc)
            throws SlickException {
        input = gc.getInput();
        for (IsomatrixState state : states) {
            state.init(gc);
        }
        startup(gc);
    }
    
    /**
     * Called once per frame, this method updates the currently active state.
     * @param gc
     * @param delta
     * @throws SlickException 
     */
    @Override
    public final void update(GameContainer gc, int delta)
            throws SlickException {
        autoUpdate(delta);
        states.get(activeState).update(gc, delta);
    }
    
    /**
     * Called once per render cycle, this method calls {@code render()} in the
     * currently active state.
     * @param gc
     * @param g
     * @throws SlickException 
     */
    @Override
    public final void render(GameContainer gc, Graphics g)
            throws SlickException {
        g.setBackground(BACKGROUND_COLOR);
        states.get(activeState).render(gc, g);
        g.scale(1f, 1f);
    }
    
    /**
     * Updates the current events in the {@link EventHandler}.
     * @param delta 
     */
    public final void autoUpdate(int delta) {
        EventHandler.update(delta);
    }
    
    /**
     * Adds a state to the game for consideration and allows the programmer to
     * specify whether it should be immediately switched to.
     * @param state
     * @param switchTo 
     */
    public final void addState(IsomatrixState state, boolean switchTo) {
        if (state != null) {
            states.add(state);
            states.get(states.size()-1).index = states.size()-1;
            if (switchTo) { activeState = states.size()-1; }
        }
    }
    
    /**
     * Adds a state to the state listing without switching to the new state.
     * @param state 
     */
    public final void addState(IsomatrixState state) {
        addState(state, false);
    }
    
    /**
     * Sets the current state to the specified ID.
     * @param state 
     */
    public final void setState(int state) {
        if (state < 0 || state > states.size()) { return; }
        activeState = state;
    }
    
    /**
     * Returns a pointer to the currentState.
     * @return 
     */
    public final IsomatrixState getState() {
        return states.get(activeState);
    }
    
    /**
     * Returns the index of the current state.
     * @return 
     */
    public final int getStateIndex() {
        return activeState;
    }
    
    /**
     * Loads the isomatrix.cfg file from the resource folder and inserts
     * default values if one is not found.
     */
    private void loadConfig() {
        Globals.logMessage("Loading " + Globals.RESOURCE_DIR
                + "isomatrix.cfg...");
        try {
            config = new ConfigFile(Globals.RESOURCE_DIR + "isomatrix.cfg");
            int width = Integer.parseInt(config.getValueByProperty("reswidth"));
            int height = Integer.parseInt(config
                    .getValueByProperty("resheight"));
            boolean fullscreen = Boolean.parseBoolean(config
                    .getValueByProperty("fullscreen"));
            boolean showfps = Boolean.parseBoolean(config
                    .getValueByProperty("showfps"));
            boolean capFrameRate = Boolean.parseBoolean(config
                    .getValueByProperty("useframecap"));
            int targetFrameRate = Integer.parseInt(config
                    .getValueByProperty("framecap"));
            if (width < 800 || height < 600) {
                Globals.logWarning("Abnormally low resolution detected in "
                        + "isomatrix.cfg, switching to 800x600.");
                this.width = 800;
                this.height = 600;
            } else {
                this.width = width;
                this.height = height;
            }
            
            this.fullscreen = fullscreen;
            this.showFPS = showfps;
            this.capFrameRate = capFrameRate;
            this.targetFrameRate = targetFrameRate;
            Globals.logMessage("Configuration loaded without any problems.");
        } catch (FileNotFoundException e) {
            Globals.logError("Could not load isomatrix.cfg!  "
                    + "Using default config values.", true);
        } catch (NumberFormatException e) {
            Globals.logWarning("Parsing error in isomatrix.cfg!  "
                    + "Using default config values.");
        }
    }
    
    
    /**
     * Returns a the current config file.
     * @return 
     */
    public final ConfigFile getConfig() {
        return config;
    }
    
    /**
     * To be overridden by subclasses, this method is executed before any of the
     * init() methods are called in any given game state.
     * @param gc 
     */
    abstract public void startup(GameContainer gc);
    
}