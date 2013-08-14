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

package com.sigmatauproductions.isomatrix.demo;

import com.sigmatauproductions.isomatrix.Globals;
import com.sigmatauproductions.isomatrix.props.*;
import com.sigmatauproductions.isomatrix.tiles.*;
import com.sigmatauproductions.isomatrix.util.*;
import java.util.Random;
import org.newdawn.slick.*;

public class IsomatrixDemo extends BasicGame {
    
    private static String title = "";
    private static int width = 800;
    private static int height = 600;
    private static boolean fullscreen = false;
    
    public static boolean showFPS = true;
    public static int targetFrameRate = 60;
    
    private Tileset set;
    private TileMap map;
    
    private ImageBuffer buffer;
    
    Prop prop;
    Prop prop2;
    Prop wideProp;
    Image heightmap;
    
    public IsomatrixDemo (String title, int width,
        int height, boolean fullscreen) throws SlickException {
        super(title);
        this.title = title;
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        set = new Tileset("default", 8);
        map = new TileMap(set, 16, 16);
        map.offset.x = 368;
        map.offset.y = 64;
        
        float randomFactor = (new Random().nextFloat()*100)-50f;
        System.out.println(randomFactor);
        heightmap = FastNoise.getNoiseImage(16, 16, randomFactor, 8, true);
        
        map.loadHeightmap(heightmap, 0, 9);
        
        Image[] propTest = { 
        new Image(Globals.PROP_DIR + "default/BlockStackRed.png"),
        //new Image(Globals.PROP_DIR + "default/SmallBlockGreen.png")
        };
        
        Image[] widePropImages = {
            new Image(Globals.PROP_DIR + "default/BlockMass.png")
        };
        
        
        //prop = new Prop(propTest, new int[] {1000}, 1, 1, 27);
        
        //prop2 = new Prop(propTest, new int[] {1000}, 1,
                //1, 28);
        
        //wideProp = new Prop(widePropImages, new int[] {1000}, 2, 2, 33);
        
        //map.addProp(prop);
        //map.addProp(prop2);
        //map.addProp(wideProp);
        //map.setShowTileID(true);
    }

    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        float scaleX = 1f;
        float scaleY = 1f;
        g.scale(scaleX, scaleY);
        map.draw(g, gc.getWidth(), gc.getHeight(), scaleX, scaleY);
        heightmap.draw(30,30);
        g.resetTransform();
    }
    
    public static final void start() throws SlickException {
        AppGameContainer app = new AppGameContainer(new IsomatrixDemo(title, width, height, fullscreen));
        app.setDisplayMode(width, height, fullscreen);
        app.setSmoothDeltas(true);
        app.setTargetFrameRate(targetFrameRate);
        app.setShowFPS(showFPS);
        app.start();
    }
    
}