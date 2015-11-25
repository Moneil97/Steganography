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
		
		int x=0, y=0, c=0;
		String message = "";
		do{
			String s = "";
				for (int j=0; j < 8; j++){
					s += ((img.getRGB(x,y) >> 0) & 1);
					x++;
					
					if (x >= img.getWidth()){
						x=0;
						y++;
						say(y);
						if (y >= img.getHeight())
							System.err.println("Out of space");
					}
				}
				c = Integer.parseInt(s, 2);
				//System.out.println(s + " : " + c + " : " + (char)c);
				message += (char) c;
		}while (c != (char)0);
		
		say("Message: " + message.substring(0, message.length()-1));
	}
	
	public static void say(Object s){
		System.out.println(s);
	}

}
