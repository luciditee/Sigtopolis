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

/**
 * Basic enum to define the cardinal directions of a {@link TileMap}.                        
 * 
 * Note that the following descriptions refer to the visual, onscreen
 * representation of a tile map and not any sort of abstract or diagram of a
 * tile map.
 * 
 * <ul>
 *  <li>{@code NORTH} defines the upper rightmost edge of the map.</li>
 *  <li>{@code EAST} defines the bottom rightmost edge of the map.</li>
 *  <li>{@code SOUTH} defines the bottom leftmost edge of the map.</li>
 *  <li>{@code WEST} defines the upper leftmost edge of the map.</li>
 *  <li>{@code UP} defines the topmost vertex of the map.</li>
 *  <li>{@code RIGHT} defines the rightmost vertex of the map.</li>
 *  <li>{@code DOWN} defines the bottommost vertex of the map.</li>
 *  <li>{@code LEFT} defines the leftmost vertex of the map.</li>
 * </ul>
 *
 * @author sigtau
 */
public enum Direction {
    // DO NOT CHANGE THE ORDER OF THESE VALUES.
    // DOING SO CAN AND WILL RUIN THE WAY HEIGHTMAPS ARE READ.
    NORTH, EAST, SOUTH, WEST, UP, RIGHT, DOWN, LEFT
}