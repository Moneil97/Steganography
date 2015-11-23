import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Encrypt {

	public static void main(String[] args) {
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("src/testImage.png")); //Open Image
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String message = "Hello there, this is a secret message" + (char)0; //Add Null Terminator
		
		int x=0,y=0;
		boolean end = false;
		for (int i=0; i < message.length() && !end; i++){//Loop though each byte of message
			byte b = (byte) message.charAt(i);
			for (int j=7; j >= 0; j--){ //Loops though each bit of the byte

				if (((b >> j) & 1) == 1)//if bit == 1
					img.setRGB(x, y, img.getRGB(x,y) | (1 << 31)); //set 32nd bit to 1
				else //if bit == 0
					img.setRGB(x, y, img.getRGB(x,y) & ~ (1 << 31)); //set 32nd bit to 0
				x++;
				
				if (x > img.getWidth()){ 
					x=0; //Jump to next row if at the end
					y++;
					if (y > img.getHeight()){
						System.err.println("Message too large");
						end = true;
						break;
					}
				}
			}
		}
		
		try {
			ImageIO.write(img, "png", new File("src/testSave.png")); //Save Image
			say("Image saved successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void say(Object s){
		System.out.println(s);
	}

}
