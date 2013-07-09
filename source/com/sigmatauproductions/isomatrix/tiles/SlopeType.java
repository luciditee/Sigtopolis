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
 * Defines a type of {@link Slope} in conjunction with a {@link Direction} to
 * create hills and other deformations to the terrain.
 * 
 * <ul>
 *  <li>{@code NONE} defines a flat tile, i.e. no deformation.</li>
 *  <li>{@code STANDARD} defines a normal slope; a basic uphill/downhill.</li>
 *  <li>{@code BOTTOM_DIAGONAL} defines a corner piece to the bottom of a
 *  hill.</li>
 *  <li>{@code MIDSEGMENT} defines the corner of a slope that does not flatten
 *  out at either the top or the bottom, as with {@code BOTTOM_DIAGONAL} or
 *  {@code TOP_DIAGONAL}.</li>
 *  <li>{@code TOP_DIAGONAL} defines the corner piece to the top of a hill.</li>
 * </ul>
 *
 * @author sigtau
 */
public enum SlopeType {
    NONE, STANDARD, BOTTOM_DIAGONAL, MIDSEGMENT, TOP_DIAGONAL
}
