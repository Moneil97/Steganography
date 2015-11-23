import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Decrypt {

	public static void main(String[] args) {
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("src/testSave.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int messageLength = "Hello There, this is a secret message".length();
		
		
		int x=0, y=0;
		String message = "";
		for (int i=0; i < messageLength; i++){
			byte b = 0;
			String s = "";
				for (int j=7; j >= 0; j--){
					b = (byte)((img.getRGB(x,y) >> 31) & 1);
					s += b;
					System.out.print(b);
					x++;
					
					if (x > img.getWidth()){
						x=0;
						y++;
						if (y > img.getHeight())
							System.err.println("Out of space");
					}
				}
				int c = Integer.parseInt(s, 2);
				System.out.print(" : " + c + " : " + (char)c);
				message += (char) c;
				System.out.println("");
		}
		
		say("Message: " + message);
	}
	
	public static void say(Object s){
		System.out.println(s);
	}

}
