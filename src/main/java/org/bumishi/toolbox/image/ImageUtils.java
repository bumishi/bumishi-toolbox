package org.bumishi.toolbox.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


public final class ImageUtils {

	private static final String PICTRUE_FORMATE_JPG = "jpg";

	private ImageUtils() {
	}


	public static byte[] addWaterForImage(InputStream source,InputStream water) throws IOException{
		BufferedImage oldImage=ImageIO.read(source);
		BufferedImage newimage=new BufferedImage(oldImage.getWidth(),oldImage.getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics2D g= newimage.createGraphics();
		g.drawImage(oldImage, 0, 0, null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.8f));
        BufferedImage waterImage=ImageIO.read(water);
		g.drawImage(waterImage,oldImage.getWidth()-waterImage.getWidth()-50,oldImage.getHeight()-waterImage.getHeight()-30,null);
		ByteArrayOutputStream baos=new ByteArrayOutputStream(100);
		try {
			ImageIO.write(newimage,PICTRUE_FORMATE_JPG,baos);
			return baos.toByteArray();
		} finally {
			g.dispose();
			baos.close();
			source.close();
			water.close();
		}

	}
	public static void main(String[] args) throws IOException {
		  InputStream source=new FileInputStream("F:\\t.png");
		InputStream water=new FileInputStream("F:\\water.png");
		byte[] bytes = addWaterForImage(source,water);
		FileOutputStream outputStream=new FileOutputStream("f:\\t2.jpg");
		outputStream.write(bytes);
		outputStream.close();
	}
}