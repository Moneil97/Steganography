import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Encrypt {

	public static void main(String[] args) {
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("src/testImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String message = "Hello There, this is a secret message";
		
		int x=0,y=0;
		for (int i=0; i < message.length(); i++){
			byte b = (byte) message.charAt(i);
				for (int j=7; j >= 0; j--){

					if (((b >> j) & 1) == 1)//if bit == 1
						img.setRGB(x, y, img.getRGB(x,y) | (1 << 31)); //set 32nd bit to 1
					else
						img.setRGB(x, y, img.getRGB(x,y) & ~ (1 << 31)); //set 32nd bit to 0
					x++;
					
					if (x > img.getWidth()){
						x=0;
						y++;
						if (y > img.getHeight())
							System.err.println("Out of space");
					}
				}
		}
		
		try {
			ImageIO.write(img, "png", new File("src/testSave.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void say(Object s){
		System.out.println(s);
	}

}
