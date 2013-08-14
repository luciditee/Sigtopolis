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

import com.sigmatauproductions.isomatrix.*;
import com.sigmatauproductions.isomatrix.props.Prop;
import com.sigmatauproductions.isomatrix.util.Transform;
import org.newdawn.slick.*;

/**
 * The basic unit terrain of space for any Isomatrix-powered game.                          
 *
 * Tiles are the basic unit of space in Isomatrix.  They are not generally used 
 * as individual components outside of the Isomatrix package, but are made public
 * in case the need arises for external use.  Typically, one will use the
 * {@link TileMap} class to set up a plane of tiles to be drawn and manipulated.
 *
 * @author sigtau
 */
final class Tile {
    
    /**
     * This defines the x-size of a tile itself, which can also be derived from
     * its image.
     * 
     * Mainly used within the isomatrix.tiles package to keep from
     * having to repeatedly access the tile's image to get the dimensions.
     * 
     * @see Tile#getWidth()
     */
    private int xDim = Globals.DEFAULT_TILE_X;
    
    /**
     * Defines the y-size of a tile itself, which can also be derived from
     * its image.
     * 
     * Mainly used within the isomatrix.tiles package to keep from
     * having to repeatedly access the tile's image to get the dimensions.
     * 
     * @see Tile#getHeight()
     */
    private int yDim = Globals.DEFAULT_TILE_Y;
    
    /**
     * Defines the {@link Transform} used to track the tile's position in space.
     * 
     * Note that when referencing a Tile initialized via a {@link TileMap}, this
     * variable tracks the tile's <b>relative</b> or <b>local</b> position,
     * and in order to get the actual (global) position, you must add this to
     * the {@link TileMap#offset offset} property.
     * 
     */
    public Transform position;
    
    /**
     * The tile's image, usually derived from the {@link Tileset} of the
     * {@link TileMap} the Tile is stored within.
     * 
     * The image's dimensions must be at least 16x16 pixels in resolution and
     * is recommended to be at a 4:3 width-to-height ratio, such as 64x48.
     * 
     * @see Globals#MIN_TILE_X
     * @see Globals#MIN_TILE_Y
     */
    private Image image;
    
    /**
     * The {@link Slope} of the tile, which tracks any deformations in the
     * tile's shape, as would be with hills, valleys, rivers, and so on.
     * 
     * Stores the {@link SlopeType} and {@link Direction} of the tile's
     * deformation.
     * 
     * @see Slope
     */
    private Slope slope = new Slope();
    
    /**
     * Used internally to determine whether the tile contains a prop that
     * requires registration.
     */
    private boolean registered = false;
    
    /**
     * Used internally to store the prop currently registered to this tile.
     */
    private Prop registeredProp = null;
    
    /**
     * Used internally to store which tileset to draw images from.
     */
    private Tileset tileset;
    
    /**
     * The default constructor, not to be used in a production environment.
     * 
     * Declared private to prevent inline initialization.
     * 
     */
    private Tile() {}
    
    /**
     * The typical constructor of a tile, used to populate the object with
     * basic values to be manipulated by the user or by a {@link TileMap}.
     * 
     */
    public Tile(Transform transform, Tileset tileset) {
        Image img = tileset.getImage(0);
        this.xDim = img.getWidth();
        this.yDim = img.getHeight();
        this.position = transform;
        this.image = img;
        this.tileset = tileset;
    }
    
    /**
     * Returns the center point of the tile, as opposed to its normalized point,
     * which corresponds to the upper-leftmost pixel in the tile's image.
     * 
     */
    public Transform getCenter() {
        return new Transform(position.x+(xDim/2),position.y+(yDim/2),position.z);
    }
    
    
    /**
     * Returns the pixel width of the tile's image.
     * 
     * This is set at the Tile's initialization and any time the Tile's image is
     * changed.
     * 
     * @see Tile#getHeight()
     */
    public int getWidth() {
        return xDim;
    }
    
    /**
     * Returns the pixel height of the Tile's image.
     * 
     * This is set at the Tile's initialization and any time the Tile's image is
     * changed.
     * 
     * @see Tile#getWidth()
     */
    public int getHeight() {
        return yDim;
    }
    
    /**
     * Returns a copy of the tile's Image.
     * 
     * @see Tile#setImage();
     */
    public Image getImage() {
        return image.copy();
    }
    
    /**
     * Sets the tile's image to the specified image.
     * 
     * Note that the new image must match the dimensions of the old image.
     * <b>Important</b>: Updating the image directly is not recommended for
     * adjusting a tile's slope.  Use setSlope() instead, or you may get
     * unexpected results.
     * 
     * @see Tile#getImage();
     */
    protected void setImage(Image im) {
        if (im.getWidth() != xDim || im.getHeight() != yDim) { return; }
        image = im;
    }
    
    /**
     * Sets the {@link SlopeType} and {@link Direction} of the slope, and
     * adjusts the tile's image as necessary.
     * 
     * The SlopeType defines what kind of tile deformations are applied, such as
     * with hills, valleys, rivers, etc., and the Direction defines <b>the
     * orientation of the slope's downhill,</b> i.e. if a ball were placed on
     * the imaginary slope, the {@code Direction} is the direction the ball
     * would roll towards.
     * <p>
     * If the {@code SlopeType} has the word "DIAGONAL" or "MIDSEGMENT" in it,
     * use screen-space directions UP, LEFT, DOWN, or RIGHT.  For any other
     * {@code SlopeType}, use cardinal directions NORTH, SOUTH, EAST, or WEST.
     * 
     * @see Tile#getSlopeType();
     * @return {@code true} upon success; {@code false} upon failure
     *         (in particular, if the direction was invalid for the specified
     *         slope type).
     */
    public boolean setSlope(SlopeType type, Direction direction) {
        switch (type) {
            case STANDARD:
                switch (direction) {
                    case EAST:
                        setImage(tileset.getImage(8));
                    break;
                    case NORTH:
                        setImage(tileset.getImage(9));
                    break;
                    case WEST:
                        setImage(tileset.getImage(10));
                    break;
                    case SOUTH:
                        setImage(tileset.getImage(11));
                    break;
                    default:
                        return false;
                }
            break;
            case BOTTOM_DIAGONAL:
                switch (direction) {
                    case RIGHT:
                        setImage(tileset.getImage(4));
                    break;
                    case UP:
                        setImage(tileset.getImage(5));
                    break;
                    case LEFT:
                        setImage(tileset.getImage(6));
                    break;
                    case DOWN:
                        setImage(tileset.getImage(7));
                    break;
                    default:
                        return false;
                }
            break;
            case MIDSEGMENT:
                switch (direction) {
                    case UP:
                        setImage(tileset.getImage(16));
                    break;
                    case RIGHT:
                        setImage(tileset.getImage(17));
                    break;
                    case DOWN:
                        setImage(tileset.getImage(18));
                    break;
                    case LEFT:
                        setImage(tileset.getImage(19));
                    break;
                    default:
                        return false;
                }
            break;
            case TOP_DIAGONAL:
                switch (direction) {
                    case RIGHT:
                        setImage(tileset.getImage(12));
                    break;
                    case UP:
                        setImage(tileset.getImage(13));
                    break;
                    case LEFT:
                        setImage(tileset.getImage(14));
                    break;
                    case DOWN:
                        setImage(tileset.getImage(15));
                    break;
                    default:
                        return false;
                }
            break;
            case NONE:
                slope.setSlopeType(type);
                slope.setDirection(Direction.NORTH);
                return true;
            default:
                return false;
        }
        
        slope.setSlopeType(type);
        slope.setDirection(direction);
        return true;
    }
    
    /**
     * Returns the Tile's {@link SlopeType}.
     * 
     * @see Tile#setSlopeType();
     */
     public SlopeType getSlopeType() {
        return slope.getSlopeType();
    }
    
     /**
     * Returns the {@link Direction} of the tile's slope.
     * 
     * @see Tile#setSlopeDirection();
     */
    public Direction getSlopeDirection() {
        return slope.getDirection();
    }
    
    /**
     * Draws the tile's image to the screen at the tile's {@link Tile#position}.
     * 
     * The tile's Z (altitude) position is added to its Y position to simulate
     * actual height.
     * 
     */
    public void draw() {
        image.draw(position.x, position.y+position.z);
    }
    
    /**
     * Draws the tile's image to the screen at its {@link Tile#position}, using
     * the specified {@link Transform} offset.
     * 
     * The Z value of the offset is ignored, and as with the normal draw(), the
     * tile's Z (altitude) position is added to its Y position.
     * 
     */
    public void draw(Transform offset) {
        image.draw(offset.x+position.x, offset.y+position.y+position.z);
    }
    
    /**
     * Returns the actual (<b>global</b>) draw position of the tile relative to the
     * specified offset.
     * 
     * @see Tile#getDrawPositionRelative();
     */
    public Transform getDrawPosition(Transform offset) {
        return new Transform(offset.x+position.x, offset.y+position.y+position.z, 0);
    }
    
    /**
     * Returns the <b>local</b> draw position of the tile.
     * 
     * <b>This method does not take any offsets into account.</b>  Thus, it
     * should only be used in cases where the relative position is needed, such
     * as with pathfinding or other relative functions.
     * 
     * @see Tile#getDrawPosition();
     */
    public Transform getDrawPositionRelative() {
        return new Transform(position.x, position.y+position.z, 0);
    }
    
    /**
     * Sets the registered status of the tile.
     * 
     * If set to false, the prop value currently cached inside the tile is
     * automatically deleted (set to null).
     * 
     * @param value 
     */
    public void setRegistered(boolean value) {
        registered = value;
        if (!value) {
            registeredProp = null;
        }
    }
    
    /**
     * Returns the registered status of this tile.
     */
    public boolean isRegistered() {
        return registered;
    }
    
    /**
     * Returns the prop currently on this tile.
     */
    public Prop getProp() {
        return registeredProp;
    }
    
    /**
     * Sets this tile's prop to the specified value.
     */
    public void setProp(Prop p) {
        if (p == null) { return; }
        registeredProp = p;
    }
    
}
