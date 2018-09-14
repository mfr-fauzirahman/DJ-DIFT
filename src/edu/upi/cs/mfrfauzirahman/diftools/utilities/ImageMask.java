/**
 * Copyright 2018 Muhammad Fauzi Rahman 
 * Based on (C) 2017 Robert Streetman's ELA
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.upi.cs.mfrfauzirahman.diftools.utilities;

import java.awt.image.BufferedImage;

/**
 * This class is used for masking the original image based on the difference (ELA) image.
 * 
 * @author Robert Streetman
 */	

public class ImageMask extends ImageTools{

	public ImageMask() {
		// TODO Auto-generated constructor stub
	}
	
	/**
     * Send this method a BufferedImage base, a BufferedImage mask, an int[] mask color,
     * and an int threshold to mask the base image with all pixels in the mask image that
     * meet/exceed the threshold with the supplied color.
     * 
     * @param baseImage     The base image which is to be masked over.
     * @param maskImage     The masking image. This is a difference image.
     * @param maskColor     The RGB pixel values desired for the mask color.
     * @param threshold     Max pixel value (r+g+b) allowed before marking pixel as changed.
     * @return              BufferedImage where the base image has 'changed' pixels masked.
     */
    public static BufferedImage MaskImages(BufferedImage baseImage, BufferedImage maskImage,
            int[] maskColor, int threshold) {
        BufferedImage result = null;
        int height = baseImage.getHeight();
        int width = baseImage.getWidth();
        
        if (maskColor.length == 3 && height == maskImage.getHeight()
                && width == maskImage.getWidth()) {
            int[][][] imgOrig = RGBArray(baseImage);
            int[][][] imgMask = RGBArray(maskImage);
            int[][][] imgResult = new int[height][width][3];
            
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    //Measure total magnitude of pixel
                    int sumMaskPixel = 0;
                    
                    for (int band = 0; band < 3; band++) {
                        sumMaskPixel += imgMask[r][c][band];
                    }
                    
                    //If pixel magnitude > threshold, then mask w/ color
                    if (sumMaskPixel > threshold) {
                        imgResult[r][c] = maskColor;
                    } else {
                        imgResult[r][c] = imgOrig[r][c];
                    }
                }
            }
            
            result = RGBImg(imgResult);
        }
        
        return result;
    }
}