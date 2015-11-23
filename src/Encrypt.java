import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

public class Encrypt {

	public static void main(String[] args) {
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("src/testImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String message = "Hello There, this is a secret message";
		
		
		for (int x=0; x < message.length(); x++)
			img.setRGB(x, 0, Color.green.getRGB());
		
		try {
			ImageIO.write(img, "png", new File("src/testSave.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
