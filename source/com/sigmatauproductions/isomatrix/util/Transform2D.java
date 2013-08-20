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

package com.sigmatauproductions.isomatrix.util;

/**
 * Stores a two-dimensional coordinate value.                           
 *
 * Used to store coordinate values for objects in 2D space.
 *
 * @author sigtau
 */
public final class Transform2D {
    
    /**
     * Stores the x coordinate value of the transform.
     */
    public int x;
    
    /**
     * Stores the y coordinate value of the transform.
     */
    public int y;
    
    
    /**
     * Initializes a new transform at the origin.
     */
    public Transform2D() {
        x = 0;
        y = 0;
    }
    
    /**
     * 
     * Initializes a new transform at the specified coordinates.
     * 
     * @param x
     * @param y
     * @param z 
     */
    public Transform2D(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Returns a new Transform2D at the origin in 2D space.
     * 
     * @return Returns a Transform2D at the coordinates {@code (0,0)}.
     */
    public static Transform2D getOrigin() {
        return new Transform2D(0, 0);
    }
    
    /**
     * Returns a new {@link Transform} object containing the x and y values of
     * this {@code Transform2D} object, with a z-value of 0.
     */
    public Transform toTransform() {
        return new Transform(x, y, 0);
    }
    
    /**
     * Returns an integer array containing the x and y values of the transform
     * in that order.
     * @return 
     */
    public int[] toArray() {
        int[] array = {x, y};
        return array;
    }
    
    /**
     * Returns a string representation of this Transform.  Overrides
     * {@code java.lang.Object.toString()}.  
     */
    @Override public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Transform2D { ");
        result.append("(x, y) = (" + x + ", " + y + ")");
        result.append(" }");

        return result.toString();
    }
}
