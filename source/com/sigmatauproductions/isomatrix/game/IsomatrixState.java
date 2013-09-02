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

import org.newdawn.slick.*;

/**
 * An abstract class for defining all game states.  Extend this class when a new
 * game state is needed, and use {@code addState()} to have it become usable.
 * @author Will
 */
abstract public class IsomatrixState {
    
    /**
     * The name of the state.
     */
    private String name = "state";
    
    /**
     * The {@link IsomatrixGame} the state will be assigned to.
     */
    protected IsomatrixGame game = null;
    
    /**
     * The index of this state.
     */
    protected int index = -1;
    
    /**
     * The required constructor for a state where the name and game-assignment
     * must be specified.
     * @param name
     * @param game 
     */
    public IsomatrixState(String name, IsomatrixGame game) {
        this.name = name;
        this.game = game;
    }
    
    /**
     * Returns the name of this state.
     * @return 
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the name of the state.
     * @return 
     */
    @Override
    public String toString() {
        return getName();
    }
    
    /**
     * Returns the index of the state relative to the state list in the assigned
     * {@link IsomatrixGame}.
     * @return 
     */
    public int getIndex() {
        return index;
    }
    
    /**
     * To be overridden by subclasses, this is called before any frames are
     * evaluated.
     * @param gc 
     */
    abstract public void init(GameContainer gc);
    
    /**
     * To be overridden by subclasses, this is called once per frame.
     * @param gc
     * @param delta
     * @throws SlickException 
     */
    abstract public void update(GameContainer gc, int delta)
            throws SlickException;
    
    /**
     * To be overridden by subclasses, this is called once per render cycle.
     * @param gc
     * @param g
     * @throws SlickException 
     */
    abstract public void render(GameContainer gc, Graphics g)
            throws SlickException;
    abstract public void mouseWheelMoved(int change);
}
