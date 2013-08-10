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
 * A utility class for generating images using noise algorithms.
 *
 * The noise algorithms contained in this class were found on various online
 * sources, all of which appeared to be in the public domain.
 *
 * @author sigtau
 */
public final class Noise {

    /**
     * Returns the perlin noise value for the specified coordinates and octaves.
     *
     * This function was originally created by Ken Perlin and is publicly
     * available in this form on his website.
     *
     * @param x The x location on the noise map, clamped from 0.0 to 1.0.
     * @param y The y location on the noise map, clamped from 0.0 to 1.0.
     * @param z The number of octaves on the noise map, clamped from 0.0 to 1.0.
     * @return Returns
     */
    // JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE
    // COPYRIGHT 2002 KEN PERLIN.
    static public double perlin(double x, double y, double z) {
        int X = (int) Math.floor(x) & 255, // FIND UNIT CUBE THAT
                Y = (int) Math.floor(y) & 255, // CONTAINS POINT.
                Z = (int) Math.floor(z) & 255;
        x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
        y -= Math.floor(y);                                // OF POINT IN CUBE.
        z -= Math.floor(z);
        double u = fade(x), // COMPUTE FADE CURVES
                v = fade(y), // FOR EACH OF X,Y,Z.
                w = fade(z);
        int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z, // HASH COORDINATES OF
                B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z;      // THE 8 CUBE CORNERS,

        return lerp(w, lerp(v, lerp(u, grad(p[AA], x, y, z), // AND ADD
                grad(p[BA], x - 1, y, z)), // BLENDED
                lerp(u, grad(p[AB], x, y - 1, z), // RESULTS
                grad(p[BB], x - 1, y - 1, z))),// FROM  8
                lerp(v, lerp(u, grad(p[AA + 1], x, y, z - 1), // CORNERS
                grad(p[BA + 1], x - 1, y, z - 1)), // OF CUBE
                lerp(u, grad(p[AB + 1], x, y - 1, z - 1),
                grad(p[BB + 1], x - 1, y - 1, z - 1))));
    }
    
    /**
     * A local function for fading smoothly between two points.
     * @param t
     */
    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }
    
    /**
     * A local function for linear interpolation between two points.
     * @param t The time value of the interpolation.
     * @param a Point A.
     * @param b Point B.
     */
    private static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }
    
    /**
     * 
     * Converts the specified hash code into 12 gradient directions.
     * 
     */
    private static double grad(int hash, double x, double y, double z) {
        int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
        double u = h < 8 ? x : y, // INTO 12 GRADIENT DIRECTIONS.
                v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
    
    /**
     * The list of permutations used for perlin noise.
     */
    private static final int p[] = new int[512], permutation[] = {151, 160, 137, 91, 90, 15,
        131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23,
        190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33,
        88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166,
        77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244,
        102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196,
        135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123,
        5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
        223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
        129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228,
        251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107,
        49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254,
        138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
    };

    static {
        for (int i = 0; i < 256; i++) {
            p[256 + i] = p[i] = permutation[i];
        }
    }
    
    /**
     * 
     * Generates a value-noise value for the specified coordinates.
     * 
     * @param x The X coordinate of the noise, clamped from 0.0 to 1.0.
     * @param y The Y coordinate of the noise, clamped from 0.0 to 1.0.
     * @param nbOctave The level of octaves (maximum of 8) in the value noise.
     */
    public static int value(double x, double y, int nbOctave) {
        int result = 0;
        int frequence256 = 256;
        int sx = (int) ((x) * frequence256);
        int sy = (int) ((y) * frequence256);
        int octave = nbOctave;
        while (octave != 0) {
            int bX = sx & 0xFF;
            int bY = sy & 0xFF;

            int sxp = sx >> 8;
            int syp = sy >> 8;


            //Compute noise for each corner of current cell
            int Y1376312589_00 = syp * 1376312589;
            int Y1376312589_01 = Y1376312589_00 + 1376312589;

            int XY1376312589_00 = sxp + Y1376312589_00;
            int XY1376312589_10 = XY1376312589_00 + 1;
            int XY1376312589_01 = sxp + Y1376312589_01;
            int XY1376312589_11 = XY1376312589_01 + 1;

            int XYBASE_00 = (XY1376312589_00 << 13) ^ XY1376312589_00;
            int XYBASE_10 = (XY1376312589_10 << 13) ^ XY1376312589_10;
            int XYBASE_01 = (XY1376312589_01 << 13) ^ XY1376312589_01;
            int XYBASE_11 = (XY1376312589_11 << 13) ^ XY1376312589_11;

            int alt1 = (XYBASE_00 * (XYBASE_00 * XYBASE_00 * 15731 + 789221) + 1376312589);
            int alt2 = (XYBASE_10 * (XYBASE_10 * XYBASE_10 * 15731 + 789221) + 1376312589);
            int alt3 = (XYBASE_01 * (XYBASE_01 * XYBASE_01 * 15731 + 789221) + 1376312589);
            int alt4 = (XYBASE_11 * (XYBASE_11 * XYBASE_11 * 15731 + 789221) + 1376312589);


            /*START BLOCK FOR VALUE NOISE*/

            alt1 &= 0xFFFF;
            alt2 &= 0xFFFF;
            alt3 &= 0xFFFF;
            alt4 &= 0xFFFF;

            /*END BLOCK FOR VALUE NOISE*/


            /*START BLOCK FOR LINEAR INTERPOLATION*/
            //BiLinear interpolation 
         /*
             int f24=(bX*bY)>>8;
             int f23=bX-f24;
             int f14=bY-f24;
             int f13=256-f14-f23-f24;

             int val=(alt1*f13+alt2*f23+alt3*f14+alt4*f24);
             */
            /*END BLOCK FOR LINEAR INTERPOLATION*/



            //BiCubic interpolation ( in the form alt(bX) = alt[n] - (3*bX^2 - 2*bX^3) * (alt[n] - alt[n+1]) )
         /*START BLOCK FOR BICUBIC INTERPOLATION*/
            int bX2 = (bX * bX) >> 8;
            int bX3 = (bX2 * bX) >> 8;
            int _3bX2 = 3 * bX2;
            int _2bX3 = 2 * bX3;
            int alt12 = alt1 - (((_3bX2 - _2bX3) * (alt1 - alt2)) >> 8);
            int alt34 = alt3 - (((_3bX2 - _2bX3) * (alt3 - alt4)) >> 8);


            int bY2 = (bY * bY) >> 8;
            int bY3 = (bY2 * bY) >> 8;
            int _3bY2 = 3 * bY2;
            int _2bY3 = 2 * bY3;
            int val = alt12 - (((_3bY2 - _2bY3) * (alt12 - alt34)) >> 8);

            val *= 256;
            /*END BLOCK FOR BICUBIC INTERPOLATION*/


            //Accumulate in result
            result += (val << octave);

            octave--;
            sx <<= 1;
            sy <<= 1;

        }
        return result >>> (16 + nbOctave + 1);
    }

    /**
     * 
     * Generates a gradient noise value at the specified coordinate.
     * 
     * @param x The x coordinate of the noise, clamped from 0.0 to 1.0.
     * @param y The y coordinate of the noise, clamped from 0.0 to 1.0.
     * @param nbOctave The octave level of the noise (max 8).
     */
    public static int gradient(double x, double y, int nbOctave) {
        int result = 0;
        int frequence256 = 256;
        int sx = (int) ((x) * frequence256);
        int sy = (int) ((y) * frequence256);
        int octave = nbOctave;
        while (octave != 0) {
            int bX = sx & 0xFF;
            int bY = sy & 0xFF;

            int sxp = sx >> 8;
            int syp = sy >> 8;


            //Compute noise for each corner of current cell
            int Y1376312589_00 = syp * 1376312589;
            int Y1376312589_01 = Y1376312589_00 + 1376312589;

            int XY1376312589_00 = sxp + Y1376312589_00;
            int XY1376312589_10 = XY1376312589_00 + 1;
            int XY1376312589_01 = sxp + Y1376312589_01;
            int XY1376312589_11 = XY1376312589_01 + 1;

            int XYBASE_00 = (XY1376312589_00 << 13) ^ XY1376312589_00;
            int XYBASE_10 = (XY1376312589_10 << 13) ^ XY1376312589_10;
            int XYBASE_01 = (XY1376312589_01 << 13) ^ XY1376312589_01;
            int XYBASE_11 = (XY1376312589_11 << 13) ^ XY1376312589_11;

            int alt1 = (XYBASE_00 * (XYBASE_00 * XYBASE_00 * 15731 + 789221) + 1376312589);
            int alt2 = (XYBASE_10 * (XYBASE_10 * XYBASE_10 * 15731 + 789221) + 1376312589);
            int alt3 = (XYBASE_01 * (XYBASE_01 * XYBASE_01 * 15731 + 789221) + 1376312589);
            int alt4 = (XYBASE_11 * (XYBASE_11 * XYBASE_11 * 15731 + 789221) + 1376312589);

            /*
             *NOTE : on  for true grandiant noise uncomment following block
             * for true gradiant we need to perform scalar product here, gradiant vector are created/deducted using
             * the above pseudo random values (alt1...alt4) : by cutting thoses values in twice values to get for each a fixed x,y vector 
             * gradX1= alt1&0xFF 
             * gradY1= (alt1&0xFF00)>>8
             *
             * the last part of the PRN (alt1&0xFF0000)>>8 is used as an offset to correct one of the gradiant problem wich is zero on cell edge
             *
             * source vector (sXN;sYN) for scalar product are computed using (bX,bY)
             *
             * each four values  must be replaced by the result of the following 
             * altN=(gradXN;gradYN) scalar (sXN;sYN)
             *
             * all the rest of the code (interpolation+accumulation) is identical for value & gradiant noise
             */


            /*START BLOCK FOR TRUE GRADIANT NOISE*/

            int grad1X = (alt1 & 0xFF) - 128;
            int grad1Y = ((alt1 >> 8) & 0xFF) - 128;
            int grad2X = (alt2 & 0xFF) - 128;
            int grad2Y = ((alt2 >> 8) & 0xFF) - 128;
            int grad3X = (alt3 & 0xFF) - 128;
            int grad3Y = ((alt3 >> 8) & 0xFF) - 128;
            int grad4X = (alt4 & 0xFF) - 128;
            int grad4Y = ((alt4 >> 8) & 0xFF) - 128;


            int sX1 = bX >> 1;
            int sY1 = bY >> 1;
            int sX2 = 128 - sX1;
            int sY2 = sY1;
            int sX3 = sX1;
            int sY3 = 128 - sY1;
            int sX4 = 128 - sX1;
            int sY4 = 128 - sY1;
            alt1 = (grad1X * sX1 + grad1Y * sY1) + 16384 + ((alt1 & 0xFF0000) >> 9); //to avoid seams to be 0 we use an offset
            alt2 = (grad2X * sX2 + grad2Y * sY2) + 16384 + ((alt2 & 0xFF0000) >> 9);
            alt3 = (grad3X * sX3 + grad3Y * sY3) + 16384 + ((alt3 & 0xFF0000) >> 9);
            alt4 = (grad4X * sX4 + grad4Y * sY4) + 16384 + ((alt4 & 0xFF0000) >> 9);

            /*END BLOCK FOR TRUE GRADIANT NOISE */


            /*START BLOCK FOR VALUE NOISE*/

            alt1 &= 0xFFFF;
            alt2 &= 0xFFFF;
            alt3 &= 0xFFFF;
            alt4 &= 0xFFFF;

            /*END BLOCK FOR VALUE NOISE*/


            /*START BLOCK FOR LINEAR INTERPOLATION*/
            //BiLinear interpolation 
         /*
             int f24=(bX*bY)>>8;
             int f23=bX-f24;
             int f14=bY-f24;
             int f13=256-f14-f23-f24;

             int val=(alt1*f13+alt2*f23+alt3*f14+alt4*f24);
             */
            /*END BLOCK FOR LINEAR INTERPOLATION*/



            //BiCubic interpolation ( in the form alt(bX) = alt[n] - (3*bX^2 - 2*bX^3) * (alt[n] - alt[n+1]) )
         /*START BLOCK FOR BICUBIC INTERPOLATION*/
            int bX2 = (bX * bX) >> 8;
            int bX3 = (bX2 * bX) >> 8;
            int _3bX2 = 3 * bX2;
            int _2bX3 = 2 * bX3;
            int alt12 = alt1 - (((_3bX2 - _2bX3) * (alt1 - alt2)) >> 8);
            int alt34 = alt3 - (((_3bX2 - _2bX3) * (alt3 - alt4)) >> 8);


            int bY2 = (bY * bY) >> 8;
            int bY3 = (bY2 * bY) >> 8;
            int _3bY2 = 3 * bY2;
            int _2bY3 = 2 * bY3;
            int val = alt12 - (((_3bY2 - _2bY3) * (alt12 - alt34)) >> 8);

            val *= 256;
            /*END BLOCK FOR BICUBIC INTERPOLATION*/


            //Accumulate in result
            result += (val << octave);

            octave--;
            sx <<= 1;
            sy <<= 1;

        }
        return result >>> (16 + nbOctave + 1);
    }
}
