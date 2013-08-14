package com.sigmatauproductions.isomatrix.tiles;

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

/**
 * A simple static class for basic transformations with {@link Direction}
 * objects.
 * 
 * @author Will
 */
public final class DirectionUtils {
    private DirectionUtils() {}
    
    /**
     * Returns the opposite (inverse) direction.
     * @param dir
     * @return 
     */
    public static Direction getInverse(Direction dir) {
        switch (dir) {
            case NORTH:
                return Direction.SOUTH;
            case EAST:
                return Direction.WEST;
            case SOUTH:
                return Direction.NORTH;
            case WEST:
                return Direction.EAST;
            case UP:
                return Direction.DOWN;
            case RIGHT:
                return Direction.LEFT;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
        }

        return null;
    }
    
    /**
     * Returns the clockwise {@link Direction}.
     * @param dir
     * @return 
     */
    public static Direction clockwise(Direction dir) {
        switch (dir) {
            case NORTH:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.EAST;
            case EAST:
                return Direction.DOWN;
            case DOWN:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.LEFT;
            case LEFT:
                return Direction.WEST;
            case WEST:
                return Direction.UP;
            case UP:
                return Direction.NORTH;
        }
        
        return null;
    }
    
    /**
     * Returns the counter-clockwise {@link Direction}.
     * @param dir
     * @return 
     */
    public static Direction counterClockwise(Direction dir) {
        switch (dir) {
            case NORTH:
                return Direction.UP;
            case UP:
                return Direction.WEST;
            case WEST:
                return Direction.LEFT;
            case LEFT:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.DOWN;
            case DOWN:
                return Direction.EAST;
            case EAST:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.NORTH;
        }
        
        return null;
    }
    
    /**
     * Returns the clockwise {@link Direction} via number of steps.
     * @param dir
     * @return 
     */
    public static Direction clockwise(Direction dir, int steps) {
        int stepsValid = (steps > 0) ? steps : 1;
        Direction d = dir;
        for (int i = 0; i < stepsValid; i++) { d = clockwise(d); }
        return d;
    }
    
    /**
     * Returns the counter-clockwise {@link Direction} via number of steps.
     * @param dir
     * @return 
     */
    public static Direction counterClockwise(Direction dir, int steps) {
        int stepsValid = (steps > 0) ? steps : 1;
        Direction d = dir;
        for (int i = 0; i < stepsValid; i++) { d = counterClockwise(d); }
        return d;
    }
    
    /**
     * Returns the (clockwise-oriented) rotational difference between two
     * {@link Direction} vectors in multiples of 45.
     * @param dir
     * @return 
     */
    public static int degreeDifferenceClockwise(Direction dir1,Direction dir2) {
        int difference = 0;
        Direction d1 = dir1;
        Direction d2 = dir2;
        
        while (d1 != d2) {
            difference += 45;
            d1 = DirectionUtils.clockwise(d1);
        }
        
        return difference;
    }
    
    /**
     * Returns the (counterclockwise-oriented) rotational difference between two
     * {@link Direction} vectors in multiples of 45.
     * @param dir
     * @return 
     */
    public static int degreeDifferenceCounterClockwise(Direction dir1,
            Direction dir2) {
        int difference = 0;
        Direction d1 = dir1;
        Direction d2 = dir2;
        
        while (d1 != d2) {
            difference += 45;
            d1 = DirectionUtils.counterClockwise(d1);
        }
        
        return difference;
    }
}
