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
            int sumMag = 0;
            
            int[] chanInMin = {255,255,255};
            int[] chanInMax = {0,0,0};
            float[] chanInAvg = {0, 0, 0};
            int[] magIn = {255, 0};
            float magInAvg = 0;
            
            int[] chanRecMin = {255,255,255};
            int[] chanRecMax = {0,0,0};
            float[] chanRecAvg = {0, 0, 0};
            int[] magRec = {255, 0};
            float magRecAvg = 0;
            
            int[] chanElaMin = {255,255,255};
            int[] chanElaMax = {0,0,0};
            float[] chanElaAvg = {0, 0, 0};
            int[] magEla = {255, 0};
            float magElaAvg = 0;
          
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {                	
                    for (int band = 0; band < 3; band++) {
                        int d = Math.abs(original[r][c][band] - comp[r][c][band]);
                        diff[r][c][band] = d;
                        
                        int input = original[r][c][band];
                        int compr = comp[r][c][band];
                        int eladiff = diff[r][c][band];
                        
                        chanInMin[band] = (input < chanInMin[band]) ? input : chanInMin[band];
                        chanInMax[band] = (input > chanInMax[band]) ? input : chanInMax[band];
                        chanInAvg[band] = chanInAvg[band] + input; 
                        
                        chanRecMin[band] = (compr < chanRecMin[band]) ? compr : chanRecMin[band];
                        chanRecMax[band] = (compr > chanRecMax[band]) ? compr : chanRecMax[band];
                        chanRecAvg[band] = chanRecAvg[band] + compr; 

                        chanElaMin[band] = (eladiff < chanElaMin[band]) ? eladiff : chanElaMin[band];
                        chanElaMax[band] = (eladiff > chanElaMax[band]) ? eladiff : chanElaMax[band];
                        chanElaAvg[band] = chanElaAvg[band] + eladiff; 
                    }
                    
                    sumMag = original[r][c][0] + original[r][c][1] + original[r][c][2];
                    magInAvg = magInAvg + sumMag;
                    magIn[0] = (sumMag < magIn[0]) ? sumMag : magIn[0];
                    magIn[1] = (sumMag > magIn[1]) ? sumMag : magIn[1];
                    
                    sumMag = comp[r][c][0] + comp[r][c][1] + comp[r][c][2];
                    magRecAvg = magRecAvg + sumMag;
                    magRec[0] = (sumMag < magRec[0]) ? sumMag : magRec[0];
                    magRec[1] = (sumMag > magRec[1]) ? sumMag : magRec[1];
                    
                    sumMag = diff[r][c][0] + diff[r][c][1] + diff[r][c][2];
                    magElaAvg = magElaAvg + sumMag;
                    magEla[0] = (sumMag < magEla[0]) ? sumMag : magEla[0];
                    magEla[1] = (sumMag > magEla[1]) ? sumMag : magEla[1];
                  
                }
            }
            
            magInAvg = magInAvg / (height * width);
            chanInAvg[0] = chanInAvg[0] / (height * width);
            chanInAvg[1] = chanInAvg[1] / (height * width);
            chanInAvg[2] = chanInAvg[2] / (height * width);
            
            magRecAvg = magRecAvg / (height * width);
            chanRecAvg[0] = chanRecAvg[0] / (height * width);
            chanRecAvg[1] = chanRecAvg[1] / (height * width);
            chanRecAvg[2] = chanRecAvg[2] / (height * width);
            
            magElaAvg = magElaAvg / (height * width);
            chanElaAvg[0] = chanElaAvg[0] / (height * width);
            chanElaAvg[1] = chanElaAvg[1] / (height * width);
            chanElaAvg[2] = chanElaAvg[2] / (height * width);

            
            Apps.setInputStats(chanInMin, chanInMax, magIn, chanInAvg, magInAvg);
            Apps.setRecompStats(chanRecMin, chanRecMax, magRec, chanRecAvg, magRecAvg);
            Apps.setElaStats(chanElaMin, chanElaMax, magEla, chanElaAvg, magElaAvg);
            
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {                	
                    for (int band = 0; band < 3; band++) {
                    	diff[r][c][band] *= scale;
                    }
                }
            }
            difference = RGBImg(diff);
            
        }
        return difference;
    }
}
