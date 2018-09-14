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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

/**
 * This class is used to rebuild the comparator based from quality level and creating a difference image (ELA).
 * 
 * @author Robert Streetman
 * @edited Muhammad Fauzi Rahman
 * @changes Applying multiplication error scale
 */	
public class ImageELA extends ImageTools {

	public ImageELA() {
		// TODO Auto-generated constructor stub
	}

	/**
     * Send this method a BufferedImage which needs to be compressed to an arbitrary JPEG level.
     * 
     * @param image Source image to compress
     * @param compressionLevel  JPEG compression level, generally ~0.95
     * @return BufferedImage Compressed version of source image
     * @throws java.io.IOException
     */
    //TODO:Add exceptions for bad input
    public static BufferedImage GetCompressedImage(BufferedImage image, float compressionLevel) throws IOException {
        BufferedImage compressed = null;
        
        // The important part: Create in-memory stream
        ByteArrayOutputStream recomp = new ByteArrayOutputStream();
        ImageOutputStream outputStream = ImageIO.createImageOutputStream(recomp);
        
        // Obtain writer for JPEG format
        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        
        JPEGImageWriteParam jpgParams = new JPEGImageWriteParam(null);
        //JPEG writer parameter 
        jpgParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgParams.setCompressionQuality(compressionLevel);
        
        
        // Set your in-memory stream as the output
        jpgWriter.setOutput(outputStream);
                
        // Write image as JPEG w/configured settings to the in-memory stream
        // (the IIOImage is just an aggregator object, allowing you to associate
        // thumbnails and metadata to the image, it "does" nothing)
        jpgWriter.write(null, new IIOImage(image, null, null), jpgParams);
       

        // Dispose the writer to free resources
        jpgWriter.dispose();
        
        byte[] jpegData;
        jpegData = recomp.toByteArray();
        ByteArrayInputStream input = new ByteArrayInputStream(jpegData);
        compressed = ImageIO.read(input);
        
        return compressed;
    }
    
    /**
     * Creates a difference image from the original image and the slightly re-compressed image.
     * 
     * @param image             The uncompressed original image
     * @param compressed        The compressed version of the original
     * @return BufferedImage    Difference image: each pixel's RGB is the difference between the original & compressed RGB values.
     */
    public static BufferedImage GetDifferenceImage(BufferedImage image, BufferedImage compressed, int scale) {
        BufferedImage difference = null;
        int height = image.getHeight();
        int width = image.getWidth();
        
        if (height == compressed.getHeight() && width == compressed.getWidth()) {
            int[][][] original = RGBArray(image);
            int[][][] comp = RGBArray(compressed);
            int[][][] diff = new int[height][width][3];
            
             for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {                	
                    for (int band = 0; band < 3; band++) {
                        int d = Math.abs(original[r][c][band] - comp[r][c][band]);
                        diff[r][c][band] = d * scale;
                        
                        /**
                         * Prevent inaccuracy when getting RGB values from ELA image later.
                         */
                        diff[r][c][band] = diff[r][c][band] % 256;
                    }
                }
            }
            difference = RGBImg(diff);
            
        }
        return difference;
    }
}
