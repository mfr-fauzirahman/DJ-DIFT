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
 * This class is simply holds specific pixel values for various uses.
 * 
 * @author Robert Streetman
 */	

public class ImageTools {

	public ImageTools() {
		// TODO Auto-generated constructor stub
	}
	
	public enum Pixel {
	    //Access these pixel values by name
	    CYAN(0, 255, 255),
	    MAGENTA(255, 0, 255),
	    YELLOW(255, 255, 0);
	    
	    private int[] rgb = new int[3];
	    
	    Pixel(int red, int green, int blue) {
	        rgb[0] = red;
	        rgb[1] = green;
	        rgb[2] = blue;
	    }

	    /**
	     * Returns the RGB value of the enumerated color.
	     * 
	     * @return The RGB value of the enumerated color.
	     */
	    public int[] RGB() {
	        return rgb;
	    }
	}
	
	/**
     * Method for choosing the masking color.
     * 
     * @param color	Color name chosen
     * @return      RGB values based on color chosen  
     */
	public static int[] MaskColor(String color){
		int[] rgb = new int[3];
		
		
		switch(color) {
			case "MAGENTA": //Magenta
				rgb = PixelColor(255, 0, 255);
				break;
			case "CYAN": //Cyan
				rgb = PixelColor(0, 255, 255);
				break;
			default: //Yellow
				rgb = PixelColor(255, 255, 0);
		}
		
		return rgb;
	}
	
	/**
     * Method for choosing the masking color.
     * 
     * @param rgb	RGB values respectively received from MaskColor
     * @return      RGB array based on input values  
     */
	public static int[] PixelColor(int red, int green, int blue) {
		int[] rgb = new int[3];
		
		rgb[0] = red;
		rgb[1] = green;
		rgb[2] = blue;
		
		return rgb;
	}

	/**
     * Send this method a 32-bit pixel value from BufferedImage to get the RGB.
     * 
     * @param bits  The 32-bit BufferedImage pixel value
     * @return      RGB values extracted from pixel  
     */
    private static int[] intRGB(int bits) {
        //Java rgb values are actually 4 bytes (r,g,b,a) pressed into one 32-bit integer
        int[] rgb = { (bits >> 16) & 0xff, (bits >> 8) & 0xff, bits & 0xff };
        
        //Don't propagate bad pixel values
        for (int i = 0; i < 3; i++) {
            if (rgb[i] < 0) {
                rgb[i] = 0;
            } else if (rgb[i] > 255) {
                rgb[i] = 255;
            }
        }
        
        return rgb;
    }
    
    /**
     * Send this method an array of RGB pixels (int) to get a BufferedImage.
     * 
     * @param raw int[][][] representing RGB pixels of image.
     * @return BufferedImage built from RGB array
     */
    public static BufferedImage RGBImg(int[][][] raw) {
        int height = raw.length;
        int width = raw[0].length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        if (height > 0 && width > 0 || raw[0][0].length == 3) {
            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                	
                	/**
                	 * Warning: setRGB with red/green values over 255 may cause incorrect result.
                	 * @author Muhammad Fauzi Rahman
                	 * @see 
                	 */
                    img.setRGB(column, row, (raw[row][column][0] << 16) | (raw[row][column][1] << 8) | (raw[row][column][2]));
                }
            }
        }
        
        return img;
    }
	
    /**
     * Send this method a BufferedImage to get an RGB array (value 0-255).
     * 
     * @param img   BufferedImage, the input image from which to extract RGB
     * @return      A 3-dimensional array of RGB values from image
     */
    public static int[][][] RGBArray(BufferedImage img) {
        int[][][] rgb = null;
        int height = img.getHeight();
        int width = img.getWidth();
        
        if (height > 0 && width > 0) {
            rgb = new int[height][width][3];

            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                    rgb[row][column] = intRGB(img.getRGB(column, row));
                }
            }
        }
        
        return rgb;
    }
}

