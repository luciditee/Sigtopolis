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
package com.sigmatauproductions.isomatrix;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.newdawn.slick.Color;

/**
 * The Isomatrix global class, containing all variables that may need to be
 * accessible to the entire project.
 *
 * @author Will
 */
public final class Globals {

    /**
     * The version string of this build of Isomatrix.
     */
    public static final String ISOMATRIX_VERSION = "Isomatrix v0.5";
    /**
     * Used for whether or not the current build is a debug build.
     */
    public static boolean DEBUG_ENABLED = true;
    /**
     * Stores the current system's newline character.
     */
    public static final String newline = System.getProperty("line.separator");
    /**
     * Contains the log of the current session.
     */
    private static StringBuilder log = new StringBuilder(ISOMATRIX_VERSION
            + " - Began logging at " + new java.util.Date().toString()
            + newline);
    /**
     * Contains the current username of whoever is logged in, as reported by
     * the system environment variables.  Warning: This can be spoofed and may
     * not always be accurate, though the accuracy is out of the control of
     * Isomatrix and Java itself.
     */
    public static final String USER_NAME = System.getProperty("user.name");
    
    /**
     * The default pixel width of a {@link Tile}.
     */
    public static final int DEFAULT_TILE_X = 64;
    /**
     * The default pixel height of a {@link Tile}.
     */
    public static final int DEFAULT_TILE_Y = 48;
    /**
     * The smallest pixel-width of a tile.
     */
    public static final int MIN_TILE_X = 16;
    /**
     * The smallest pixel-height of a tile.
     */
    public static final int MIN_TILE_Y = 16;
    /**
     * The default x-size of a tile.
     */
    public static final int DEFAULT_X_SIZE = 32;
    /**
     * The default y-size of a tile.
     */
    public static final int DEFAULT_Y_SIZE = 32;
    /**
     * A string leading to the resource directory of Isomatrix, relative to the
     * classpath.
     */
    public static final String RESOURCE_DIR = "resources/";
    /**
     * A string leading to the tileset directory of Isomatrix, relative to the
     * classpath.
     */
    public static final String TILESET_DIR = RESOURCE_DIR + "tilesets/";
    /**
     * A string naming the default tileset.
     */
    public static final String DEFAULT_TILESET = "default";
    /**
     * A string naming the prop directory of Isomatrix, relative to the
     * classpath.
     */
    public static final String PROP_DIR = RESOURCE_DIR + "props/";
    /**
     * A string naming the GUI image directory for Isomatrix, relative to the
     * classpath.
     */
    public static final String GUI_DIR = RESOURCE_DIR + "gui/";
    
    public static final Color TILE_MOUSEOVER_COLOR = new Color(100,100,255);
    
    /**
     * Logs the specified warning to the debug log.
     */
    public static void logWarning(String warning) {
        log.append(new java.util.Date().toString());
        log.append(" ** Warning: ");
        log.append(warning);
        log.append(newline);

        if (DEBUG_ENABLED) {
            System.out.println("** Warning: " + warning);
        }
    }

    /**
     * Logs the specified error to the debug log, and also logs a stacktrace of
     * the error if requested. In the event of a debug build, logging an error
     * will cause the entire program to exit after dumping the debug log to a
     * file named {@code debut.txt}.
     */
    public static void logError(String err, boolean stacktrace) {
        log.append(new java.util.Date().toString());
        log.append(" *** Error: ");
        log.append(err);
        log.append(newline);

        // If it's a debug log, log the stacktrace--or if we've explicitly
        // requested one
        if (stacktrace || DEBUG_ENABLED) {
            log.append("*** Stack trace:");
            log.append(newline);
            StackTraceElement[] error = Thread.currentThread().getStackTrace();
            for (int i = 0; i < error.length; i++) {
                log.append("*** ");
                log.append(error[i]);
                log.append(newline);
            }

            // ... and print it to the console.
            System.out.print("*** Error: ");
            System.out.println(err);
            System.out.println("*** Stack trace:");
            StackTraceElement[] e = Thread.currentThread().getStackTrace();
            for (int i = 0; i < e.length; i++) {
                System.out.println("*** " + e[i]);
            }
            System.out.println();
        }

        // Spew a debug log to a file if a serious error occurs (but only under
        // the supervision of a debug build)
        if (DEBUG_ENABLED) {
            log.append("Spewing debug log...");
            try (PrintWriter spew = new PrintWriter("debug.txt")) {
                spew.println(log.toString());
                spew.flush();
            } catch (FileNotFoundException e) {
                System.err.println("CRITICAL: Could not spew debug "
                        + "log to file!");
                e.printStackTrace(System.err);
            }

            System.exit(-1);
        }
    }

    /**
     * Logs the specified message to the debug log.
     */
    public static void logMessage(String message) {
        log.append(new java.util.Date().toString());
        log.append(" * ");
        log.append(message);
        log.append(newline);

        if (DEBUG_ENABLED) {
            System.out.println("* " + message);
        }
    }

    /**
     * Returns the debug log in the form of a String.
     *
     * @return
     */
    public static String getLog() {
        return log.toString();
    }
    
    /**
     * A utility function for linear interpolation.
     * @param a The starting point.
     * @param b The ending point.
     * @param t The amount of completion of the interpolation.
     * @return 
     */
    public static float lerp(float a, float b, float t) {
        if (t < 0) {
            return a;
        }
        return a + t * (b - a);
    }

    /**
     * The default constructor, set to private to ensure the class is always
     * accessed from a static context.
     */
    private Globals() {
    }
}
