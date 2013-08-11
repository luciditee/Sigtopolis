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
 * Defines the shapes and deformations applied to tiles in the event of hills,
 * valleys, mountains, and so on.
 *
 * This class is simply a wrapper for the {@link SlopeType} and
 * {@link Direction} classes and combines them into a single concept.
 * 
 * @see SlopeType
 * @see Direction
 * @author sigtau
 */
final class Slope {
    
    /**
     * Defines the Slope's type of slope.  Defaults to {@code SlopeType.NONE}.
     * 
     * @see SlopeType
     */
    private SlopeType type = SlopeType.NONE;
    
    /**
     * Defines the direction of the Slope.
     * 
     * Note that the direction corresponds to the side of the "hill" it would
     * be on--that is to say, an uphill slope where the lower point is
     * {@link Direction#SOUTH} and the higher point is {@link Direction#NORTH},
     * the direction would be set to {@link Direction#SOUTH}.
     * 
     * @see Direction
     */
    private Direction dir = Direction.NORTH;
    
    /**
     * The default constructor; creates a new Slope object with the default
     * type and direction values.
     */
    public Slope() {}
    
    /**
     * Creates a new Slope object with the specified slope type and direction.
     * 
     * @param type
     * @param dir 
     */
    public Slope(SlopeType type, Direction dir) {
        this.type = type;
        this.dir = dir;
    }
    
    /**
     * Returns the {@link SlopeType} of the Slope.
     * 
     * @return The {@link SlopeType}
     */
    public SlopeType getSlopeType() {
        return type;
    }
    
    /**
     * Returns the {@link Direction} of the Slope.
     * 
     * @return The {@link Direction}
     */
    public Direction getDirection() {
        return dir;
    }
    
    /**
     * Sets the {@link SlopeType} of the Slope.
     */
    public void setSlopeType(SlopeType t) {
        type = t;
    }
    
    /**
     * Sets the {@link Direction} of the Slope.
     */
    public void setDirection(Direction d) {
        dir = d;
    }
}