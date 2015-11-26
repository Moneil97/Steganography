import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
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
		
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File("src/output.txt"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		boolean end = false;
		int x=1, y=0, c=0, k=0;
		//String message = "";
		try{
			while(!end){
				String s = "";
				for (int j=0; j < 8; j++){
					
					s += ((img.getRGB(x,y)>>(8*k))& 1);
					k++;
					if (k == 3){
						k=0;
						x++;
						if (x >= img.getWidth()){ 
							x=0; //Jump to next row if at the end
							y++;
							if (y >= img.getHeight()){
								System.err.println("Message too large");
								end = true;
								break;
							}
						}
					}
				}
				c = Integer.parseInt(s, 2);
				if (c != (char)0){
					writer.write((char) c);
				}
				else{
					writer.close();
					break;
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void say(Object s){
		System.out.println(s);
	}

}
