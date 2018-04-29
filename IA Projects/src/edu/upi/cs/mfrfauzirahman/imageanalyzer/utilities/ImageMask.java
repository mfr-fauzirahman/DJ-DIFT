package edu.upi.cs.mfrfauzirahman.imageanalyzer.utilities;

import java.awt.image.BufferedImage;

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
            int[] maskColor, int threshold, int scale) {
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
                    if (sumMaskPixel > threshold * scale) {
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
