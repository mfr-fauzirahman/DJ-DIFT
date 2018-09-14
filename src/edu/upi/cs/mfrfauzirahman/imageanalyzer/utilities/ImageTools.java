package edu.upi.cs.mfrfauzirahman.imageanalyzer.utilities;

import java.awt.image.BufferedImage;

public class ImageTools {

	public ImageTools() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * This class is simply holds specific pixel values for various uses.
	 * 
	 * @author Robert Streetman
	 */	
	public enum Pixel {
	    //Access these pixel values by name
	    BLACK(0, 0, 0),
	    BLUE(0, 0, 255),
	    CYAN(0, 255, 255),
	    GREEN(0, 255, 0),
	    MAGENTA(255, 0, 255),
	    ORANGE(255, 128, 0),
	    RED(255, 0, 0),
	    WHITE(255, 255, 255),
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
     * @param raw   int[][][] representing RGB pixels of image.
     * @return BufferedImage built from RGB array
     */
    public static BufferedImage RGBImg(int[][][] raw) {
        BufferedImage img = null;
        int height = raw.length;
        int width = raw[0].length;
        
        if (height > 0 && width > 0 || raw[0][0].length == 3) {
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                    img.setRGB(column, row, (raw[row][column][0] << 16)
                            | (raw[row][column][1] << 8) | (raw[row][column][2]));
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

