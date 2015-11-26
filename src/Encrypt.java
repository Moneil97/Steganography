import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Encrypt {

	public static void main(String[] args) {
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("src/qr.png")); //Open Image
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//String message = "Hello there, this is a secret message" + (char)0; //Add Null Terminator
		
		String message = "";
		try {
			//Scanner scan = new Scanner(new File("src/input2.txt"));
			Scanner scan = new Scanner(new File("src/Shakespeare.txt"));
			int a=0;
			while (scan.hasNextLine() && a < 2000){
				message += scan.nextLine();
				say(a++);
			}
			message +=(char)0;
			scan.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		int messageBits = message.length()*8; 
		say("Bits: " + messageBits);
		int imageSize = img.getWidth() * img.getHeight();
		say("Image size: " + imageSize);
		int bitsPerByteNeeded = (int) messageBits/(imageSize-1)+1; //-1 to account for number added pixel 0,0
		
//		if (bitsPerByteNeeded > 7){
//			say("image too small");
//			System.exit(0);
//		}
			
		
		//Whoops, each RGB is an int, not a byte. Only the blue channel is being changed
		
		
		say("Bits per byte needed: " + bitsPerByteNeeded);
		
		//@ pixel 0,0 set last 3 binary digits to bitsPerByteNeeded (3 bit == 0-7)
		img.setRGB(0,0, setLastBits(img.getRGB(0,0), bitsPerByteNeeded, 3));
		int x=1,y=0;
		boolean end = false;
		
		if (bitsPerByteNeeded >= 1000){ //undo (1)
			
			for (int i=0; i < message.length() && !end; i++){//Loop though each byte of message
				byte b = (byte) message.charAt(i);
				for (int j=7; j >= 0; j--){ //Loops though each bit of the byte
					
					if (((b >> j) & 1) == 1)//if bit == 1
						img.setRGB(x, y, img.getRGB(x,y) | 1); //set rightmost bit to 1
					else //if bit == 0
						img.setRGB(x, y, img.getRGB(x,y) &~ 1); //set rightmost bit to 0
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
		}
		else{
			
			String endBit = "";
			
			for (int i=0; i < message.length() && !end; i++){//Loop though each byte of message
				byte b = (byte) message.charAt(i);
				
				for (int j=7; j >= 0; j--){ //Loops though each bit of the byte
					
					if (endBit.length() < bitsPerByteNeeded){
						endBit += ((b >> j) & 1);
					}
					else{
						//say("before: ");
						//sayBin(img.getRGB(x, y));
						img.setRGB(x, y, setLastBits(img.getRGB(x, y), Integer.parseInt(endBit,2), bitsPerByteNeeded));
						//say("after: ");
						//sayBin(img.getRGB(x, y));
						
						endBit = "";
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
			}
		}
		
		
		
		
		try {
			ImageIO.write(img, "png", new File("src/testSave.png")); //Save Image
			say("Image saved successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	//TODO: less lazy/more efficient way of doing this
	private static int getNthLastDigits(int num, int digits) {
		String bin = Integer.toBinaryString(num);
		return Integer.parseInt(bin.substring(bin.length()-digits),2);
	}

	private static int setLastBits(int num, int val, int digits){
		
		for (int i=0; i < digits; i++, val = val>>1){
			
			if ((val&1)==1)
				num = (num | (1 << i));
			else
				num = num &~ (1 << i);
		}
			
			return num;
	}
	
	public static void sayBin(int s){
		System.out.println(Integer.toBinaryString(s));
	}
	
	public static void say(Object s){
		System.out.println(s);
	}

}
