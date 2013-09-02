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
import com.sigmatauproductions.isomatrix.util.ConfigFile;
import com.sigmatauproductions.sigtopolis.ui.*;
import com.sigmatauproductions.sigtopolis.util.FontHandler;
import org.newdawn.slick.*;

public final class MainMenu extends IsomatrixState {
    
    private ConfigFile strings;
    private FontHandler text;
    private Input input;
    private Graphics g = null;
    
    private TitleMenu main;
    private TitleMenu newGameMenu;
    private TitleMenu optionsMenu;
    
    private HalfPane[] panes;
    
    private TitleMenu activeMenu;
    
    
    public MainMenu(IsomatrixGame game, ConfigFile strings, Input input)
        throws SlickException {
        super("Main Menu", game);
        Globals.logMessage("Creating main menu instance.");
        this.input = input;
        this.strings = strings;
        String fontDir = strings.getValueByProperty("font");
        if (fontDir == null) {
            Globals.logError("The font is not specified in the current "
                    + "language file " + strings.getFilename(), true);
        }
        
        text = new FontHandler(Globals.GUI_DIR + "fonts/" + fontDir + "/");
        text.processLanguage(strings.getLines());
        
        main = new TitleMenu(
            new String[] { strings.getValueByProperty("name"),
            strings.getValueByProperty("newgame"),
            strings.getValueByProperty("loadgame"),
            strings.getValueByProperty("options"),
            strings.getValueByProperty("quit") },
            new int[] {
            0, 30, 0, 0, 50
            },
            new boolean[] {
                false, true, true, true, true
            },
            new int[] {
                0, 0, 1, 2, 3
            },
            this, text);
        
        newGameMenu = new TitleMenu(
            new String[] { strings.getValueByProperty("quickplay"),
            strings.getValueByProperty("customplay"),
            strings.getValueByProperty("sandboxgame"),
            strings.getValueByProperty("scenario"), 
            strings.getValueByProperty("tutorial") },
            new int[] {
            78, 0, 0, 0, 0
            },
            new boolean[] {
                true, true, true, true, true
            },
            new int[] {
                0, 1, 2, 3, 4
            },
            this, text);
        
        optionsMenu = new TitleMenu(
            new String[] { strings.getValueByProperty("gameplay"),
            strings.getValueByProperty("graphics"),
            strings.getValueByProperty("audio"),
            strings.getValueByProperty("about") },
            new int[] {
            78, 0, 0, 0
            },
            new boolean[] {
                true, true, true, true
            },
            new int[] {
                0, 1, 2, 3
            },
            this, text);
        
        
        HalfPane[] p = {
            new HalfPane(strings.getValueByProperty("quickplay"), this, text,
                this.input),
            new HalfPane(strings.getValueByProperty("customplay"), this, text,
                this.input),
            new HalfPane(strings.getValueByProperty("sandboxgame"), this, text,
                this.input),
            new HalfPane(strings.getValueByProperty("scenario"), this, text,
                this.input),
            new HalfPane(strings.getValueByProperty("tutorial"), this, text,
                this.input),
            new HalfPane(strings.getValueByProperty("gameplay"), this, text,
                this.input),
            new HalfPane(strings.getValueByProperty("graphics"), this, text,
                this.input),
            new HalfPane(strings.getValueByProperty("audio"), this, text,
                this.input),
            new HalfPane(strings.getValueByProperty("about"), this, text,
                this.input)
        };panes = p;
        panes[0].addComponent(new PaneLabel(
                strings.getValueByProperty("quickplay_desc"),
                0,48,panes[0],input,text.getSmallFont()));
        
        setVisiblePane(0);
        
        newGameMenu.setHidden(true);
        optionsMenu.setHidden(true);
        activeMenu = main;
    }
    
    @Override
    public final void init(GameContainer gc) {
       
    }
    
    @Override
    public final void update(GameContainer gc, int delta) 
            throws SlickException {
        main.update(delta, input);
        newGameMenu.update(delta, input);
        optionsMenu.update(delta, input);
        updatePanes(gc, g);
        
        switch (main.getCurrentAction()) {
            case 0:
                optionsMenu.setHidden(true);
                newGameMenu.setHidden(false);
                activeMenu = newGameMenu;
            break;
            case 1:
                System.out.println("Load Game");
            break;
            case 2:
                optionsMenu.setHidden(false);
                newGameMenu.setHidden(true);
                activeMenu = optionsMenu;
            break;
            case 3:
                System.exit(0);
            break;
        }
        
    }
    
    @Override
    public final void render(GameContainer gc, Graphics g)
            throws SlickException {
        this.g = g;
        new Image("resources/TransparencyTest.png").draw(0,0);
        main.draw(0, gc, g, input);
        optionsMenu.draw(1, gc, g, input);
        newGameMenu.draw(1, gc, g, input);
        drawPanes(gc, g);
    }
    
    @Override
    public final void mouseWheelMoved(int change) {
        
    }
    
    public final void setVisiblePane(int pane) {
        if (pane < 0 || pane > panes.length-1) {
            for (int i = 0; i < panes.length; i++) {
                panes[i].setHidden(true);
            }
            return;
        }
        
        for (int i = 0; i < panes.length; i++) {
            if (i == pane) {
                panes[i].setHidden(false);
            } else {
                panes[i].setHidden(true);
            }
        }
    }
    
    public final void drawPanes(GameContainer gc, Graphics g) {
        for (int i = 0; i < panes.length; i++) {
            panes[i].draw(gc, g, input);
        }
    }
    
    public final void updatePanes(GameContainer gc, Graphics g) {
        for (int i = 0; i < panes.length; i++) {
            panes[i].update(gc, g);
        }
    }
    
}
