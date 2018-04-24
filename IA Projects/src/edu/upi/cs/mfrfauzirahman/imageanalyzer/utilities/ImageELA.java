package edu.upi.cs.mfrfauzirahman.imageanalyzer.utilities;

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

import edu.upi.cs.mfrfauzirahman.imageanalyzer.Apps;

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
        if(compressionLevel != 1) {
            jpgParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgParams.setCompressionQuality(compressionLevel);
        }else {
        	jpgParams.setCompressionMode(ImageWriteParam.MODE_DEFAULT);
        }
        
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
            
            int[] chanInMax = {0,0,0};
            int[] chanInMin = {255,255,255};
            int[] inMag = {255, 0};
            
            int[] chanElaMax = {0,0,0};
            int[] chanElaMin = {255,255,255};
            int[] elaMag = {255, 0};
            
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {                	
                    for (int band = 0; band < 3; band++) {
                        int d = Math.abs(original[r][c][band] - comp[r][c][band]);
                        diff[r][c][band] = d;
                        
                        int input = original[r][c][band];
                        int elav = d;
                        //int compr = comp[r][c][band];
                        
                        
                        chanInMin[band] = (input < chanInMin[band]) ? input : chanInMin[band];
                        chanInMax[band] = (input > chanInMax[band]) ? input : chanInMax[band];
                        
                        chanElaMin[band] = (elav < chanElaMin[band]) ? elav : chanElaMin[band];
                        chanElaMax[band] = (elav > chanElaMax[band]) ? elav : chanElaMax[band];
                    }
                    
                    int sumMag = original[r][c][0] + original[r][c][1] + original[r][c][2];
                    inMag[0] = (sumMag < inMag[0]) ? sumMag : inMag[0];
                    inMag[1] = (sumMag > inMag[0]) ? sumMag : inMag[1];
                    
                    sumMag = diff[r][c][0] + diff[r][c][1] + diff[r][c][2];
                    elaMag[0] = (sumMag < elaMag[0]) ? sumMag : elaMag[0];
                    elaMag[1] = (sumMag > elaMag[0]) ? sumMag : elaMag[1];
                }
            }

            Apps.setInputStats(chanInMin, chanInMax, inMag);
          
            
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    for (int band = 0; band < 3; band++) {
                        diff[r][c][band] *= scale;
                        //diff[r][c][band]  = (diff[r][c][band] > 255) ? 255 : (diff[r][c][band] < 0) ? 0 : diff[r][c][band];
                    }
                }
            }
            difference = RGBImg(diff);
            
        }
        return difference;
    }
}
