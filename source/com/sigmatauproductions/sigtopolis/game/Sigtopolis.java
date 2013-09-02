
package com.sigmatauproductions.sigtopolis.game;

import com.sigmatauproductions.isomatrix.Globals;
import com.sigmatauproductions.isomatrix.game.IsomatrixGame;
import com.sigmatauproductions.isomatrix.util.ConfigFile;
import com.sigmatauproductions.sigtopolis.states.*;
import java.io.FileNotFoundException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

/**
 * The initializing class for Sigtopolis as a whole.
 * @author Will
 */
public class Sigtopolis extends IsomatrixGame {
    
    /**
     * A {@link ConfigFile} containing all of the strings used by the current
     * session, allowing for translations into other languages.
     */
    private ConfigFile languageFile;
    
    /**
     * Initializes the game and loads the current language file.
     * @throws SlickException 
     */
    public Sigtopolis() throws SlickException {
        super("Sigtopolis");
        String language = config.getValueByProperty("language");
        try {
            languageFile = new ConfigFile(Globals.GUI_DIR + language
                    + "/strings.cfg");
        } catch (FileNotFoundException e) {
            Globals.logError("The specified language \""+language+"\""
                    + " could not be found.",true);
        }
    }
    
    /**
     * Initializes the game's states and sets the initial state.
     * @param gc 
     */
    @Override
    public void startup(GameContainer gc) {
        Globals.logMessage("Sigtopolis booting!");
        Globals.logMessage("Loading states.");
        try {
            MainMenu menu = new MainMenu(this, languageFile, input);
            GameScreen game = new GameScreen(this, languageFile, input);
            
            addState(menu);
            addState(game);
            
            menu.init(gc);
            game.init(gc);
            
            setState(2);
        } catch (SlickException e) {
            Globals.logError("SlickException in startup():"
                    + e.getMessage(), true);
        }
    }
    
    /**
     * Sends a message to the current state indicating that the mouse wheel has
     * moved.
     * @param change 
     */
    @Override
    public void mouseWheelMoved(int change) {
        getState().mouseWheelMoved(change);
    }
}
