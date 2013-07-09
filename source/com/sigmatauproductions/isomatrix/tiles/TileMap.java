
package com.sigmatauproductions.isomatrix.tiles;

import com.sigmatauproductions.isomatrix.*;
import org.newdawn.slick.*;

/**
 * A terrain consisting of a plane of {@link Tile Tiles}.                          
 *
 * A TileMap is required to use any of the major Isomatrix components.  Tiles
 * are arranged in a rectangular plane and then deformed via heightmap into the
 * shape of hills and valleys.  They are required to use the Prop and Entity
 * family of classes.
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
     * The default sea level of the TileMap, used if one is not defined later.
     */
    public final int DEFAULT_SEA_LEVEL = 0;
    
    /**
     * The offset used when drawing the TileMap.
     * 
     * Useful primarily for "scrolling" the map along the screen when the map
     * size in pixels exceeds that of the screen resolution.
     */
    public Transform offset = new Transform(300, 0, 0);
    
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
    public TileMap (Tileset tileset, int xSize, int ySize) throws SlickException {
        this.tileset = tileset;
        this.xSize = xSize;
        this.ySize = ySize;
        tiles = new Tile[xSize*ySize];
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
        
        int adjustmentFactorX = tileset.getImage(0).getWidth()/2;
        int adjustmentFactorY = tileset.getImage(0).getHeight()/3;
        
        // Start by populating the array with the basic tiles
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(new Transform(), tileset.getImage(0).copy());
        }
        
        Transform rowOrigin = Transform.getOrigin();
        int count = 0;
        
        // Now let's assign true coordinates to the tiles
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                tiles[count].position.x = rowOrigin.x+(adjustmentFactorX*x);
                tiles[count].position.y = rowOrigin.y+(adjustmentFactorY*(x));
                count++;
            }
            rowOrigin.x -= adjustmentFactorX;
            rowOrigin.y += adjustmentFactorY;
        }
        
        canDraw = true;
        
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
     * @param index
     * @param tilesetIndex 
     */
    public void setTileImage(int index, int tilesetIndex) {
        tiles[index].setImage(tileset.getImage(tilesetIndex));
    }
    
    /**
     * 
     * Sets the z-value (altitude or "height") of a tile to be the specified
     * value.
     * 
     * Note that {@code height} does not take a pixel value, but rather, a
     * normalized value to be multiplied by the height offset in the map's
     * {@link Tileset}.  For example, if the height offset is set to 8 pixels,
     * calling {@code setTileHeight(34,2)} sets the height of tile 34 to be
     * 2 units above the zero height.
     * 
     * @param index
     * @param height 
     */
    public void setTileHeight(int index, int height) {
        tiles[index].position.z = -height*tileset.getHeightOffset();
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
                ret = (index-1)-ySize;
            break;
            case DOWN:
                ret = (index+1)+ySize;
            break;
            case LEFT:
                ret = (index-1)+ySize;
            break;
            case RIGHT:
                ret = (index+1)-ySize;
            break;
            case NORTH:
                ret = (index-ySize);
            break;
            case SOUTH:
                ret = (index+ySize);
            break;
            case EAST:
                ret = index+1;
            break;
            case WEST:
                ret = index-1;
            break;
        }
        
        if (ret < 0 || ret >= tiles.length) ret = -1;
        
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
    public boolean draw() throws SlickException {
        if (canDraw) {
            for (int i = 0; i < tiles.length; i++) {
                //System.out.println("Drawing... (" + tiles[i].position.x + ", " + tiles[i].position.y + ", " + tiles[i].position.z + ")");
                tiles[i].draw(offset);
            }
            return true;
        }
        
        return false;
    }
}
