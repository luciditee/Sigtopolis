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

package com.sigmatauproductions.isomatrix.props;

import com.sigmatauproductions.isomatrix.Globals;
import com.sigmatauproductions.isomatrix.tiles.TileMap;
import com.sigmatauproductions.isomatrix.util.Transform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.newdawn.slick.*;

/**
 * Used for storing props, which act as static objects of arbitrary size and
 * sit on a map at fixed positions, such as buildings, trees, and other static
 * objects.  Props can be set as "superficial" or "non-superficial," the
 * difference being that superficial props apply to things that can be replaced
 * by other props without the need for explicit deletion--for example, a tree
 * acts as a superficial prop, and when the player places a building on the map
 * that overlaps with the tree's tile, the tree yields to the building and is
 * automatically removed.  In this case, the building is non-superficial,
 * meaning that all other props (the tree) have to yield to it while being
 * placed.
 * 
 * <p>
 * When compared to a TileMap and Tile, the Prop has a few key differences:
 * <ul>
 *  <li>Props can be of any arbitrary size at least 1x1 tiles.</li>
 *  <li>Props support animation.</li>
 *  <li>Props are "anchored" (that is, they have their origin) at their bottom-
 *      most point, unlike TileMaps, whose origins are at the top-most
 *      point.</li>
 * </ul>
 * 
 * Prop images can be of any arbitrary height, but <b>all whitespace MUST be
 * cropped from the image,</b> or else strange behavior will occur when drawing
 * the prop.
 * <p>
 * The prop class is intentionally extendable so that any game can create props
 * with custom behaviors.
 * 
 * @author Will
 */
public class Prop implements Cloneable {
    
    /**
     * Stores the default duration (in ms) of a frame in prop animations.
     */
    public static final int DEFAULT_DURATION = 100;
    
    /**
     * Used internally to store the slick2d Animation that will be drawn.
     */
    private Animation animation;
    
    /**
     * Used to store the screen-space position of the prop.
     */
    public Transform position = Transform.getOrigin();
    
    /**
     * The width (x-size) of the prop, in number of tiles.
     */
    private int width = 1;
    
    /**
     * The height (y-size) of the prop, in number of tiles.
     */
    private int height = 1;
    
    /**
     * The tile index of the bottom-most (anchor) tile of this prop.
     */
    private int anchor = 0;
    
    /**
     * Contains whether or not the tile requires registration.
     */
    private boolean doRegister = true;
    
    /**
     * The default constructor, set to private to prevent people from gaining
     * access to methods such as getWidth() and getHeight() without creating
     * a proper new Prop.
     */
    private Prop() {}
    
    /**
     * Creates a new animated non-superficial prop.
     * 
     * @param images The images used in the animation, one frame per array entry
     * @param duration An integer array (which must be the same length of the
     *                 image array) containing the durations of each frame.
     * @param width The prop's width (x-size) in tiles.
     * @param height The prop's height (y-size) in tiles.
     * @param anchor The bottom-most (anchor) tile of the prop.
     */
    public Prop(Image[] images, int[] duration,
            int width, int height, int anchor) {
        int[] d = validateDuration(duration);
        this.animation = new Animation(images, d, true);
        this.width = width;
        this.height = height;
        validateDimensions();
        this.anchor = (anchor >= 0) ? anchor : 1;
        animation.start();
    }
    
    /**
     * Same as the previous constructor, except it allows a superficial
     * prop to be created.
     * @param doRegister If set to false, the prop will be superficial.
     */
    public Prop(Image[] images, int[] duration,
            int width, int height, int anchor, boolean doRegister) {
        this(images, duration, width, height, anchor);
        this.doRegister = doRegister;
    }
    
    /**
     * Functionally identical to the first constructor, but creates a
     * non-animated prop.
     */
    public Prop (Image image, int width, int height, int anchor) {
        this(new Image[]{ image }, new int[]{ DEFAULT_DURATION },
                width, height, anchor);
    }
    
    /**
     * Functionally identical to the previous constructor, but creates a
     * non-animated prop with the ability to be superficial.
     */
    public Prop (Image image, int width, int height, int anchor,
            boolean doRegister) {
        this(image, width, height, anchor);
        this.doRegister = doRegister;
    }
    
    /**
     * Used internally to ensure that a duration array passed to one of the
     * constructors for animated props is sane and contains valid integer
     * values.
     */
    private int[] validateDuration(int[] drtn) {
        int[] d = drtn;
        // the most unreadable line of code I've ever written. It's on one line
        // because there was some pretty ridiculous extra whitespace with it
        // separated as normal, so I compacted it. -sigtau
        for(int i=0;i<d.length;i++){d[i]=(d[i]>0)?d[i]:DEFAULT_DURATION;}
        return d;
    }
    
    /**
     * Used internally to ensure the dimensions of the prop are at least 1x1.
     */
    private void validateDimensions() {
        if (width < 0) {
            width = 1;
            Globals.logWarning("Prop set to invalid width, defaulted"
                    + " to a width of 1.");
        }
        
        if (height < 0) {
            height = 1;
            Globals.logWarning("Prop set to invalid height, defaulted"
                    + " to a height of 1.");
        }
    }
    
    /**
     * Draws the prop to the screen at the position specified in its
     * {@link Transform}.
     */
    public final void draw() {
        animation.draw(position.x, position.y);
    }
    
    /**
     * Returns the bottom-most tile this prop is anchored to.
     */
    public final int getAnchor() {
        return anchor;
    }
    
    public final void setAnchor(int a) {
        anchor = a;
    }
    
    /**
     * Returns the image-height of this prop.
     */
    public final int getAnimationHeight() {
        return animation.getHeight();
    }
    
    /**
     * Returns the image-width of this prop.
     */
    public final int getAnimationWidth() {
        return animation.getWidth();
    }
    
    /**
     * Returns the y-length (height) of this prop in tiles. 
     */
    public final int getHeight() {
        return height;
    }
    
    /**
     * Returns the x-length (width) of this prop in tiles.
     */
    public final int getWidth() {
        return width;
    }
    
    /**
     * Returns the superficiality status (registration) of the prop.
     */
    public final boolean needsRegistration() {
        return doRegister;
    }
    
    @Override
    public final Prop clone() throws CloneNotSupportedException {
        return ((Prop)(super.clone()));
    }
    
    public static boolean createCluster(TileMap map, Prop[] props,
            int[] freq, int center, int _radius) {
        // First, validate that everything is non-null and good to use.
        // Return false if not.
        if (map == null || props == null || freq == null) { return false; }
        if (center < 0 || center >= map.getTileCount()) { return false; }
        if (props.length != freq.length) { return false; }
        if (map.getTile(center).isRegistered()) { return false; }
        int radius = (_radius > 1) ? _radius : 2;
        int[] frequencies = freq;
        
        // Initialize a list to hold all of the tiles we will be drawing to.
        List<Integer> tiles = new ArrayList<>();
        
        // Get all the tiles within a distance equal to the radius.
        for (int i = 0; i < map.getTileCount(); i++) {
            if (map.distance(center, i) <= radius) { tiles.add(i); }
        }
        
        // Get a random number generator instance
        Random random = new Random();
        
        // Start the prop population cycle
        boolean flag = false;
        for (;;) {
            // First we make sure we aren't finished yet.  To do this,
            // we check the frequency array.
            for (int i = 0; i < frequencies.length; i++) {
                if (frequencies[i] != 0) { flag = true; }
            }
            if (!flag || tiles.isEmpty()) { break; }
            
            // Determine which prop to put and on which tile
            int whichProp;
            int whichTile;
            do {
                whichProp = random.nextInt(props.length);
                whichTile = random.nextInt(tiles.size());
            } while (frequencies[whichProp] != 0);
                
            try {
                // Clone the prop and give it an anchor
                Prop instance = props[whichProp].clone();
                instance.setAnchor(whichTile);
                
                // Add it to the map
                map.addProp(instance);
                
                // Clean up by decreasing the frequency of the prop by one,
                // and remove the tile we've added a prop to from the list
                frequencies[whichProp]--;
                for (int i = 0; i < tiles.size(); i++) {
                    if (tiles.get(i) == whichTile) {
                        tiles.remove(i);
                    }
                }
            } catch (CloneNotSupportedException e) {
                Globals.logError("CloneNotSupportedException in"
                        + " PropCluster.create()", false);
                return false;
            }
        }
        
        // Return true, the cluster is complete.
        return true;
    }
}
