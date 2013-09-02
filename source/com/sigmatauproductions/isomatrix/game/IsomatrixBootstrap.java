
package com.sigmatauproductions.isomatrix.game;

import org.newdawn.slick.*;

/**
 * A static class used for starting an {@link IsomatrixGame}.
 * @author Will
 */
public final class IsomatrixBootstrap {
    /**
     * Starts the specified game.
     * @param game
     * @throws SlickException 
     */
    public static void start(IsomatrixGame game) throws SlickException {
        AppGameContainer app = new AppGameContainer(game);
        app.setDisplayMode(game.width, game.height, game.fullscreen);
        app.setSmoothDeltas(true);
        if (game.capFrameRate) {
            app.setTargetFrameRate(game.targetFrameRate);
        }
        app.setShowFPS(game.showFPS);
        app.setIcon(game.icon);
        app.start();
    }
}
