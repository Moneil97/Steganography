import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Encrypt {

	static BufferedImage img;
	static int bitsPerByteNeeded;
	
	public static void main(String[] args) {
		
		
		try {
		    img = ImageIO.read(new File("src/testImage.png")); //Open Image
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long imageSize = img.getWidth() * img.getHeight() * 3;
		say("imageSize: " + imageSize);
		
		long messageBits = 0;
		try {
			
			File f = new File("src/input2.txt");
			//File f = new File("src/Shakespeare.txt");
			messageBits = (f.length()+1)*8; //+1 for Null Terminator
			say("messageBits: " + messageBits);
			bitsPerByteNeeded = (int) (messageBits/(imageSize-3)+1); //-3 to account for pixel 0,0 being used
			say("Bits per byte needed: " + bitsPerByteNeeded);
			if (bitsPerByteNeeded > 8){
				System.err.println("image too small");
				System.exit(0);
			}
			//@ pixel 0,0 set last 4 binary digits (1R1G2B) to bitsPerByteNeeded (4 bit == 0-15)
			img.setRGB(0, 0, setRGBbits(img.getRGB(0, 0), bitsPerByteNeeded, 4));
			
			Scanner scan = new Scanner(f);
			scan.close();
			scan = new Scanner(f);
			//int a=0;
			while (scan.hasNextLine()){
				//say(a++);
				
				if (bitsPerByteNeeded == 1){
					singleBit(scan.nextLine() + "\n");
				}
				else{
					multiBit(scan.nextLine() + "\n");
				}
			}
			
			//Add Null Terminator for decrypter to use
			if (bitsPerByteNeeded == 1){
				singleBit(String.valueOf((char)0));
			}
			else{
				multiBit(String.valueOf((char)0));
				if (endBit.length() > 0)
					img.setRGB(x, y, setRGBbits(img.getRGB(x, y), Integer.parseInt(endBit,2), endBit.length()));
			}
			
			scan.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		
		try {
			ImageIO.write(img, "png", new File("src/testSave.png")); //Save Image
			say("Image saved successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	static int x=1, y=0, k=0;
	static boolean end = false;
	
	private static void singleBit(String line){
		
		for (int i=0; i < line.length() && !end; i++){//Loop though each byte of message
			byte b = (byte) line.charAt(i);
			
			for (int j=7; j >= 0; j--){ //Loops though each bit of the byte
				
				if (((b >> j) & 1) == 1)//if bit == 1
					img.setRGB(x, y, img.getRGB(x,y) | (1<<(8*k))); //set R,G,or B rightmost bit to 1
				else //if bit == 0
					img.setRGB(x, y, img.getRGB(x,y) &~ (1<<(8*k))); //set R,G,or B rightmost bit to 0
				
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
		}
	}
	
	static String endBit = "";
	
	private static void multiBit(String line){
		
		for (int i=0; i < line.length() && !end; i++){//Loop though each byte of message
			byte b = (byte) line.charAt(i);
			//say((char)b);
			for (int j=7; j >= 0; j--){ //Loops though each bit of the byte
				
				//if (endBit.length() < bitsPerByteNeeded*3){ //*3 for each color channel
					endBit += ((b >> j) & 1);
					//say(endBit);
				//}
				/*else*/ if (endBit.length() == bitsPerByteNeeded*3){
					//say("before: ");
					//sayBin(img.getRGB(x, y));
					img.setRGB(x, y, setRGBbits(img.getRGB(x, y), Integer.parseInt(new StringBuilder(endBit).reverse().toString(),2), bitsPerByteNeeded*3));
					//say("after: ");
					//sayBin(img.getRGB(x, y));
					
					endBit = "";
					x++;
					if (x >= img.getWidth()){ 
						x=0; //Jump to next row if at the end
						y++;
						if (y >= img.getHeight()){
							System.err.println("Message too large");
							System.exit(0);
							end = true;
							break;
						}
					}
				}
				
				
			}
		}
		
	}
	
	//TODO: less lazy/more efficient way of doing this
	private static int getNthLastDigits(int num, int digits) {
		String bin = Integer.toBinaryString(num);
		return Integer.parseInt(bin.substring(bin.length()-digits),2);
	}
	
	private static int setRGBbits(int rgb, int val, int digits){
		
		//digits < 25
		
		for (int i=0, k=0; i*3+k < digits; val = val>>1){
			
			if ((val&1)==1)
				rgb = rgb | (1<<(8*k+i));
			else
				rgb = rgb &~ (1<<(8*k+i));
			
			k++;
			if (k == 3){
				k=0;
				i++;
			}
			
		}
			return rgb;
	}
	
	public static void sayBin(int s){
		System.out.println(Integer.toBinaryString(s));
	}
	
	public static void say(Object s){
		System.out.println(s);
	}

}
