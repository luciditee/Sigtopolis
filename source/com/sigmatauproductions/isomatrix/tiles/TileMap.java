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
import com.sigmatauproductions.isomatrix.props.*;
import com.sigmatauproductions.isomatrix.util.Transform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import org.newdawn.slick.*;

/**
 * A terrain consisting of a plane of {@link Tile Tiles}.
 *
 * A TileMap is required to use any of the major Isomatrix components. Tiles are
 * arranged in a rectangular plane and then deformed via heightmap into the
 * shape of hills and valleys. They are required to use the {@link Prop} and
 * Entity family of classes.
 *
 * @author sigtau
 */
public final class TileMap {

    /**
     * The minimum X-size (width) of a TileMap.
     */
    public final int MIN_X = 4;
    /**
     * The minimum Y-size (height) of a TileMap.
     */
    public final int MIN_Y = 4;
    /**
     * The maximum X-size (width) of a TileMap.
     */
    public final int MAX_X = 8192;
    /**
     * The maximum Y-size (height) of a TileMap.
     */
    public final int MAX_Y = 8192;
    /**
     * The offset used when drawing the TileMap.
     *
     * Useful primarily for "scrolling" the map along the screen when the map
     * size in pixels exceeds that of the screen resolution.
     */
    public Transform offset = new Transform(0, 0, 0);
    /**
     * The {@link Tileset} used to portray the TileMap on the screen.
     *
     * @see TileMap#getTileset()
     */
    private Tileset tileset;
    /**
     * The x-size (width) of the TileMap.
     *
     * @see TileMap#getWidth()
     */
    private int xSize = 0;
    /**
     * The y-size (height) of the TileMap.
     *
     * @see TileMap#getHeight()
     */
    private int ySize = 0;
    /**
     * The array that holds all of the {@link Tile} objects that make up this
     * TileMap.
     *
     * @see TileMap#getTile()
     */
    private Tile[] tiles;
    /**
     * Used internally to determine whether or not the TileMap is ready to be
     * drawn to the screen using draw().
     */
    private boolean canDraw = false;
    /**
     * Used internally to determine whether props should be drawn.
     */
    private boolean drawProps = false;
    /**
     * Used internally to store the props on this map.
     */
    private List<Prop> props = new ArrayList<>();
    /**
     * Used internally to determine whether or not tile ID labels should be
     * drawn.
     */
    private boolean showTileIDs = false;

    /**
     * Initializes a TileMap using the default tileset, default width, and
     * default height.
     *
     * @throws SlickException
     */
    public TileMap() throws SlickException {
        this(new Tileset(), Globals.DEFAULT_X_SIZE, Globals.DEFAULT_Y_SIZE);
    }

    /**
     *
     * Initializes a new TileMap using the specified tileset, width, and height.
     *
     * @param tileset
     * @param xSize
     * @param ySize
     * @throws SlickException
     */
    public TileMap(Tileset tileset, int xSize, int ySize) throws SlickException {
        this.tileset = tileset;
        this.xSize = xSize;
        this.ySize = ySize;
        tiles = new Tile[xSize * ySize];
        populateMap();
    }

    /**
     *
     * Used internally to fill the map with the default tiles at the default
     * height.
     *
     * @throws SlickException
     */
    private void populateMap() throws SlickException {

        int adjustmentFactorX = tileset.getImage(0).getWidth() / 2;
        int adjustmentFactorY = tileset.getImage(0).getHeight() / 3;

        // Start by populating the array with the basic tiles
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(new Transform(), tileset);
            tiles[i].setSlope(SlopeType.NONE, Direction.NORTH);
        }

        Transform rowOrigin = Transform.getOrigin();
        int count = 0;

        // Now let's assign true coordinates to the tiles
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                tiles[count].position.x = rowOrigin.x + (adjustmentFactorX * x);
                tiles[count].position.y = rowOrigin.y + (adjustmentFactorY * (x));
                count++;
            }
            rowOrigin.x -= adjustmentFactorX;
            rowOrigin.y += adjustmentFactorY;
        }

        canDraw = true;
        drawProps = true;
    }

    /**
     *
     * Returns the {@link Tileset} used by this map.
     *
     * @return The Tileset used by this map.
     */
    public Tileset getTileset() {
        return tileset;
    }

    /**
     *
     * Returns a {@link Tile} from the TileMap's array given the specified
     * index.
     *
     * @param index
     * @return Returns a specific tile from the map.
     */
    public Tile getTile(int index) {
        return tiles[index];
    }

    /**
     * Sets the image of the specified {@link Tile} to be of a specific tile
     * from the map's {@link Tileset}.
     * 
     * <b>Warning!</b> It is not advisable to set the image of the tile manually
     * when adjusting a tile's slope!  Use setTileSlope() instead.
     *
     * @param index
     * @param tilesetIndex
     */
    public void setTileImage(int index, int tilesetIndex) {
        if (index > tiles.length || index < 0) { return; }
        tiles[index].setImage(tileset.getImage(tilesetIndex));
    }
    
    /**
     * Sets the slope of the specified tile and updates its image accordingly.
     * 
     * @param index The index of the tile.  Will return without doing anything
     *              in the event of an invalid index.
     * @param type The {@link SlopeType}.
     * @param direction The {@link Direction}.
     */
    public void setTileSlope(int index, SlopeType type, Direction direction) {
        if (index > tiles.length || index < 0) { return; }
        tiles[index].setSlope(type, direction);
    }
    
    /**
     * Sets the slope of the specified tile and updates its image accordingly.
     * 
     * @param coords An array ({@code x = int[0], y = int[1]}) containing the
     *                        tile coordinates.
     * @param type The {@link SlopeType}.
     * @param direction The {@link Direction}.
     */
    public void setTileSlope(int[] coords, SlopeType type, Direction direction){
        int index = getTileByCoordinates(coords[0], coords[1]);
        if (index < 0) { return; }
        setTileSlope(index, type, direction);
    }

    /**
     *
     * Sets the z-value (altitude or "height") of a tile to be the specified
     * value.
     *
     * Note that {@code height} does not take a pixel value, but rather, a
     * normalized value to be multiplied by the height offset in the map's
     * {@link Tileset}. For example, if the height offset is set to 8 pixels,
     * calling {@code setTileHeight(34,2)} sets the height of tile 34 to be 2
     * units above the zero height.
     *
     * @param index
     * @param height
     */
    public void setTileHeight(int index, int height) {
        if (index > tiles.length || index < 0) { return; }
        tiles[index].position.z = -height * tileset.getHeightOffset();
    }

    /**
     *
     * Gets the z-position (altitude) of the specified tile.
     *
     * @param index
     * @return The value of the tile's z-position.
     */
    public int getTileHeight(int index) {
        return -tiles[index].position.z;
    }

    /**
     *
     * Using basic arithmetic, returns the index of the next immediate tile in
     * the the specified cardinal or screen-space {@link Direction}.
     *
     * @param index
     * @param direction
     * @return
     */
    public int getNeighbor(int index, Direction direction) {
        int ret = 0;
        switch (direction) {
            case UP:
                ret = (index - 1) - ySize;
                break;
            case DOWN:
                ret = (index + 1) + ySize;
                break;
            case LEFT:
                ret = (index - 1) + ySize;
                break;
            case RIGHT:
                ret = (index + 1) - ySize;
                break;
            case NORTH:
                ret = (index - ySize);
                break;
            case SOUTH:
                ret = (index + ySize);
                break;
            case EAST:
                ret = index + 1;
                break;
            case WEST:
                ret = index - 1;
                break;
        }

        if (ret < 0 || ret >= tiles.length) {
            ret = -1;
        }

        return ret;
    }

    /**
     *
     * Gets the x-size (width) of the map.
     *
     * @return Returns an int containing the x-size.
     */
    public int getWidth() {
        return xSize;
    }

    /**
     *
     * Gets the y-size (height) of the map.
     *
     * @return Returns an int containing the y-size.
     */
    public int getHeight() {
        return ySize;
    }

    /**
     *
     * Draws the tile to the screen using its offset.
     *
     * @return Returns true if {@code canDraw} is set to true, false otherwise.
     * @throws SlickException
     */
    public boolean draw(Graphics g) throws SlickException {
        if (canDraw) {
            for (int i = 0; i < tiles.length; i++) {
                tiles[i].draw(offset);
            }

            if (drawProps) {
                for (Prop prop : props) {
                    prop.position = getTile(prop.getAnchor())
                            .getDrawPosition(offset);

                    prop.position.y = getTile(prop.getAnchor())
                            .getDrawPosition(offset).y
                            - getTile(prop.getAnchor()).getHeight()
                            - getTileset().getPropOffset();
                    if (prop.getWidth() > 1 || prop.getHeight() > 1) {
                        prop.position.x = prop.position.x
                                - (tileset.getTileWidth() / 2);
                    }

                    prop.draw();
                }
            }

            for (int i = 0; i < tiles.length; i++) {
                if (showTileIDs) {
                    g.drawString(new Integer(i).toString(),
                            tiles[i].getDrawPosition(offset).x
                            + (tiles[i].getWidth() / 2 - 10),
                            tiles[i].getDrawPosition(offset).y
                            + (tiles[i].getHeight() / 2 - 10));
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Adds the specified prop to the map.
     *
     * Use the prop's {@code anchor} property to determine where the prop will
     * be placed.
     *
     * @param prop
     * @return Returns false if the prop is placed out of bounds or on another
     * prop.
     */
    public boolean addProp(Prop prop) {
        // First, we check to see if the prop requires that it be registered
        // to the tiles underneath.  Usually, superficial props like rocks,
        // trees, and other natural world elements don't have to be registered.
        if (!prop.needsRegistration()) {
            props.add(prop);
            sortProps();
            return true;
        }

        // Create a temporary ArrayList of tile indexes to 
        List<Integer> tilesToRegister = new ArrayList<>();

        int rowInitial = prop.getAnchor();
        int index;
        for (int x = 0; x < prop.getWidth(); x++) {
            index = rowInitial;
            for (int y = 0; y < prop.getHeight(); y++) {
                tilesToRegister.add(index);
                index = getTileByCoordinates(getTileCoordinates(index)[0],
                        getTileCoordinates(index)[1] - 1);
            }
            rowInitial = getTileByCoordinates(
                    getTileCoordinates(rowInitial)[0] - 1,
                    getTileCoordinates(rowInitial)[1]);
        }

        for (int tileIndex : tilesToRegister) {
            if (tileIndex < 0 || tileIndex > tiles.length) {
                return false;
            } else {
                if (getTile(tileIndex).isRegistered()) {
                    return false;
                }
                getTile(tileIndex).setRegistered(true);
                getTile(tileIndex).setProp(prop);
            }
        }

        // Add the new prop to the ArrayList of props
        props.add(prop);

        // Sort the list in the order of their anchors so that they are not
        // drawn on top of one another (tall buildings, etc.)
        sortProps();
        return true;
    }

    /**
     * Deletes the specified prop from the map.
     *
     * @param prop
     */
    public void deleteProp(Prop prop) {
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].getProp().equals(prop)) {
                tiles[i].setRegistered(false);
            }
        }

        props.remove(prop);
    }

    /**
     * Used internally to put the props in the order that they will be drawn.
     *
     */
    private void sortProps() {
        // Create an anonymous inner class to define how props will be sorted--
        // in this case, by their anchor.
        Collections.sort(props, new Comparator<Prop>() {
            @Override
            public int compare(Prop p1, Prop p2) {
                int prop1 = ((Prop) p1).getAnchor();
                int prop2 = ((Prop) p2).getAnchor();

                // If you reverse these two, you get trippy results where front
                // objects get rendered in front of back objects. That said,
                // I do not recommend you do that unless you're feeling lulzy.
                return prop1 - prop2;
            }
        });
    }

    /**
     * Gets a tile based on its coordinate.
     *
     * The origin of the tile grid is at the top corner, or at the tile closest
     * to {@code Direction.UP}. The origin is located at (0,0).
     *
     * @param x The x coordinate (along the {@code Direction.NORTH} or
     * {@code Direction.SOUTH} edge.
     * @param y The y coordinate (along the {@code Direction.EAST} or
     * {@code Direction.WEST} edge.
     * @return Returns the tile index based on its coordinate.
     */
    public int getTileByCoordinates(int x, int y) {
        // First off, return -1 if they give us a negative or oversized
        // coordinate in either direction.
        if (x < 0 || x > xSize || y < 0 || y > ySize) {
            return -1;
        }
        int index = x;
        for (int i = 0; i < y; i++) {
            index += xSize;
        }

        if (x == xSize - 1) {
            index -= xSize;
        }

        return index;
    }

    /**
     * Returns an integer array (x = int[0], y = int[1]) containing the
     * coordinates of the specified tile index.
     *
     * @param index
     */
    public int[] getTileCoordinates(int index) {
        // First, make sure the index is a sane index.
        if (index < 0 || index > tiles.length) {
            return null;
        }

        // Iterate through all possible coordinate combinations until you've
        // found the correct one, then return it.
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                if (getTileByCoordinates(x, y) == index) {
                    return new int[]{x, y};
                }
            }
        }

        // If we're here, something went wrong--return -1, -1 for the
        // coordinates to indicate this.
        return new int[]{-1, -1};
    }

    /**
     * Returns the distance (in the form of an integer) that measures the number
     * of tiles, inclusive, from {@code tile1} to {@code tile2}.
     *
     */
    public int distance(int tile1, int tile2) {
        // First, verify that the tiles are sane
        int[] nonsane = {-1, -1};
        if (getTileCoordinates(tile1) == nonsane
                || getTileCoordinates(tile2) == nonsane
                || getTileCoordinates(tile1) == null
                || getTileCoordinates(tile2) == null) {

            // Return -1 if they aren't.
            return -1;

        }

        // Distance between two points = sqrt((x2-x1)^2 + (y2-y1)^2)
        // We first put the x and y coordinates into small arrays for easy
        // access, then raise them to the power of 2.
        double[] x = {(double) getTileCoordinates(tile1)[0],
            (double) getTileCoordinates(tile2)[0]};
        double[] y = {(double) getTileCoordinates(tile1)[1],
            (double) getTileCoordinates(tile2)[1]};
        double operand1 = Math.pow((x[1] - x[0]), 2);
        double operand2 = Math.pow((y[1] - y[0]), 2);

        // Get the square root of the two operands added together, and cast it
        // to an integer to prevent loss of precision.
        return ((int) Math.sqrt(operand1 + operand2));
    }

    /**
     * Toggles tile ID labels on and off (used for debug).
     */
    public void setShowTileID(boolean show) {
        showTileIDs = show;
    }
    
    /**
     * 
     * Randomizes the flat tiles (tiles with a {@link SlopeType} of
     * {@code NONE}) to give variation to the TileMap's appearance.
     * 
     * The parameters in this method must add up to 1.0 in sum, otherwise
     * this method will not work--logical, given that the parameters are
     * actually values of probability.
     * 
     * @param first The likelihood of the first tile, from 0.0 to 1.0.
     * @param second The likelihood of the second tile, from 0.0 to 1.0.
     * @param third The likelihood of the third tile, from 0.0 to 1.0.
     * @param fourth The likelihood of the fourth tile, from 0.0 to 1.0.
     * @return Returns true upon success, and false if the parameters do not
     *         equal 1.0 in sum.
     */
    public boolean randomizeFlats(double first, double second,
            double third, double fourth) {
        // First, make sure the arguments evaluate to 1.0 when added
        double total = first+second+third+fourth;
        
        if (total != 1.0) {
            return false;
        }
        
        Random rand = new Random();
        for (int i = 0; i < tiles.length; i++) {
            // If the current tile is flat, randomize it.
            if (tiles[i].getSlopeType() == SlopeType.NONE) {
                // Calculate probability.  Usually, the first is the most common
                // so we can omit that from the probability check and just use
                // and else{} block for it later, due to the nature of
                // probability (0.0 to 1.0).
                boolean scnd = rand.nextDouble() <= second;
                boolean thrd = rand.nextDouble() <= third;
                boolean frth = rand.nextDouble() <= fourth;
                
                if (scnd) {
                    tiles[i].setImage(tileset.getImage(1));
                } else if (thrd) {
                    tiles[i].setImage(tileset.getImage(2));
                } else if (frth) {
                    tiles[i].setImage(tileset.getImage(3));
                } else {
                    tiles[i].setImage(tileset.getImage(0));
                }
            }
        }
        
        return true;
    }
    
    /**
     * Randomizes the flat tiles (tiles with a {@link SlopeType} of
     * {@code NONE}) to give variation to the TileMap's appearance, using only
     * the default values of 0.6, 0.2, 0.1, and 0.1 for the likelihoods
     * respectively.
     */
    public boolean randomizeFlats() {
        return randomizeFlats(0.6, 0.2, 0.1, 0.1);
    }
}
